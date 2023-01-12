package ua.od.cepuii.library.command.implementation;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.od.cepuii.library.command.ActionCommand;
import ua.od.cepuii.library.dto.BookFilterParam;
import ua.od.cepuii.library.dto.Page;
import ua.od.cepuii.library.dto.RequestParser;
import ua.od.cepuii.library.entity.Book;
import ua.od.cepuii.library.resource.ConfigurationManager;
import ua.od.cepuii.library.service.BookService;

import java.sql.SQLException;
import java.util.Collection;

public class GetAllBooks implements ActionCommand {
    private static final Logger log = LoggerFactory.getLogger(GetAllBooks.class);

    private final BookService bookService = new BookService();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        Page currentPage = RequestParser.getPageFromSession(request);
        if (request.getParameter("modified") != null) {
            currentPage.setPageAmount(0);
        }
        BookFilterParam filterParam = RequestParser.getBookFilter(request);
        if (currentPage.getPageAmount() == 0) {
            int pageAmount = bookService.getPageAmount(currentPage, filterParam);
            currentPage.setPageAmount(pageAmount);
        }
        try {
            Collection<Book> books = bookService.getAll(currentPage, filterParam);
            request.setAttribute("books", books);
            request.setAttribute("data", "receive");
            request.getSession().setAttribute("filter", filterParam);
            request.getSession().setAttribute("page", currentPage);
            log.info("page attributes {}", currentPage);
            log.info("filter attributes {}", filterParam);
            return ConfigurationManager.getProperty("path.page.main");
        } catch (SQLException e) {
            log.error(e.getMessage());
            return ConfigurationManager.getProperty("path.page.error");
        }
    }
}
