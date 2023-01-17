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

public class SaveBook implements ActionCommand {
    private static final Logger log = LoggerFactory.getLogger(SaveBook.class);
    BookService bookService = new BookService();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        Book book = RequestParser.getBook(request);
        boolean update = bookService.createOrUpdate(book);
        if (update) {
            return ConfigurationManager.getProperty("path.page.main") + "?" + ConfigurationManager.getProperty("path.success");
        }

        long bookId = RequestParser.getLong(request, "bookId");
        return ConfigurationManager.getProperty("path.controller.edit_book") + bookId;
    }
}
