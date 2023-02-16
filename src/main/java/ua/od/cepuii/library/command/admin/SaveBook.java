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
import ua.od.cepuii.library.entity.Book;
import ua.od.cepuii.library.service.BookService;

import static ua.od.cepuii.library.constants.AttributesName.BOOK;
import static ua.od.cepuii.library.constants.AttributesName.REPORTS;

/**
 * This class is responsible for creating or updating books in the system.
 * It uses the {@link BookService} to call the appropriate method to create or update a book.
 *
 * @author Sergei Chernousov
 * @version 1.0
 */
public class SaveBook implements ActionCommand {
    private static final Logger log = LoggerFactory.getLogger(SaveBook.class);
    BookService bookService = AppContext.getInstance().getBookService();

    /**
     * It retrieves the book details from the request and calls the appropriate method
     * in {@link BookService} to create or update a book.
     * If the book creation or update was successful, the result is stored in the session
     * under the attribute {@code REPORTS}.
     *
     * @param request  HttpServletRequest request object
     * @param response HttpServletResponse response object
     * @return The path to the show books page if the book creation or update was successful,
     * otherwise, the path to the edit book page
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        Book book = RequestParser.getBook(request);
        log.info("create or update book: {}", book);
        Report report = bookService.createOrUpdate(book);

        if (report.hasErrors()) {
            request.setAttribute(BOOK, book);
            request.setAttribute(REPORTS, report.getReports());
            return Path.EDIT_BOOK_PAGE_FORWARD;
        }
        request.getSession().setAttribute(REPORTS, report.getReports());
        return Path.SHOW_BOOKS;
    }
}
