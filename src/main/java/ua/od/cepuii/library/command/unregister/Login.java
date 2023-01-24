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

public class Login implements ActionCommand {

    private static final Logger log = LoggerFactory.getLogger(Login.class);
    private final UserService userService = AppContext.getInstance().getUserService();
    private static final String PARAM_NAME_EMAIL = "email";
    private static final String PARAM_NAME_PASSWORD = "password";

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String page;
        String email = request.getParameter(PARAM_NAME_EMAIL);
        String password = request.getParameter(PARAM_NAME_PASSWORD);
        User user = userService.getUserByEmailAndPassword(email, password);
        if (user != null) {
            request.getSession().removeAttribute("logout");
            RequestParser.setUserInfo(request, user);
            CookieUtil.setUserToCookie(response, user);
            log.info("user login: {}", user);
            page = ConfigurationManager.getProperty("path.controller.books");
        } else {
            log.info("error incorrect login");
            request.setAttribute("userEmail", email);
            request.setAttribute("errorLoginPassMessage",
                    MessageManager.getProperty("message.loginError"));
            page = ConfigurationManager.getProperty("path.page.login.forward");
        }
        return page;
    }


}
