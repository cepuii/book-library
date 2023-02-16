package ua.od.cepuii.library.command.admin;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ua.od.cepuii.library.command.ActionCommand;
import ua.od.cepuii.library.constants.Path;
import ua.od.cepuii.library.dto.RequestParser;

import static ua.od.cepuii.library.constants.AttributesName.REPORTS;

/**
 * This class is responsible for showing add librarian page.
 *
 * @author Sergei Chernousov
 * @version 1.0
 */
public class AddLibrarian implements ActionCommand {

    /**
     * This method handles the request for sets the reports map from the session to the request, if it exists,
     * and returns the path to the add librarian page.
     *
     * @param request  HttpServletRequest request object
     * @param response HttpServletResponse response object
     * @return The path to the add librarian page
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        RequestParser.setMapFromSessionToRequest(request, REPORTS);
        return Path.ADD_LIBRARIAN_PAGE;
    }
}
