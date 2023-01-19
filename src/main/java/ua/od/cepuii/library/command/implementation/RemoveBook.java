package ua.od.cepuii.library.command.implementation;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.od.cepuii.library.command.ActionCommand;
import ua.od.cepuii.library.resource.ConfigurationManager;
import ua.od.cepuii.library.resource.MessageManager;
import ua.od.cepuii.library.service.BookService;
import ua.od.cepuii.library.util.ValidationUtil;

import java.sql.SQLException;

public class RemoveBook implements ActionCommand {
    private static final Logger log = LoggerFactory.getLogger(RemoveBook.class);
    BookService bookService = new BookService();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String bookIdString = request.getParameter("bookId");
        if (ValidationUtil.isDigit(bookIdString)) {
            try {
                bookService.delete(Long.parseLong(bookIdString));
                log.info("delete book {}", bookIdString);
                log.info("delete book  by user {}", request.getSession().getAttribute("user"));
            } catch (SQLException e) {
                //TODO add remove error
                log.error(e.getMessage());
                request.setAttribute("wrongAction", MessageManager.getProperty("message.wrongAction.delete"));
                return ConfigurationManager.getProperty("path.page.main.forward");
            }
        }
        return ConfigurationManager.getProperty("path.page.main_catalog");
    }
}
