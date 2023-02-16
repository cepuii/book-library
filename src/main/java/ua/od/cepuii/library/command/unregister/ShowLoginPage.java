package ua.od.cepuii.library.command.unregister;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ua.od.cepuii.library.command.ActionCommand;
import ua.od.cepuii.library.constants.Path;


/**
 * This class is responsible for showing login page.
 *
 * @author Sergei Chernousov
 * @version 1.0
 */
public class ShowLoginPage implements ActionCommand {


    /**
     * Handles the request to display the login page by returning the path to the login page.
     *
     * @param request  the HttpServletRequest object that contains the request the client has made of the servlet
     * @param response the HttpServletResponse object that contains the response the servlet sends to the client
     * @return the path to the login page
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        return Path.LOGIN_PAGE;
    }
}
