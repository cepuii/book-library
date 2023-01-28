package ua.od.cepuii.library.command.common;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.od.cepuii.library.command.ActionCommand;
import ua.od.cepuii.library.context.AppContext;
import ua.od.cepuii.library.dto.RequestParser;
import ua.od.cepuii.library.resource.ConfigurationManager;
import ua.od.cepuii.library.resource.MessageManager;
import ua.od.cepuii.library.service.UserService;
import ua.od.cepuii.library.util.ValidationUtil;

public class ChangePassword implements ActionCommand {

    private static final Logger log = LoggerFactory.getLogger(ChangePassword.class);
    private final UserService userService = AppContext.getInstance().getUserService();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        if (request.getMethod().equalsIgnoreCase("get")) {
            return ConfigurationManager.getProperty("path.page.changePassword");
        }
        //TODO move Validation
        String oldPassword = request.getParameter("oldPassword");
        String newPassword = request.getParameter("newPassword");
        long userId = RequestParser.getLong(request, "userId");
        boolean forwardBack = validatePasswords(request, userId, oldPassword, newPassword);
        if (!forwardBack) {
            if (userService.updatePassword(userId, newPassword)) {
                log.info("userId: {}, changePassword", userId);
                request.getSession().setAttribute("success", MessageManager.getProperty("message.password.change"));
                return ConfigurationManager.getProperty("path.controller.profile.success");
            }
            request.setAttribute("badPassword", MessageManager.getProperty("message.signUp.password"));
        }
        return ConfigurationManager.getProperty("path.page.changePassword.forward");
    }

    private boolean validatePasswords(HttpServletRequest request, long userId, String oldPassword, String newPassword) {
        boolean forwardBack = false;
        if (oldPassword.equals(newPassword)) {
            forwardBack = true;
            request.setAttribute("badPasswords", MessageManager.getProperty("message.change.password.same"));
        }
        if (!ValidationUtil.validatePass(oldPassword)) {
            forwardBack = true;
            request.setAttribute("badPassword", MessageManager.getProperty("message.signUp.password"));
        }
        String confirmPassword = request.getParameter("confirmPassword");
        if (confirmPassword == null || !confirmPassword.equals(newPassword)) {
            forwardBack = true;
            request.setAttribute("badConfirm", MessageManager.getProperty("message.signUp.password.confirm"));
        }
        if (!ValidationUtil.validatePass(oldPassword) && userService.checkPassword(userId, oldPassword)) {
            forwardBack = true;
            request.setAttribute("badOldPassword", MessageManager.getProperty("message.change.password"));
        }
        return forwardBack;
    }
}
