package ua.od.cepuii.library.command.admin;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.od.cepuii.library.command.ActionCommand;
import ua.od.cepuii.library.context.AppContext;
import ua.od.cepuii.library.resource.ConfigurationManager;
import ua.od.cepuii.library.resource.MessageManager;
import ua.od.cepuii.library.service.BookService;
import ua.od.cepuii.library.util.ValidationUtil;

public class RemoveBook implements ActionCommand {
    private static final Logger log = LoggerFactory.getLogger(RemoveBook.class);
    BookService bookService = AppContext.getInstance().getBookService();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String bookIdString = request.getParameter("bookId");
        if (ValidationUtil.isDigit(bookIdString)) {
            boolean delete = bookService.delete(Long.parseLong(bookIdString));
            log.info("delete book {}  by user {}, result {}", bookIdString, request.getSession().getAttribute("user"), delete);
            if (!delete) {
                request.getSession().setAttribute("wrongAction", MessageManager.getProperty("message.wrongAction.delete"));
            }
        }
        return ConfigurationManager.getProperty("path.controller.books");
    }
}
