package ua.od.cepuii.library.repository.jdbc;

import ua.od.cepuii.library.db.HikariConnectionPool;
import ua.od.cepuii.library.repository.BookRepository;
import ua.od.cepuii.library.repository.LoanRepository;
import ua.od.cepuii.library.repository.RepositoryFactory;
import ua.od.cepuii.library.repository.UserRepository;
import ua.od.cepuii.library.repository.executor.DbExecutorImpl;

public class JdbcRepositoryFactory implements RepositoryFactory {

    @Override
    public UserRepository getUserRepository() {
        return new JdbcUserRepository(new DbExecutorImpl<>(), new HikariConnectionPool());
    }

    @Override
    public BookRepository getBookRepository() {
        return new JdbcBookRepository(new DbExecutorImpl<>(), new HikariConnectionPool());
    }

    @Override
    public LoanRepository getLoanRepository() {
        return new JdbcLoanRepository(new DbExecutorImpl<>(), new HikariConnectionPool());
    }
}
