package ua.od.cepuii.library.command.common;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.od.cepuii.library.command.ActionCommand;
import ua.od.cepuii.library.constants.Path;
import ua.od.cepuii.library.context.AppContext;
import ua.od.cepuii.library.dto.Report;
import ua.od.cepuii.library.dto.RequestParser;
import ua.od.cepuii.library.service.UserService;

import static ua.od.cepuii.library.constants.AttributesName.*;

/**
 * This class is responsible for saving new password for user in the system.
 * It uses the {@link UserService} to call the appropriate method update user.
 *
 * @author Sergei Chernousov
 * @version 1.0
 */
public class ChangePassword implements ActionCommand {

    private static final Logger log = LoggerFactory.getLogger(ChangePassword.class);
    private final UserService userService = AppContext.getInstance().getUserService();

    /**
     * This method updates the password of a user in the database.
     * If the method of the request is GET, it sets any previous reports from the session to the request.
     * If the method is POST, it retrieves the old password and the new password from the request parameters,
     * retrieves the user id, and updates the password in the database.
     * If there are any errors, the user is redirected to the change password page.
     * If there are no errors, the user is redirected to the show profile page.
     *
     * @param request  HttpServletRequest request
     * @param response HttpServletResponse response
     * @return A string representing the URL of the next page to be displayed.
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        if (request.getMethod().equalsIgnoreCase(GET)) {
            RequestParser.setMapFromSessionToRequest(request, REPORTS);
            return Path.CHANGE_PASSWORD_PAGE;
        }
        String oldPassword = request.getParameter(OLD_PASS);
        String newPassword = request.getParameter(NEW_PASSWORD);

        long userId = RequestParser.getLong(request, USER_ID);
        Report report = userService.updatePassword(userId, oldPassword, newPassword);
        request.getSession().setAttribute(REPORTS, report.getReports());
        if (report.hasErrors()) {
            return Path.CHANGE_PASSWORD;
        }
        log.info("userId: {}, changePassword", userId);
        return Path.SHOW_PROFILE;
    }


}
