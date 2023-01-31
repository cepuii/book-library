package ua.od.cepuii.library.service;

import ua.od.cepuii.library.repository.RepositoryFactory;

public class ServiceFactory {
    private final UserService userService;
    private final BookService bookService;
    private final LoanService loanService;

    public ServiceFactory(RepositoryFactory repositoryFactory) {
        this.userService = new UserService(repositoryFactory.getUserRepository());
        this.bookService = new BookService(repositoryFactory.getBookRepository());
        this.loanService = new LoanService(repositoryFactory.getLoanRepository(), repositoryFactory.getBookRepository());
    }

    public UserService getUserService() {
        return userService;
    }

    public BookService getBookService() {
        return bookService;
    }

    public LoanService getLoanService() {
        return loanService;
    }
}
