package ua.od.cepuii.library.command.implementation;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.od.cepuii.library.command.ActionCommand;
import ua.od.cepuii.library.dto.RequestParser;
import ua.od.cepuii.library.entity.Book;
import ua.od.cepuii.library.resource.ConfigurationManager;
import ua.od.cepuii.library.service.BookService;

import java.sql.SQLException;

public class SaveBook implements ActionCommand {
    private static final Logger log = LoggerFactory.getLogger(SaveBook.class);
    BookService bookService = new BookService();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        try {
            Book book = RequestParser.getBook(request);
            boolean update = bookService.update(book);
            if (update) {
                return ConfigurationManager.getProperty("path.page.main") + ConfigurationManager.getProperty("path.success");
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        long bookId = RequestParser.getLong(request, "bookId");
        return ConfigurationManager.getProperty("path.controller.edit_book") + bookId;
    }
}
