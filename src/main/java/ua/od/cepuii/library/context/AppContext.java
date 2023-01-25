package ua.od.cepuii.library.context;

import lombok.Getter;
import ua.od.cepuii.library.db.ConnectionPool;
import ua.od.cepuii.library.db.HikariConnectionPool;
import ua.od.cepuii.library.repository.RepositoryFactory;
import ua.od.cepuii.library.repository.jdbc.JdbcRepositoryFactory;
import ua.od.cepuii.library.service.BookService;
import ua.od.cepuii.library.service.LoanService;
import ua.od.cepuii.library.service.UserService;

@Getter
public class AppContext {
    private static final AppContext appContext = new AppContext();
    private final BookService bookService;
    private final UserService userService;
    private final LoanService loanService;

    private AppContext() {
        RepositoryFactory factory = new JdbcRepositoryFactory();
        this.bookService = new BookService(factory.getBookRepository());
        this.userService = new UserService(factory.getUserRepository());
        this.loanService = new LoanService(factory.getLoanRepository(), factory.getBookRepository());
        ConnectionPool connectionPool = new HikariConnectionPool();
        connectionPool.getConnection();
    }
    public static AppContext getInstance() {
        return appContext;
    }

}
