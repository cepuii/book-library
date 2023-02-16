package ua.od.cepuii.library.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.od.cepuii.library.context.AppContext;
import ua.od.cepuii.library.service.BookService;
import ua.od.cepuii.library.service.UserService;

import java.io.IOException;

/**
 * This class serves as a servlet for handling the validation requests for email and book title in the library application.
 * It uses {@link UserService} and {@link BookService} to determine whether the provided email or book title already exists.
 *
 * @author Serhei Chernousov
 * @version 1.0
 * @since 2022-12-19
 */
@WebServlet(name = "ValidationServlet", urlPatterns = "/validate/*")
public class ValidateController extends HttpServlet {

    private final UserService userService = AppContext.getInstance().getUserService();
    private final BookService bookService = AppContext.getInstance().getBookService();
    private static final Logger log = LoggerFactory.getLogger(ValidateController.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String type = req.getParameter("type");
        String value = req.getParameter("value");
        log.info("validationController type: {} - {}", type, value);
        boolean existType = false;
        if (type.equals("email")) {
            existType = userService.isExistEmail(value).hasErrors();
        } else {
            existType = bookService.isExistTitle(value).hasErrors();
        }
        resp.setContentType("text/plain");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(String.valueOf(existType));

    }
}
