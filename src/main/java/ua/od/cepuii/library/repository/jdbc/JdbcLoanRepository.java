package ua.od.cepuii.library.repository.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.od.cepuii.library.db.ConnectionPool;
import ua.od.cepuii.library.dto.FilterParams;
import ua.od.cepuii.library.entity.Loan;
import ua.od.cepuii.library.entity.enums.LoanStatus;
import ua.od.cepuii.library.repository.LoanRepository;
import ua.od.cepuii.library.repository.executor.DbExecutor;

import java.sql.*;
import java.util.*;

import static ua.od.cepuii.library.repository.jdbc.RepositoryUtil.prepareForLike;
import static ua.od.cepuii.library.repository.jdbc.RepositoryUtil.validateForLike;

public class JdbcLoanRepository implements LoanRepository {
    private static final Logger log = LoggerFactory.getLogger(JdbcLoanRepository.class);
    private final DbExecutor<Loan> dbExecutor;
    private final ConnectionPool connectionPool;
    private static final String INSERT_LOAN = "INSERT INTO loan (user_id, book_id, duration, status_id) VALUES (?,?,?,?)";
    private static final String SELECT_BY_ID = "SELECT id, user_id,book_id, start_time, duration, status_id, fine FROM loan WHERE id=?  ";
    private static final String SELECT_ALL = "SELECT loan.id l_id, loan.book_id l_bookId, loan.user_id l_userId," + "loan.start_time l_start_time, " + "loan.duration l_duration, b.title b_title, b.date_publication b_date, b.fine b_fine, ls.status l_status " + "FROM loan " + "JOIN loan_status ls on ls.id = loan.status_id " + "JOIN book b on b.id = loan.book_id ";
    private static final String ORDER_BY = "ORDER BY status, b.title, duration ";
    private static final String LIMIT_OFFSET = "LIMIT ? OFFSET ?;";
    private static final String SELECT_ALL_WITH_LIMITS = SELECT_ALL + "WHERE b.title LIKE ? AND ls.status LIKE ? AND loan.status_id<>3 " + ORDER_BY + LIMIT_OFFSET;
    private static final String SELECT_ALL_BY_USER_ID = SELECT_ALL + "WHERE loan.user_id = ? AND loan.status_id<>3 " + ORDER_BY + LIMIT_OFFSET;
    private static final String SELECT_ALL_WITH_STATUS_RETURNED_BY_USER_ID = SELECT_ALL + "WHERE loan.user_id=? AND loan.status_id=3 " + LIMIT_OFFSET;
    private static final String DELETE_BY_ID = "DELETE FROM loan WHERE id=?";
    private static final String UPDATE = "UPDATE loan SET duration=?, status_id=?";
    private static final String UPDATE_STATUS = "UPDATE loan SET status_id=? WHERE id=?";
    private static final String INCREASE_BOOK_BORROW_AMOUNT = "UPDATE book SET no_of_borrow=(no_of_borrow+1) WHERE id=?;";
    private static final String DECREASE_BOOK_BORROW_AMOUNT = "UPDATE book SET no_of_borrow=(no_of_borrow-1) WHERE id=?;";

    private static final String GET_BOOKS_IDS_BY_USER_ID = "SELECT loan.book_id l_bookId " + "FROM loan  " + "         JOIN loan_status ls on ls.id = loan.status_id " + "         JOIN book b on b.id = loan.book_id " + "         JOIN users u on u.id = loan.user_id " + "WHERE loan.status_id != 3 " + "  AND user_id=? ";

    private static final String SELECT_USERS_WITH_OVERDUE = "SELECT loan.id l_id, loan.book_id bookId,  b.fine b_fine, loan.user_id userId FROM loan JOIN book b on b.id = loan.book_id WHERE loan.status_id = 1 AND (loan.start_time::DATE + loan.duration) < now();";

    private static final String SET_USER_FINE = "UPDATE users SET fine = fine + ? WHERE id = ?;";

    private static final String SUBTRACT_FINE_BY_USER_ID = "UPDATE users SET fine = fine - ? WHERE id = ?;";

    public JdbcLoanRepository(DbExecutor<Loan> dbExecutor, ConnectionPool connectionPool) {
        this.dbExecutor = dbExecutor;
        this.connectionPool = connectionPool;
    }

