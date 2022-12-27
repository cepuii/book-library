package ua.od.cepuii.library.repository.jdbc;

import ua.od.cepuii.library.db.HikariConnectionPool;
import ua.od.cepuii.library.repository.*;
import ua.od.cepuii.library.repository.executor.DbExecutorImpl;

public class JdbcRepositoryFactory implements RepositoryFactory {
    @Override
    public AuthorRepository getAuthorRepository() {
        return new JdbcAuthorRepository(new DbExecutorImpl<>(), new HikariConnectionPool());
    }

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
