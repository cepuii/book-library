package ua.od.cepuii.library.command.unregister;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.od.cepuii.library.command.ActionCommand;
import ua.od.cepuii.library.constants.AttributesName;
import ua.od.cepuii.library.constants.Constants;
import ua.od.cepuii.library.constants.Path;
import ua.od.cepuii.library.context.AppContext;
import ua.od.cepuii.library.dto.BookTO;
import ua.od.cepuii.library.dto.FilterParams;
import ua.od.cepuii.library.dto.Page;
import ua.od.cepuii.library.dto.RequestParser;
import ua.od.cepuii.library.service.BookService;
import ua.od.cepuii.library.service.LoanService;

import java.util.Collection;

import static ua.od.cepuii.library.constants.AttributesName.*;

public class ShowBooks implements ActionCommand {
    private static final Logger log = LoggerFactory.getLogger(ShowBooks.class);

    private final BookService bookService = AppContext.getInstance().getBookService();
    private final LoanService loanService = AppContext.getInstance().getLoanService();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {

        //TODO change filter logic
        FilterParams filterParam = RequestParser.getFilterParams(request, "titleSearch", "authorSearch");
        Page page = RequestParser.getPage(request, bookService, filterParam);

        Collection<BookTO> books = bookService.getAll(page, filterParam);
        request.setAttribute(AttributesName.BOOKS, books);
        request.setAttribute(AttributesName.DATA, Constants.RECEIVE);

        HttpSession session = request.getSession();
        session.setAttribute(FILTER, filterParam);
        request.setAttribute(PAGE, page);
        long userId = RequestParser.getLong(request, USER_ID);

        if (userId != 0) {
            Collection<Long> booksIdsByUserId = loanService.getBooksIdsByUserId(userId);
            session.setAttribute(LOAN_ITEMS, booksIdsByUserId);
        }

        RequestParser.setMapFromSessionToRequest(request, REPORTS);
        log.info("page attributes {}", page);
        return Path.MAIN_PAGE;
    }
}
