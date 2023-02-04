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

public class RemoveBook implements ActionCommand {
    private static final Logger log = LoggerFactory.getLogger(RemoveBook.class);
    BookService bookService = AppContext.getInstance().getBookService();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        long bookId = RequestParser.getLong(request, BOOK_ID);
        Report report = bookService.delete(bookId);

        request.getSession().setAttribute(REPORTS, report);
        log.info("delete book {}  by user {}, result {}", bookId, request.getSession().getAttribute(USER), report);

        return Path.SHOW_BOOKS;
    }
}
