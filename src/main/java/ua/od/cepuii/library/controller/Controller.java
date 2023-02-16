package ua.od.cepuii.library.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.od.cepuii.library.command.ActionCommand;
import ua.od.cepuii.library.command.ActionFactory;
import ua.od.cepuii.library.constants.Path;

import java.io.IOException;

/**
 * Controller class is a servlet that is responsible for processing HTTP requests and responses.
 * This class acts as a central point of control for all requests coming to the application.
 * <p>
 * The class extends the HttpServlet and overrides the doGet() and doPost() methods to handle HTTP GET and POST requests respectively.
 * It uses the processRequest() method to delegate the request to the corresponding action command defined by the ActionFactory.
 * The processRequest() method returns a String representing the URL of the next page to which the user will be redirected.
 * <p>
 * If the returned URL ends with "forward=true", the request will be forwarded to the specified page,
 * otherwise the response will be redirected to the specified page.
 *
 * @author Serhei Chernousov
 * @version 1.0
 */
public class Controller extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(Controller.class);


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String s = processRequest(req, resp);
        log.info("doGet, go to {}", s);
        req.getRequestDispatcher(s).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String s = processRequest(req, resp);
        log.info("doPost, go to {}", s);
        if (s.endsWith("forward=true")) {
            req.getRequestDispatcher(s).forward(req, resp);
            return;
        }
        resp.sendRedirect(req.getContextPath() + s);
    }

    private String processRequest(HttpServletRequest request, HttpServletResponse response) {
        ActionFactory client = new ActionFactory();
        ActionCommand command = client.defineCommand(request);
        String page = command.execute(request, response);
        if (page == null) {
            String msg = "message.nullPage";
            log.error(msg);
            page = Path.ERROR_PAGE;
            request.getSession().setAttribute("nullPage", msg);
        }
        return page;
    }
}
