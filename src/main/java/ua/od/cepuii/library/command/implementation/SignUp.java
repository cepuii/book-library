package ua.od.cepuii.library.command.implementation;

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
import ua.od.cepuii.library.util.ValidationUtil;

public class SignUp implements ActionCommand {

    private static final Logger log = LoggerFactory.getLogger(SignUp.class);
    private final UserService userService = new UserService();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String path;
        User user = RequestParser.getUser(request);

        if (ValidationUtil.validateUser(user)) {
            long userId = userService.create(user);
            HttpSession session = request.getSession();
            session.setAttribute("userId", userId);
            session.setAttribute("user", user.getEmail());
            path = ConfigurationManager.getProperty("path.page.main");
        } else {
            request.setAttribute("errorLoginPassMessage", MessageManager.getProperty("message.signuperror"));
            path = ConfigurationManager.getProperty("path.page.signup");
        }

        return path;
    }
}
