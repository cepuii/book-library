package ua.od.cepuii.library.command.implementation;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.od.cepuii.library.command.ActionCommand;
import ua.od.cepuii.library.entity.User;
import ua.od.cepuii.library.resource.ConfigurationManager;
import ua.od.cepuii.library.resource.MessageManager;
import ua.od.cepuii.library.service.UserService;

public class LoginCommand implements ActionCommand {

    private static final Logger log = LoggerFactory.getLogger(LoginCommand.class);

    UserService userService = new UserService();

    private static final String PARAM_NAME_EMAIL = "email";
    private static final String PARAM_NAME_PASSWORD = "password";

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String email = request.getParameter(PARAM_NAME_EMAIL);
        String password = request.getParameter(PARAM_NAME_PASSWORD);
        String page;
        User user = userService.getUserByEmailAndPassword(email, password);
        if (user != null) {
            request.getSession().setAttribute("userId", user.getId());
            request.getSession().setAttribute("user", user.getEmail());
            request.getSession().setAttribute("userRole", user.getRole().toString());
            log.info("user login: {}", user);
            page = ConfigurationManager.getProperty("path.page.main");
        } else {
            log.info("error incorrect login");
            request.setAttribute("errorLoginPassMessage",
                    MessageManager.getProperty("message.loginerror"));
            page = ConfigurationManager.getProperty("path.page.login") + ConfigurationManager.getProperty("path.page.forward");
        }
        return page;
    }
}
