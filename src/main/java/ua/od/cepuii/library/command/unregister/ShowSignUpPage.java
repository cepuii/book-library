package ua.od.cepuii.library.command.unregister;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ua.od.cepuii.library.command.ActionCommand;
import ua.od.cepuii.library.constants.Path;

/**
 * This class is responsible for showing sign-up page.
 *
 * @author Sergei Chernousov
 * @version 1.0
 */
public class ShowSignUpPage implements ActionCommand {

    /**
     * Handles the request to display the sign-up page.
     *
     * @param request  the HttpServletRequest object that contains the request the client has made of the servlet
     * @param response the HttpServletResponse object that contains the response the servlet sends to the client
     * @return the path to the sign-up page
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        return Path.SIGN_UP_PAGE;
    }
}
