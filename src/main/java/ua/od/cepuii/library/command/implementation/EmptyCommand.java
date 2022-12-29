package ua.od.cepuii.library.command.implementation;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ua.od.cepuii.library.command.ActionCommand;
import ua.od.cepuii.library.entity.Book;
import ua.od.cepuii.library.resource.ConfigurationManager;
import ua.od.cepuii.library.service.BookService;
import ua.od.cepuii.library.util.PaginationUtil;

import java.sql.SQLException;
import java.util.Collection;

public class EmptyCommand implements ActionCommand {

    private final
    BookService bookService = new BookService();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        int currentPage = PaginationUtil.getCurrentPage(request);
        try {
            Collection<Book> books = bookService.getAll(currentPage);
            request.getSession().setAttribute("books", books);
            return ConfigurationManager.getProperty("path.page.main");
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
