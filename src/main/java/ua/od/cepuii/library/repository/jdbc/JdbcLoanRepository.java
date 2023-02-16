package ua.od.cepuii.library.repository.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.od.cepuii.library.db.ConnectionPool;
import ua.od.cepuii.library.dto.FilterParams;
import ua.od.cepuii.library.entity.Loan;
import ua.od.cepuii.library.entity.enums.LoanStatus;
import ua.od.cepuii.library.repository.AbstractRepository;
import ua.od.cepuii.library.repository.LoanRepository;
import ua.od.cepuii.library.repository.jdbc.executor.QueryExecutor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class JdbcLoanRepository extends AbstractRepository<Loan> implements LoanRepository {
    private static final Logger log = LoggerFactory.getLogger(JdbcLoanRepository.class);
    private final QueryExecutor<Loan> executor;
    private static final String INSERT_LOAN = "INSERT INTO loan (user_id, book_id, duration, status_id) VALUES (?,?,?,?)";
    private static final String SELECT_BY_ID = "SELECT id, user_id,book_id, start_time, duration, status_id, fine FROM loan WHERE id=?  ";
    private static final String SELECT_ALL = "SELECT loan.id l_id, loan.book_id l_bookId, loan.user_id l_userId, u.email u_email, " +
            "loan.start_time l_start_time, loan.duration l_duration, b.title b_title, b.date_publication b_date, b.fine b_fine, ls.status l_status " +
            "FROM loan JOIN loan_status ls on ls.id = loan.status_id " +
            "JOIN book b on b.id = loan.book_id " +
            "JOIN users u on u.id = loan.user_id ";
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

    private static final String COUNT_SELECT_ALL_FILTER = "SELECT count(loan.id) " +
            "FROM loan " +
            "         JOIN users u on u.id = loan.user_id " +
            "         JOIN book b on b.id = loan.book_id " +
            "WHERE b.title LIKE ? " +
            "  AND u.email LIKE ? " +
            "  AND loan.status_id <> 3";
    private static final String COUNT_SELECT_ALL_FILTER_BY_ID = "SELECT count(loan.id) " +
            "FROM loan " +
            "         JOIN users u on u.id = loan.user_id " +
            "         JOIN book b on b.id = loan.book_id " +
            "WHERE b.title LIKE ? " +
            "  AND u.email LIKE ? " +
            "  AND u.id = ? " +
            "  AND loan.status_id <> 3";
    private static final String SUBTRACT_FINE_BY_USER_ID = "UPDATE users SET fine = fine - ? WHERE id = ?;";

    public JdbcLoanRepository(QueryExecutor<Loan> loanExecutor, ConnectionPool connectionPool) {
        super(connectionPool);
        this.executor = loanExecutor;
    }

    /**
     * @param connection
     * @param loan
     * @return
     * @throws SQLException
     */
    @Override
    protected long insertAndGetId(Connection connection, Loan loan) {
        try {
            long loanId = executor.insert(connection, INSERT_LOAN, List.of(loan.getUserId(), loan.getBookId(), loan.getDuration(), loan.getStatus().ordinal()));
            executor.isExistResultById(connection, INCREASE_BOOK_BORROW_AMOUNT, loan.getBookId());
            return loanId;
        } catch (SQLException e) {
            log.error(e.getMessage());
            return -1;
        }
    }

    /**
     * @param connection
     * @param id
     * @return
     * @throws SQLException
     */
    @Override
    protected Optional<Loan> selectById(Connection connection, long id) throws SQLException {
        return executor.selectByParams(connection, SELECT_BY_ID, List.of(id), RepositoryUtil::fillLoan);
    }

    /**
     * @param connection
     * @param loan
     * @return
     */
    @Override
    protected boolean update(Connection connection, Loan loan) {
        try {
            return executor.update(connection, UPDATE, List.of(loan.getDuration(), loan.getStatus().ordinal()));
        } catch (SQLException e) {
            log.error(e.getMessage());
            return false;
        }
    }

    /**
     * @param connection
     * @param id
     * @return
     */
    @Override
    protected boolean delete(Connection connection, long id) {
        try {
            return executor.isExistResultById(connection, DELETE_BY_ID, id);
        } catch (SQLException e) {
            log.error(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteAndDecreaseBookBorrow(long loanId, long bookId) {
        try (Connection connection = connectionPool.getConnection()) {
            connection.setSavepoint();
            try {
                Optional<Loan> loan = executor.selectByParams(connection, SELECT_BY_ID, List.of(loanId), RepositoryUtil::fillLoan);
                if (loan.isEmpty() || loan.get().getStatus() != LoanStatus.RAW) {
                    throw new SQLException("can`t delete loan: {}", loan.toString());
                }
                boolean deleteLoan = executor.isExistResultById(connection, DELETE_BY_ID, loanId);
                if (deleteLoan) {
                    executor.isExistResultById(connection, DECREASE_BOOK_BORROW_AMOUNT, bookId);
                }
                connection.commit();
                return deleteLoan;

            } catch (SQLException e) {
                log.error(e.getMessage());
                connection.rollback();
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return false;
    }

    /**
     * @param connection
     * @param params
     * @param orderBy
     * @return
     * @throws SQLException
     */
    @Override
    protected Collection<Loan> selectAll(Connection connection, List<Object> params, String orderBy) throws SQLException {
        return executor.selectAll(connection, SELECT_ALL_WITH_LIMITS, params, RepositoryUtil::fillLoans);
    }


    @Override
    public Collection<Loan> getAllByUserId(long userId, int limit, int offset) {
        try (Connection connection = connectionPool.getConnection()) {
            return executor.selectAll(connection, SELECT_ALL_BY_USER_ID, List.of(userId, limit, offset), RepositoryUtil::fillLoans);
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
                boolean update = executor.update(connection, UPDATE_STATUS, List.of(loan.getStatus().ordinal(), loan.getId()));
                if (update && loan.getStatus().equals(LoanStatus.RETURNED)) {
                    if (fineSubtract) {
                        executor.update(connection, SUBTRACT_FINE_BY_USER_ID, List.of(loan.getFine(), loan.getUserId()));
                    }
                    executor.isExistResultById(connection, DECREASE_BOOK_BORROW_AMOUNT, loan.getBookId());
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
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_BOOKS_IDS_BY_USER_ID)) {

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
            return executor.selectAll(connection, SELECT_ALL_WITH_STATUS_RETURNED_BY_USER_ID,
                    List.of(userId, limit, offset), RepositoryUtil::fillLoans);
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

    @Override
    public int getCount(FilterParams filterParam) {
        try (Connection connection = connectionPool.getConnection()) {
            if (filterParam.getId() == 0) {
                return executor.selectCount(connection, COUNT_SELECT_ALL_FILTER,
                        List.of(filterParam.getFirstParamForQuery(), filterParam.getSecondParamForQuery()));
            } else {
                return executor.selectCount(connection, COUNT_SELECT_ALL_FILTER_BY_ID,
                        List.of(filterParam.getFirstParamForQuery(), filterParam.getSecondParamForQuery(), filterParam.getId()));
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return 0;
    }

    /**
     * @param connection
     * @param filterParam
     * @return
     * @throws SQLException
     */
    @Override
    protected int getCount(Connection connection, FilterParams filterParam) throws SQLException {
        if (filterParam.getId() == 0) {
            return executor.selectCount(connection, COUNT_SELECT_ALL_FILTER,
                    List.of(filterParam.getFirstParamForQuery(), filterParam.getSecondParamForQuery()));
        } else {
            return executor.selectCount(connection, COUNT_SELECT_ALL_FILTER_BY_ID,
                    List.of(filterParam.getFirstParamForQuery(), filterParam.getSecondParamForQuery(), filterParam.getId()));
        }
    }
}

