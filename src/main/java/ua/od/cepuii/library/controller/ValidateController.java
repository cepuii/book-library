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
            existType = userService.isExistEmail(value);
        } else {
            existType = bookService.isExistTitle(value);
        }
        resp.setContentType("text/plain");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(String.valueOf(existType));

    }
}
