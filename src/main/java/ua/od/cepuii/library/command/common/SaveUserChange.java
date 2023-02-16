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
import ua.od.cepuii.library.entity.User;
import ua.od.cepuii.library.service.UserService;

import static ua.od.cepuii.library.constants.AttributesName.REPORTS;

/**
 * This class is responsible for saving changes made to a user's profile in the system.
 *
 * @author Sergei Chernousov
 * @version 1.0
 */
public class SaveUserChange implements ActionCommand {
    private static final Logger log = LoggerFactory.getLogger(SaveUserChange.class);
    private final UserService userService = AppContext.getInstance().getUserService();

    /**
     * The method takes user data from the HttpServletRequest and updates it in the system.
     * The result  message is saved in the  HttpServletRequest object's session as an attribute with the name "REPORTS".
     * If the update is successful, the user is redirected to the show profile page.
     *
     * @param request  The {@link HttpServletRequest} object that contains user data.
     * @param response The {@link HttpServletResponse} object that is used to redirect the user.
     * @return A string representation of the path to the show profile page.
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        User user = RequestParser.getUser(request);

        Report report = userService.createOrUpdate(user);
        request.getSession().setAttribute(REPORTS, report.getReports());

        if (report.hasErrors()) {
            log.error("email already exist: {}", user.getEmail());
        }

        return Path.SHOW_PROFILE;
    }
}
