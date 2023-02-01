package ua.od.cepuii.library.command.admin;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.od.cepuii.library.command.ActionCommand;
import ua.od.cepuii.library.context.AppContext;
import ua.od.cepuii.library.dto.RequestParser;
import ua.od.cepuii.library.entity.Book;
import ua.od.cepuii.library.resource.MessageManager;
import ua.od.cepuii.library.service.BookService;
import ua.od.cepuii.library.util.PathManager;

public class SaveBook implements ActionCommand {
    private static final Logger log = LoggerFactory.getLogger(SaveBook.class);
    BookService bookService = AppContext.getInstance().getBookService();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        Book book = RequestParser.getBook(request);
        if (bookService.isExistTitle(book.getTitle())) {
            String error = MessageManager.getProperty("message.book.title.exist");
            log.error(error);
            request.setAttribute("wrongAction", error);
        } else {
            boolean update = bookService.createOrUpdate(book);
            if (update) {
                request.getSession().setAttribute("success", MessageManager.getProperty("message.book.add"));
                return PathManager.getProperty("controller.books");
            }

            request.setAttribute("wrongAction", MessageManager.getProperty("message.wrongAction.edit"));
        }
        request.setAttribute("book", book);
        return PathManager.getProperty("page.edit.book.forward");
    }
}
