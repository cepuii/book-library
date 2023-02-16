package ua.od.cepuii.library.repository.jdbc;

import ua.od.cepuii.library.db.ConnectionPool;
import ua.od.cepuii.library.repository.BookRepository;
import ua.od.cepuii.library.repository.LoanRepository;
import ua.od.cepuii.library.repository.RepositoryFactory;
import ua.od.cepuii.library.repository.UserRepository;
import ua.od.cepuii.library.repository.jdbc.executor.QueryExecutorImpl;

/**
 * A {@code JdbcRepositoryFactory} class provides implementation for {@link RepositoryFactory} interface.
 * It creates repository objects for {@link ua.od.cepuii.library.repository.UserRepository},
 * {@link ua.od.cepuii.library.repository.BookRepository}, and {@link ua.od.cepuii.library.repository.LoanRepository}.
 *
 * @author Sergei Chernousov
 * @version 1.0
 */
public class JdbcRepositoryFactory implements RepositoryFactory {

    private final ConnectionPool connectionPool;

    /**
     * Constructs a new instance of {@code JdbcRepositoryFactory} with the given {@code ConnectionPool} object.
     *
     * @param connectionPool a {@code ConnectionPool} object that represents a pool of database connections.
     */
    public JdbcRepositoryFactory(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    /**
     * Returns an instance of {@link ua.od.cepuii.library.repository.jdbc.JdbcUserRepository}.
     *
     * @return an instance of {@code JdbcUserRepository}.
     */
    @Override
    public UserRepository getUserRepository() {
        return new JdbcUserRepository(new QueryExecutorImpl<>(), connectionPool);
    }

    /**
     * Returns an instance of {@link ua.od.cepuii.library.repository.jdbc.JdbcBookRepository}.
     *
     * @return an instance of {@code JdbcBookRepository}.
     */
    @Override
    public BookRepository getBookRepository() {
        return new JdbcBookRepository(new QueryExecutorImpl<>(), new QueryExecutorImpl<>(), connectionPool);
    }

    /**
     * Returns an instance of {@link ua.od.cepuii.library.repository.jdbc.JdbcLoanRepository}.
     *
     * @return an instance of {@code JdbcLoanRepository}.
     */
    @Override
    public LoanRepository getLoanRepository() {
        return new JdbcLoanRepository(new QueryExecutorImpl<>(), connectionPool);
    }
}
