package ua.od.cepuii.library.repository.jdbc;

import ua.od.cepuii.library.db.ConnectionPool;
import ua.od.cepuii.library.repository.BookRepository;
import ua.od.cepuii.library.repository.LoanRepository;
import ua.od.cepuii.library.repository.RepositoryFactory;
import ua.od.cepuii.library.repository.UserRepository;

public class JdbcRepositoryFactory implements RepositoryFactory {

    private final ConnectionPool connectionPool;

    public JdbcRepositoryFactory(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public UserRepository getUserRepository() {
        return new JdbcUserRepository(connectionPool);
    }

    @Override
    public BookRepository getBookRepository() {
        return new JdbcBookRepository(connectionPool);
    }

    @Override
    public LoanRepository getLoanRepository() {
        return new JdbcLoanRepository(connectionPool);
    }
}
