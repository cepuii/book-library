package ua.od.cepuii.library.command.admin;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.od.cepuii.library.command.ActionCommand;
import ua.od.cepuii.library.context.AppContext;
import ua.od.cepuii.library.dto.BookTO;
import ua.od.cepuii.library.dto.RequestParser;
import ua.od.cepuii.library.exception.RequestParserException;
import ua.od.cepuii.library.resource.ConfigurationManager;
import ua.od.cepuii.library.resource.MessageManager;
import ua.od.cepuii.library.service.BookService;

public class EditBook implements ActionCommand {
    private static final Logger log = LoggerFactory.getLogger(EditBook.class);
    BookService bookService = AppContext.getInstance().getBookService();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        try {
            long bookId = RequestParser.getLong(request, "bookId");
            if (bookId != 0) {
                BookTO bookTO = bookService.getById(bookId);
                request.setAttribute("book", bookTO);
            }
            return ConfigurationManager.getProperty("path.page.edit.book");
        } catch (RequestParserException e) {
            log.error(e.getMessage());
            request.setAttribute("wrongAction", MessageManager.getProperty("message.wrongAction.edit"));
            return ConfigurationManager.getProperty("path.controller.books");
        }
    }
}
