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

public class SignUp implements ActionCommand {

    private static final Logger log = LoggerFactory.getLogger(SignUp.class);
    private final UserService userService = AppContext.getInstance().getUserService();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        User user = RequestParser.getUser(request);

        Report report = userService.createOrUpdate(user);
        request.getSession().setAttribute(REPORTS, report);

        if (report.hasErrors()) {
            log.error("user doesn`t sign up, user: {}", user);
            request.setAttribute(USER_EMAIL, user.getEmail());
            return Path.SIGN_UP_PAGE_FORWARD;
        }

        log.info("user sign-up, userId: {}", report.getReport(USER_ID));
        user.setId(Long.parseLong(report.getReport(USER_ID)));
        RequestParser.setUserInfo(request, user);
        CookieUtil.setUserToCookie(response, user);
        return Path.SHOW_BOOKS;
    }


}
