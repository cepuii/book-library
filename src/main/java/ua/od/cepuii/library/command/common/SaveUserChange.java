package ua.od.cepuii.library.command.common;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.od.cepuii.library.command.ActionCommand;
import ua.od.cepuii.library.dto.RequestParser;
import ua.od.cepuii.library.entity.User;
import ua.od.cepuii.library.resource.ConfigurationManager;
import ua.od.cepuii.library.resource.MessageManager;
import ua.od.cepuii.library.service.UserService;

public class SaveUserChange implements ActionCommand {
    private static final Logger log = LoggerFactory.getLogger(SaveUserChange.class);
    private final UserService userService = new UserService();

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
        return ConfigurationManager.getProperty("path.controller.profile");
    }
}
