package ua.od.cepuii.library.command.unregisted;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.od.cepuii.library.command.ActionCommand;
import ua.od.cepuii.library.dto.RequestParser;
import ua.od.cepuii.library.entity.User;
import ua.od.cepuii.library.resource.ConfigurationManager;
import ua.od.cepuii.library.resource.MessageManager;
import ua.od.cepuii.library.service.UserService;
import ua.od.cepuii.library.util.CookieUtil;
import ua.od.cepuii.library.util.ValidationUtil;

public class SignUp implements ActionCommand {

    private static final Logger log = LoggerFactory.getLogger(SignUp.class);
    private final UserService userService = new UserService();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        User user = RequestParser.getUser(request);
        boolean forwardBack = validateUser(request, user);
        if (userService.isExistEmail(user.getEmail())) {
            forwardBack = true;
            log.error("email already exist: {}", user.getEmail());
            request.getSession().setAttribute("emailExist", MessageManager.getProperty("message.signUp.email.exist"));
        }
        if (forwardBack) {
            request.getSession().setAttribute("userEmail", user.getEmail());
            return ConfigurationManager.getProperty("path.page.signUp.forward");
        }
        long userId = userService.createOrUpdate(user);
        user.setId(userId);
        request.getSession().invalidate();
        RequestParser.setUserInfo(request, user);
        CookieUtil.setUserToCookie(response, user);
        return ConfigurationManager.getProperty("path.page.main");
    }

    private static boolean validateUser(HttpServletRequest request, User user) {
        boolean forwardBack = false;
        HttpSession session = request.getSession();
        if (!ValidationUtil.validatePass(user.getPassword())) {
            forwardBack = true;
            session.setAttribute("badPassword", MessageManager.getProperty("message.signUp.password"));
        }
        if (!ValidationUtil.validateEmail(user.getEmail())) {
            forwardBack = true;
            session.setAttribute("badEmail", MessageManager.getProperty("message.signUp.email"));
        }
        String confirmPassword = request.getParameter("confirmPassword");
        if (confirmPassword == null || !confirmPassword.equals(user.getPassword())) {
            forwardBack = true;
            session.setAttribute("badConfirm", MessageManager.getProperty("message.signUp.password.confirm"));
        }
        return forwardBack;
    }
}
