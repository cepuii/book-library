package ua.od.cepuii.library.repository;

public interface RepositoryFactory {
    AuthorRepository getAuthorRepository();

    UserRepository getUserRepository();

    BookRepository getBookRepository();

    LoanRepository getLoanRepository();
}
