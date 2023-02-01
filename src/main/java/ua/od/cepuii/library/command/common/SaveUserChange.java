package ua.od.cepuii.library.command.common;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.od.cepuii.library.command.ActionCommand;
import ua.od.cepuii.library.context.AppContext;
import ua.od.cepuii.library.dto.RequestParser;
import ua.od.cepuii.library.entity.User;
import ua.od.cepuii.library.resource.MessageManager;
import ua.od.cepuii.library.service.UserService;
import ua.od.cepuii.library.util.PathManager;

public class SaveUserChange implements ActionCommand {
    private static final Logger log = LoggerFactory.getLogger(SaveUserChange.class);
    private final UserService userService = AppContext.getInstance().getUserService();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        User user = RequestParser.getUser(request);
        if (userService.isExistEmail(user.getEmail())) {
            log.error("email already exist: {}", user.getEmail());
            request.getSession().setAttribute("emailExist", MessageManager.getProperty("message.signUp.email.exist"));
        } else {
            userService.createOrUpdate(user);
            request.getSession().setAttribute("userEmail", user.getEmail());
        }
        return PathManager.getProperty("controller.profile");
    }
}
