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

/**
 * This class is responsible for showing edit book page and add necessary information in request.
 * It uses the {@link BookService} to call the appropriate method to get book information.
 *
 * @author Sergei Chernousov
 * @version 1.0
 */
public class EditBook implements ActionCommand {
    private static final Logger log = LoggerFactory.getLogger(EditBook.class);
    BookService bookService = AppContext.getInstance().getBookService();


    /**
     * This method retrieves the bookId from the request and retrieves the corresponding book information
     * from the database using the {@link  BookService}. If the bookId is not equal to 0, it sets the
     * book information in the request attribute as an instance of {@link  BookTO} and returns the path to
     * the edit book page. Otherwise, it sets the report in the request attribute with the key
     * {@code WRONG_ACTION} and a message indicating a wrong action and returns the path to the show books page.
     *
     * @param request  HttpServletRequest request object
     * @param response HttpServletResponse response object
     * @return The path to the edit book page if the bookId is not equal to 0, the path to the show books page otherwise
     */
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
