package ua.od.cepuii.library.command.implementation;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.od.cepuii.library.command.ActionCommand;
import ua.od.cepuii.library.dto.BookTO;
import ua.od.cepuii.library.dto.RequestParser;
import ua.od.cepuii.library.exception.RequestParserException;
import ua.od.cepuii.library.resource.ConfigurationManager;
import ua.od.cepuii.library.service.BookService;

import java.sql.SQLException;

public class EditBook implements ActionCommand {
    private static final Logger log = LoggerFactory.getLogger(EditBook.class);
    BookService bookService = new BookService();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        try {
            long bookId = RequestParser.getLong(request, "bookId");
            if (bookId != 0) {
                BookTO bookTO = bookService.getById(bookId);
                request.setAttribute("book", bookTO);
            }
            return ConfigurationManager.getProperty("path.page.edit.book");
        } catch (SQLException | RequestParserException e) {
            log.error(e.getMessage());
            return ConfigurationManager.getProperty("path.page.main");
        }
    }
}
