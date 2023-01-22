package ua.od.cepuii.library.command.admin;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.od.cepuii.library.command.ActionCommand;
import ua.od.cepuii.library.dto.RequestParser;
import ua.od.cepuii.library.entity.Author;
import ua.od.cepuii.library.exception.RequestParserException;
import ua.od.cepuii.library.resource.ConfigurationManager;
import ua.od.cepuii.library.service.BookService;

import java.sql.SQLException;

public class AddAuthor implements ActionCommand {
    private static final Logger log = LoggerFactory.getLogger(AddAuthor.class);
    BookService bookService = new BookService();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        try {
            long bookId = RequestParser.getLong(request, "bookId");
            Author author = RequestParser.getNewAuthor(request);
            bookService.addAuthor(bookId, author);
            return ConfigurationManager.getProperty("path.controller.edit_book") + bookId;
        } catch (SQLException | RequestParserException e) {
            log.error(e.getMessage());
            return ConfigurationManager.getProperty("path.page.main");
        }
    }
}