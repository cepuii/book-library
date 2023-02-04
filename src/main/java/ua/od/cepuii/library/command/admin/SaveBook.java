package ua.od.cepuii.library.command.admin;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.od.cepuii.library.command.ActionCommand;
import ua.od.cepuii.library.constants.Path;
import ua.od.cepuii.library.context.AppContext;
import ua.od.cepuii.library.dto.Report;
import ua.od.cepuii.library.dto.RequestParser;
import ua.od.cepuii.library.entity.Book;
import ua.od.cepuii.library.service.BookService;
import ua.od.cepuii.library.util.PathManager;

import static ua.od.cepuii.library.constants.AttributesName.BOOK;
import static ua.od.cepuii.library.constants.AttributesName.REPORTS;

public class SaveBook implements ActionCommand {
    private static final Logger log = LoggerFactory.getLogger(SaveBook.class);
    BookService bookService = AppContext.getInstance().getBookService();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        Book book = RequestParser.getBook(request);
        log.info("create or update book: {}", book);
        Report report = bookService.createOrUpdate(book);

        if (report.hasErrors()) {
            request.setAttribute(BOOK, book);
            request.setAttribute(REPORTS, report);
            return PathManager.getProperty("page.edit.book.forward");
        }
        request.getSession().setAttribute(REPORTS, report);
        return Path.SHOW_BOOKS;
    }
}
