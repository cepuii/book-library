package ua.od.cepuii.library.command.unregister;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.od.cepuii.library.command.ActionCommand;
import ua.od.cepuii.library.context.AppContext;
import ua.od.cepuii.library.dto.RequestParser;
import ua.od.cepuii.library.entity.User;
import ua.od.cepuii.library.resource.ConfigurationManager;
import ua.od.cepuii.library.resource.MessageManager;
import ua.od.cepuii.library.service.UserService;
import ua.od.cepuii.library.util.CookieUtil;
import ua.od.cepuii.library.util.PasswordUtil;
import ua.od.cepuii.library.util.ValidationUtil;

public class SignUp implements ActionCommand {

    private static final Logger log = LoggerFactory.getLogger(SignUp.class);
    private final UserService userService = AppContext.getInstance().getUserService();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        User user = RequestParser.getUser(request);
        boolean forwardBack = validateUser(request, user);
        if (userService.isExistEmail(user.getEmail())) {
            forwardBack = true;
            log.error("email already exist: {}", user.getEmail());
            request.setAttribute("emailExist", MessageManager.getProperty("message.signUp.email.exist"));
        }
        if (forwardBack) {
            request.setAttribute("userEmail", user.getEmail());
            return ConfigurationManager.getProperty("path.page.signUp.forward");
        }
        long userId = userService.createOrUpdate(user);
        user.setId(userId);
        RequestParser.setUserInfo(request, user);
        CookieUtil.setUserToCookie(response, user);
        return ConfigurationManager.getProperty("path.controller.books");
    }

    private static boolean validateUser(HttpServletRequest request, User user) {
        boolean forwardBack = false;
        if (!ValidationUtil.validatePass(user.getPassword())) {
            forwardBack = true;
            request.setAttribute("badPassword", MessageManager.getProperty("message.signUp.password"));
        }
        if (!ValidationUtil.validateEmail(user.getEmail())) {
            forwardBack = true;
            request.setAttribute("badEmail", MessageManager.getProperty("message.signUp.email"));
        }
        String confirmPassword = request.getParameter("confirmPassword");
        if (confirmPassword == null || PasswordUtil.verify(confirmPassword, user.getPassword().getBytes())) {
            forwardBack = true;
            request.setAttribute("badConfirm", MessageManager.getProperty("message.signUp.password.confirm"));
        }
        return forwardBack;
    }
}
