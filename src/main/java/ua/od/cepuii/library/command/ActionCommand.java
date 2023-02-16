package ua.od.cepuii.library.command;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * The ActionCommand interface defines the method to execute an action.
 *
 * @author Sergei Chernousov
 */
public interface ActionCommand {

    /**
     * Executes the action for the given request and response.
     *
     * @param request  the HttpServletRequest object that contains the request the client has made of the servlet
     * @param response the HttpServletResponse object that contains the response the servlet sends to the client
     * @return the path to the page for forward or redirect
     */
    String execute(HttpServletRequest request, HttpServletResponse response);
}
