package ua.od.cepuii.library.command.admin;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.od.cepuii.library.command.ActionCommand;
import ua.od.cepuii.library.constants.Path;
import ua.od.cepuii.library.context.AppContext;
import ua.od.cepuii.library.dto.BookTO;
import ua.od.cepuii.library.dto.RequestParser;
import ua.od.cepuii.library.service.BookService;

import java.util.Map;

import static ua.od.cepuii.library.constants.AttributesName.*;
import static ua.od.cepuii.library.constants.Constants.WRONG_ACTION;

public class EditBook implements ActionCommand {
    private static final Logger log = LoggerFactory.getLogger(EditBook.class);
    BookService bookService = AppContext.getInstance().getBookService();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        long bookId = RequestParser.getLong(request, BOOK_ID);

        if (bookId != 0) {
            log.info("edit book, bookId: {}", bookId);
            BookTO bookTO = bookService.getById(bookId);
            request.setAttribute(BOOK, bookTO);
            return Path.EDIT_BOOK_PAGE;
        }

        request.setAttribute(REPORTS, Map.of(WRONG_ACTION, "message.wrongAction.edit"));
        return Path.SHOW_BOOKS;
    }
}
