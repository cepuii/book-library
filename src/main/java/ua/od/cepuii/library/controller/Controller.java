package ua.od.cepuii.library.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.od.cepuii.library.command.ActionCommand;
import ua.od.cepuii.library.command.ActionFactory;
import ua.od.cepuii.library.resource.MessageManager;
import ua.od.cepuii.library.util.PathManager;

import java.io.IOException;
public class Controller extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(Controller.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String s = processRequest(req, resp);
        req.getRequestDispatcher(s).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String s = processRequest(req, resp);
        if (s.endsWith("forward=true")) {
            log.info("post forward");
            req.getRequestDispatcher(s).forward(req, resp);
            return;
        }
        resp.sendRedirect(req.getContextPath() + s);
    }

    private String processRequest(HttpServletRequest request, HttpServletResponse response) {
        ActionFactory client = new ActionFactory();
        ActionCommand command = client.defineCommand(request);
        String page = command.execute(request, response);
        if (page != null) {
            log.info("go to page: {}", page);
        } else {
            String msg = MessageManager.getProperty("message.nullPage");
            log.error(msg);
            page = PathManager.getProperty("page.error");
            request.getSession().setAttribute("nullPage", msg);
        }
        return page;
    }
}
