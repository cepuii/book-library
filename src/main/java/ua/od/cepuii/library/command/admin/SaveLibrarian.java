package ua.od.cepuii.library.command.admin;

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

import static ua.od.cepuii.library.constants.AttributesName.NEW_USER;
import static ua.od.cepuii.library.constants.AttributesName.REPORTS;

/**
 * This class is responsible for saving new user with role Librarian in the system.
 * It uses the {@link UserService} to call the appropriate method to create user or update user.
 *
 * @author Sergei Chernousov
 * @version 1.0
 */
public class SaveLibrarian implements ActionCommand {
    private static final Logger log = LoggerFactory.getLogger(SaveLibrarian.class);

    private final UserService userService = AppContext.getInstance().getUserService();

    /**
     * The method retrieves the librarian information from the HttpServletRequest, and passes it to the
     * {@link UserService#createOrUpdate} method to be saved in the database. If the save operation is successful,
     * the user is redirected to the {@link Path#SHOW_USERS} page, otherwise, the user is redirected to the
     * {@link Path#ADD_LIBRARIAN_FORWARD} page with a report of errors.
     *
     * @param request  HttpServletRequest object
     * @param response HttpServletResponse object
     * @return a string indicating the next page the user should be redirected to
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {

        User user = RequestParser.getUser(request);
        Report report = userService.createOrUpdate(user);

        if (report.hasErrors()) {
            request.setAttribute(NEW_USER, user);
            request.setAttribute(REPORTS, report.getReports());
            return Path.ADD_LIBRARIAN_FORWARD;
        }

        log.info("librarian save, userId: {}", user.getId());
        request.getSession().setAttribute(REPORTS, report.getReports());
        return Path.SHOW_USERS;
    }
}
