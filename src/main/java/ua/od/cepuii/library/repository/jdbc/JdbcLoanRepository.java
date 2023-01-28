package ua.od.cepuii.library.repository.jdbc;

import ua.od.cepuii.library.db.ConnectionPool;
import ua.od.cepuii.library.entity.Loan;
import ua.od.cepuii.library.repository.LoanRepository;
import ua.od.cepuii.library.repository.executor.DbExecutor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class JdbcLoanRepository implements LoanRepository {
    private final DbExecutor<Loan> dbExecutor;
    private final ConnectionPool connectionPool;
    private static final String INSERT_LOAN = "INSERT INTO loan (user_id, book_id, duration, status_id) VALUES (?,?,?,?)";
    private static final String SELECT_BY_ID = "SELECT id, user_id,book_id, start_time, duration, status_id, fine FROM loan WHERE id=?";
    private static final String SELECT_ALL = "SELECT id, user_id,book_id, start_time, duration, status_id, fine FROM loan";
    private static final String DELETE_BY_ID = "DELETE FROM loan WHERE id=?";
    private static final String UPDATE = "UPDATE loan SET duration=?, status_id=?";

    public JdbcLoanRepository(DbExecutor<Loan> dbExecutor, ConnectionPool connectionPool) {
        this.dbExecutor = dbExecutor;
        this.connectionPool = connectionPool;
    }

    @Override
    public long insert(Loan loan) throws SQLException {
        try (Connection connection = connectionPool.getConnection()) {
            return dbExecutor.executeInsert(connection, INSERT_LOAN, List.of(loan.getAuthorId(), loan.getBookId(),
                    loan.getDuration(), loan.getStatus().ordinal()));
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
            return dbExecutor.executeDelete(connection, DELETE_BY_ID, id);
        }
    }

    @Override
    public Collection<Loan> getAll() throws SQLException {
        try (Connection connection = connectionPool.getConnection()) {
            return dbExecutor.executeSelectAll(connection, SELECT_ALL, RepositoryUtil::fillLoans);
        }
    }
}
