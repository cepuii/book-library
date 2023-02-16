package ua.od.cepuii.library.service;

import ua.od.cepuii.library.repository.RepositoryFactory;

/**
 * A factory class that creates instances of service classes based on the given repository factory.
 * The created service classes have access to the corresponding repository instances.
 *
 * @author Sergei Chernousov
 * @version 1.0
 */
public class ServiceFactory {
    private final UserService userService;
    private final BookService bookService;
    private final LoanService loanService;

    /**
     * Constructs a new ServiceFactory instance with the given RepositoryFactory instance.
     * Initializes UserService, BookService, and LoanService with corresponding Repository instances.
     *
     * @param repositoryFactory the RepositoryFactory instance that provides repository objects for the service classes
     */
    public ServiceFactory(RepositoryFactory repositoryFactory) {
        this.userService = new UserService(repositoryFactory.getUserRepository());
        this.bookService = new BookService(repositoryFactory.getBookRepository());
        this.loanService = new LoanService(repositoryFactory.getLoanRepository(), repositoryFactory.getBookRepository());
    }

    /**
     * Returns the UserService instance.
     *
     * @return the UserService instance
     */
    public UserService getUserService() {
        return userService;
    }

    /**
     * Returns the BookService instance.
     *
     * @return the BookService instance
     */
    public BookService getBookService() {
        return bookService;
    }

    /**
     * Returns the LoanService instance.
     *
     * @return the LoanService instance
     */
    public LoanService getLoanService() {
        return loanService;
    }
}
