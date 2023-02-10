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
import static ua.od.cepuii.library.constants.Constants.SUCCESS;
import static ua.od.cepuii.library.constants.Constants.WRONG_ACTION;

public class Login implements ActionCommand {

    private static final Logger log = LoggerFactory.getLogger(Login.class);
    private final UserService userService = AppContext.getInstance().getUserService();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String email = RequestParser.getParameterOrAttribute(request, EMAIL);
        String password = RequestParser.getParameterOrAttribute(request, EMAIL);

        User user = userService.getUserByEmailAndPassword(email, password);
        Report report = Report.newInstance();

        if (user == null) {
            log.info("error incorrect login");
            report.addReport(USER_EMAIL, email);
            report.addError(WRONG_ACTION, "message.loginError");
            request.setAttribute(REPORTS, report);
            return Path.LOGIN_PAGE_FORWARD;
        }

        request.getSession().removeAttribute(LOGOUT);
        RequestParser.setUserInfo(request, user);
        CookieUtil.setUserToCookie(response, user);
        log.info("user login: {}", user);
        report.addReport(SUCCESS, "message.user.greets");
        request.getSession().setAttribute(REPORTS, report);
        return Path.SHOW_BOOKS;
    }


}
