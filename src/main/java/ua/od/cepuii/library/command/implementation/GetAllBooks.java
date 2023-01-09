package ua.od.cepuii.library.command.implementation;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ua.od.cepuii.library.command.ActionCommand;
import ua.od.cepuii.library.dto.BookFilterParam;
import ua.od.cepuii.library.dto.Page;
import ua.od.cepuii.library.dto.Wrapper;
import ua.od.cepuii.library.entity.Book;
import ua.od.cepuii.library.resource.ConfigurationManager;
import ua.od.cepuii.library.service.BookService;

import java.sql.SQLException;
import java.util.Collection;

public class GetAllBooks implements ActionCommand {

    private final
    BookService bookService = new BookService();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        Page currentPage = Wrapper.getCurrentPage(request);
        String orderBy = request.getParameter("orderBy");
        boolean descending = Boolean.parseBoolean(request.getParameter("descending"));
        String titleSearch = request.getParameter("titleSearch");
        String authorSearch = request.getParameter("authorSearch");
        BookFilterParam filterParam = new BookFilterParam(titleSearch, authorSearch);
        if (currentPage.getPageAmount() == 0) {
            int pageAmount = bookService.getPageSettings(currentPage, filterParam);
            currentPage.setPageAmount(pageAmount);
        }
        try {
            Collection<Book> books = bookService.getAll(orderBy, descending, currentPage);
            request.getSession().setAttribute("books", books);
            request.setAttribute("currentPage", currentPage.getCurrentPage());
            request.setAttribute("noOfRecords", currentPage.getNoOfRecords());
            request.setAttribute("lastPage", currentPage.getPageAmount());
            return ConfigurationManager.getProperty("path.page.main");
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