    @Override
    public long insert(Loan loan) {
        try (Connection connection = connectionPool.getConnection()) {
            Savepoint savepoint = connection.setSavepoint();
            try {
                long loanId = dbExecutor.insert(connection, INSERT_LOAN, List.of(loan.getUserId(), loan.getBookId(), loan.getDuration(), loan.getStatus().ordinal()));
                dbExecutor.queryById(connection, INCREASE_BOOK_BORROW_AMOUNT, loan.getBookId());
                connection.commit();
                return loanId;
            } catch (SQLException e) {
                log.error(e.getMessage());
                connection.rollback(savepoint);
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return -1;
    }

    @Override
    public Optional<Loan> getById(long id) {
        try (Connection connection = connectionPool.getConnection()) {
            return dbExecutor.select(connection, SELECT_BY_ID, id, RepositoryUtil::fillLoan);
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public boolean update(Loan loan) {
        try (Connection connection = connectionPool.getConnection()) {
            connection.setSavepoint();
            try {
                boolean b = dbExecutor.update(connection, UPDATE, List.of(loan.getDuration(), loan.getStatus().ordinal()));
                connection.commit();
                return b;
            } catch (Exception e) {
                connection.rollback();
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return false;
    }

    @Override
    public boolean delete(long id) {
        try (Connection connection = connectionPool.getConnection()) {
            connection.setSavepoint();
            try {
                boolean b = dbExecutor.queryById(connection, DELETE_BY_ID, id);
                connection.commit();
                return b;
            } catch (SQLException e) {
                log.error(e.getMessage());
                connection.rollback();
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return false;
    }

    @Override
    public Collection<Loan> getAll(FilterParams params, String orderBy, int limit, int offset) {
        String bookTitleSearch = prepareForLike(validateForLike(params.getFirstParam()));
        String statusSearch = prepareForLike(validateForLike(params.getSecondParam()));
        try (Connection connection = connectionPool.getConnection()) {
            return dbExecutor.selectAllWithLimit(connection, SELECT_ALL_WITH_LIMITS, bookTitleSearch, statusSearch, limit, offset, RepositoryUtil::fillLoans);
        } catch (SQLException e) {
            log.error(e.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    public Collection<Loan> getAllByUserId(long userId, int limit, int offset) {
        try (Connection connection = connectionPool.getConnection()) {
            return dbExecutor.selectAllById(connection, SELECT_ALL_BY_USER_ID, userId, limit, offset, RepositoryUtil::fillLoans);
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return Collections.emptyList();
    }

    @Override
    public boolean updateStatus(Loan loan, boolean fineSubtract) {
        try (Connection connection = connectionPool.getConnection()) {
            connection.setSavepoint();
            try {
                boolean update = dbExecutor.update(connection, UPDATE_STATUS, List.of(loan.getStatus().ordinal(), loan.getId()));
                if (update && loan.getStatus().equals(LoanStatus.RETURNED)) {
                    if (fineSubtract) {
                        dbExecutor.update(connection, SUBTRACT_FINE_BY_USER_ID, List.of(loan.getFine(), loan.getUserId()));
                    }
                    dbExecutor.queryById(connection, DECREASE_BOOK_BORROW_AMOUNT, loan.getBookId());
                }
                connection.commit();
                return update;
            } catch (SQLException e) {
                connection.rollback();
                log.error(e.getMessage());
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return false;
    }

    @Override
    public Collection<Long> getBooksIdsByUserId(long userId) {
        Collection<Long> booksIds = new HashSet<>();
        try (Connection connection = connectionPool.getConnection(); PreparedStatement statement = connection.prepareStatement(GET_BOOKS_IDS_BY_USER_ID)) {
            statement.setLong(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    booksIds.add(resultSet.getLong("l_bookId"));
                }
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return booksIds;
    }

    @Override
    public Collection<Loan> getLoanHistory(long userId, int limit, int offset) {
        try (Connection connection = connectionPool.getConnection()) {
            return dbExecutor.selectAllById(connection, SELECT_ALL_WITH_STATUS_RETURNED_BY_USER_ID, userId, limit, offset, RepositoryUtil::fillLoans);
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return Collections.emptyList();
    }

    @Override
    public void updateFine() {
        try (Connection connection = connectionPool.getConnection()) {
            connection.setSavepoint();
            try (PreparedStatement statement = connection.prepareStatement(SELECT_USERS_WITH_OVERDUE);
                 PreparedStatement setLoanStatus = connection.prepareStatement(UPDATE_STATUS);
                 PreparedStatement setUserFineStatement = connection.prepareStatement(SET_USER_FINE);
                 ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    long loanId = resultSet.getLong("l_id");
                    setLoanStatus.setInt(1, 2);
                    setLoanStatus.setLong(2, loanId);
                    log.info("{}", setLoanStatus);
                    setLoanStatus.addBatch();
                    int fine = resultSet.getInt("b_fine");
                    setUserFineStatement.setInt(1, fine);
                    long userId = resultSet.getLong("userId");
                    setUserFineStatement.setLong(2, userId);
                    log.info("{}", setUserFineStatement);
                    setUserFineStatement.addBatch();
                }
                int[] ints = setLoanStatus.executeBatch();
                int[] ints1 = setUserFineStatement.executeBatch();
                log.info("execute queries {} {}", ints.length, ints1.length);
                connection.commit();
            } catch (Exception e) {
                log.error(e.getMessage());
                connection.rollback();
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }
}

