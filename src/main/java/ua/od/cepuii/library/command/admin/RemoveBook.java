package ua.od.cepuii.library.command.admin;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.od.cepuii.library.command.ActionCommand;
import ua.od.cepuii.library.constants.Path;
import ua.od.cepuii.library.context.AppContext;
import ua.od.cepuii.library.dto.Report;
import ua.od.cepuii.library.dto.RequestParser;
import ua.od.cepuii.library.service.BookService;

import static ua.od.cepuii.library.constants.AttributesName.*;

/**
 * This class is responsible for removing book by id from request.
 * It uses the {@link BookService} to call the appropriate method.
 *
 * @author Sergei Chernousov
 * @version 1.0
 */
public class RemoveBook implements ActionCommand {
    private static final Logger log = LoggerFactory.getLogger(RemoveBook.class);
    BookService bookService = AppContext.getInstance().getBookService();

    /**
     * This method retrieves the book ID from the request, deletes the book using the {@link BookService#delete(long)} method,
     * stores the result of the operation in the session, and returns the path to the show books page.
     *
     * @param request  HttpServletRequest request object
     * @param response HttpServletResponse response object
     * @return The path to the show books page
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        long bookId = RequestParser.getLong(request, BOOK_ID);
        Report report = bookService.delete(bookId);

        request.getSession().setAttribute(REPORTS, report.getReports());
        log.info("delete book {}  by user {}, result {}", bookId, request.getSession().getAttribute(USER), report);

        return Path.SHOW_BOOKS;
    }
}
