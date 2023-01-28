package ua.od.cepuii.library.repository;

public interface RepositoryFactory {
    UserRepository getUserRepository();

    BookRepository getBookRepository();

    LoanRepository getLoanRepository();
}
