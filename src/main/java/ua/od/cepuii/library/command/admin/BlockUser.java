package ua.od.cepuii.library.command.admin;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.od.cepuii.library.command.ActionCommand;
import ua.od.cepuii.library.dto.RequestParser;
import ua.od.cepuii.library.resource.ConfigurationManager;
import ua.od.cepuii.library.service.UserService;

public class BlockUser implements ActionCommand {

    private static final Logger log = LoggerFactory.getLogger(BlockUser.class);

    UserService userService = new UserService();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        long userId = RequestParser.getLong(request, "userId");
        boolean isBlocked = RequestParser.getBoolean(request, "isBlocked");
        boolean blockUnblock = userService.blockUnblock(userId, isBlocked);
        log.info("user {} blockUnblock isComplete {}", userId, blockUnblock);
        return ConfigurationManager.getProperty("path.controller.users");
    }
}
