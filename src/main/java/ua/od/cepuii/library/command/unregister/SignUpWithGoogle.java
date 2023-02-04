package ua.od.cepuii.library.command.unregister;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.od.cepuii.library.command.ActionCommand;
import ua.od.cepuii.library.context.AppContext;
import ua.od.cepuii.library.dto.Report;
import ua.od.cepuii.library.dto.RequestParser;
import ua.od.cepuii.library.entity.User;
import ua.od.cepuii.library.service.UserService;
import ua.od.cepuii.library.util.CookieUtil;
import ua.od.cepuii.library.util.PathManager;

import static ua.od.cepuii.library.constants.AttributesName.USER_ID;


public class SignUpWithGoogle implements ActionCommand {

    private static final Logger log = LoggerFactory.getLogger(SignUp.class);
    private final UserService userService = AppContext.getInstance().getUserService();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        User user = RequestParser.getUser(request);
        boolean forwardBack = false;
        if (userService.isExistEmail(user.getEmail()).hasErrors()) {
            forwardBack = true;
            log.error("email already exist: {}", user.getEmail());
            request.setAttribute("emailExist", "message.signUp.email.exist");
        }
        if (forwardBack) {
            request.setAttribute("userEmail", user.getEmail());
            return PathManager.getProperty("page.signUp.forward");
        }
//        long userId = userService.createOrUpdate(user);
        Report report = userService.createOrUpdate(user);
        user.setId(Long.parseLong(report.getReport(USER_ID)));
        RequestParser.setUserInfo(request, user);
        CookieUtil.setUserToCookie(response, user);
        return PathManager.getProperty("controller.books");
    }
}
