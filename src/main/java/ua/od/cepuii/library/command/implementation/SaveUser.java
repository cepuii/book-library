package ua.od.cepuii.library.command.implementation;

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

public class SaveUser implements ActionCommand {
    private static final Logger log = LoggerFactory.getLogger(SaveUser.class);

    private final UserService userService = new UserService();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        User user = RequestParser.getUser(request);
        long l = userService.create(user);
        if (l == -1) {
            request.setAttribute("newUser", user);
            request.setAttribute("wrongAction", MessageManager.getProperty("message.wrongAction.add"));
            log.error("can`t add user {}", user);
            return ConfigurationManager.getProperty("path.page.add.librarian");

        }
        return ConfigurationManager.getProperty("path.controller.users") + "&" + ConfigurationManager.getProperty("path.success");
    }
}