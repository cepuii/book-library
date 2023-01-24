package ua.od.cepuii.library.command.unregister;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.od.cepuii.library.command.ActionCommand;
import ua.od.cepuii.library.context.AppContext;
import ua.od.cepuii.library.dto.BookTO;
import ua.od.cepuii.library.dto.FilterParams;
import ua.od.cepuii.library.dto.Page;
import ua.od.cepuii.library.dto.RequestParser;
import ua.od.cepuii.library.resource.ConfigurationManager;
import ua.od.cepuii.library.service.BookService;
import ua.od.cepuii.library.service.LoanService;

import java.util.Collection;

public class ShowBooks implements ActionCommand {
    private static final Logger log = LoggerFactory.getLogger(ShowBooks.class);

    private final BookService bookService = AppContext.getInstance().getBookService();
    private final LoanService loanService = AppContext.getInstance().getLoanService();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        FilterParams filterParam = RequestParser.getFilterParams(request, "titleSearch", "authorSearch");
        Page page = RequestParser.getPageFromSession(request, bookService, filterParam);
        Collection<BookTO> books = bookService.getAll(page, filterParam);
        request.setAttribute("books", books);
        request.setAttribute("data", "receive");
        HttpSession session = request.getSession();
        session.setAttribute("filter", filterParam);
        session.setAttribute("page", page);
        long userId = RequestParser.getLong(request, "userId");
        if (userId != 0 && session.getAttribute("loanItems") == null) {
            Collection<Long> booksIdsByUserId = loanService.getBooksIdsByUserId(userId);
            session.setAttribute("loanItems", booksIdsByUserId);
        }
        RequestParser.setFromSessionToRequest(request, "wrongAction");
        log.info("page attributes {}", page);
        return ConfigurationManager.getProperty("path.page.main");
    }
}
