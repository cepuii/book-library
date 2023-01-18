package ua.od.cepuii.library.command.implementation;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.od.cepuii.library.command.ActionCommand;
import ua.od.cepuii.library.dto.FilterAndSortParams;
import ua.od.cepuii.library.dto.Page;
import ua.od.cepuii.library.dto.RequestParser;
import ua.od.cepuii.library.entity.Book;
import ua.od.cepuii.library.resource.ConfigurationManager;
import ua.od.cepuii.library.service.BookService;

import java.util.Collection;

public class ShowBooks implements ActionCommand {
    private static final Logger log = LoggerFactory.getLogger(ShowBooks.class);

    private final BookService bookService = new BookService();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        FilterAndSortParams filterParam = RequestParser.getFilterParams(request, "titleSearch", "authorSearch");
        Page currentPage = RequestParser.getPageFromSession(request, bookService, filterParam);
        Collection<Book> books = bookService.getAll(currentPage, filterParam);
        request.setAttribute("books", books);
        request.setAttribute("data", "receive");
        request.getSession().setAttribute("filter", filterParam);
        request.getSession().setAttribute("page", currentPage);
        log.info("page attributes {}", currentPage);
        return ConfigurationManager.getProperty("path.page.main");
    }
}
