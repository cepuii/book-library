package ua.od.cepuii.library.repository;

/**
 * The interface Repository factory.
 * It defines a set of methods for creating instances of repositories.
 * These repositories manage entities stored in the database and can perform CRUD operations.
 *
 * @author Sergei Chernousov
 * @version 1.0
 */
public interface RepositoryFactory {
    UserRepository getUserRepository();

    BookRepository getBookRepository();

    LoanRepository getLoanRepository();
}
