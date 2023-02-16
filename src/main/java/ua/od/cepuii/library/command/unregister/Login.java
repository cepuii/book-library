package ua.od.cepuii.library.command.unregister;

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
import ua.od.cepuii.library.util.CookieUtil;

import static ua.od.cepuii.library.constants.AttributesName.*;

/**
 * This class is responsible for logging user in the system.
 *
 * @author Sergei Chernousov
 * @version 1.0
 */
public class Login implements ActionCommand {

    private static final Logger log = LoggerFactory.getLogger(Login.class);
    private final UserService userService = AppContext.getInstance().getUserService();

    /**
     * Logs in a user by validating their email and password. If the login information is valid, the user information
     * is stored in the session and a cookie. If the login information is invalid, an error message is added to the
     * report and the user is redirected back to the login page.
     *
     * @param request  the servlet request
     * @param response the servlet response
     * @return the path to the next page
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String email = RequestParser.getParameterOrAttribute(request, EMAIL);
        String password = RequestParser.getParameterOrAttribute(request, PASSWORD);

        User user = userService.getUserByEmailAndPassword(email, password);
        Report report = Report.newInstance();

        if (user == null) {
            log.info("error incorrect login");
            report.addReport(USER_EMAIL, email);
            report.addError(WRONG_ACTION, "message.loginError");
            request.setAttribute(REPORTS, report.getReports());
            return Path.LOGIN_PAGE_FORWARD;
        }

        request.getSession().removeAttribute(LOGOUT);
        RequestParser.setUserInfo(request, user);
        CookieUtil.setUserToCookie(response, user);
        log.info("user login: {}", user);
        report.addReport(SUCCESS, "message.user.greets");
        request.getSession().setAttribute(REPORTS, report.getReports());
        return Path.SHOW_BOOKS;
    }


}
