package ua.od.cepuii.library.repository.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.od.cepuii.library.db.ConnectionPool;
import ua.od.cepuii.library.dto.FilterAndSortParams;
import ua.od.cepuii.library.entity.Loan;
import ua.od.cepuii.library.repository.LoanRepository;
import ua.od.cepuii.library.repository.executor.DbExecutor;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class JdbcLoanRepository implements LoanRepository {
    private static final Logger log = LoggerFactory.getLogger(JdbcLoanRepository.class);
    private final DbExecutor<Loan> dbExecutor;
    private final ConnectionPool connectionPool;
    private static final String INSERT_LOAN = "INSERT INTO loan (user_id, book_id, duration, status_id) VALUES (?,?,?,?)";
    private static final String SELECT_BY_ID = "SELECT id, user_id,book_id, start_time, duration, status_id, fine FROM loan WHERE id=?";
    private static final String SELECT_ALL = "SELECT id, user_id,book_id, start_time, duration, status_id, fine FROM loan";
    private static final String SELECT_ALL_BY_USER_ID = "SELECT loan.id l_id, loan.book_id l_bookId, loan.user_id l_userId," +
            "loan.start_time l_start_time, " +
            "loan.duration l_duration, b.title b_title, b.date_publication b_date, ls.status l_status " +
            "FROM loan " +
            "JOIN loan_status ls on ls.id = loan.status_id " +
            "JOIN book b on b.id = loan.book_id " +
            "WHERE loan.user_id = ? " +
            "ORDER BY status, b.title, duration " +
            "LIMIT ? OFFSET ?;";
    private static final String DELETE_BY_ID = "DELETE FROM loan WHERE id=?";
    private static final String UPDATE = "UPDATE loan SET duration=?, status_id=?";
    private static final String INCREASE_BOOK_BORROW = "UPDATE book SET no_of_borrow=(no_of_borrow+1) WHERE id=?;";
    private static final String DECREASE_BOOK_BORROW = "UPDATE book SET no_of_borrow=(no_of_borrow-1) WHERE id=?;";

    public JdbcLoanRepository(DbExecutor<Loan> dbExecutor, ConnectionPool connectionPool) {
        this.dbExecutor = dbExecutor;
        this.connectionPool = connectionPool;
    }

    @Override
    public long insert(Loan loan) throws SQLException {
        try (Connection connection = connectionPool.getConnection()) {
            Savepoint savepoint = connection.setSavepoint("InsertSavePoint");
            long loanId = 0;
            try {
                loanId = dbExecutor.executeInsert(connection, INSERT_LOAN, List.of(loan.getUserId(), loan.getBookId(),
                        loan.getDuration(), loan.getStatus().ordinal()));
                dbExecutor.executeById(connection, INCREASE_BOOK_BORROW, loan.getBookId());
                connection.commit();
            } catch (Exception e) {
                log.error(e.getMessage());
                connection.rollback(savepoint);
            }
            return loanId;
        }
    }

    @Override
    public Optional<Loan> getById(long id) throws SQLException {
        try (Connection connection = connectionPool.getConnection()) {
            return dbExecutor.executeSelect(connection, SELECT_BY_ID, id, RepositoryUtil::fillLoan);
        }
    }

    @Override
    public boolean update(Loan loan) throws SQLException {
        try (Connection connection = connectionPool.getConnection()) {
            return dbExecutor.executeUpdate(connection, UPDATE, List.of(loan.getDuration(), loan.getStatus().ordinal()));
        }
    }

    @Override
    public boolean delete(long id) throws SQLException {
        try (Connection connection = connectionPool.getConnection()) {
            return dbExecutor.executeById(connection, DELETE_BY_ID, id);
        }
    }

    @Override
    public Collection<Loan> getAll(FilterAndSortParams params, String orderBy, int limit, int offset) {
        try (Connection connection = connectionPool.getConnection()) {
            return dbExecutor.executeSelectAll(connection, SELECT_ALL, orderBy, limit, offset, RepositoryUtil::fillLoans);
        } catch (SQLException e) {
            log.error(e.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    public Collection<Loan> getAllByUserId(long userId, int limit, int offset) throws SQLException {
        try (Connection connection = connectionPool.getConnection()) {
            return dbExecutor.executeSelectAll(connection, SELECT_ALL_BY_USER_ID, String.valueOf(userId), limit, offset, RepositoryUtil::fillLoans);
        }
    }
}
