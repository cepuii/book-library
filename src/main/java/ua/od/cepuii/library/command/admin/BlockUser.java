package ua.od.cepuii.library.command.admin;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.od.cepuii.library.command.ActionCommand;
import ua.od.cepuii.library.context.AppContext;
import ua.od.cepuii.library.resource.MessageManager;
import ua.od.cepuii.library.service.UserService;
import ua.od.cepuii.library.util.PathManager;

import static ua.od.cepuii.library.dto.RequestParser.getBoolean;
import static ua.od.cepuii.library.dto.RequestParser.getLong;

public class BlockUser implements ActionCommand {

    private static final Logger log = LoggerFactory.getLogger(BlockUser.class);

    private final UserService userService = AppContext.getInstance().getUserService();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        long blockUserId = getLong(request, "blockUserId");
        boolean isBlocked = getBoolean(request, "isBlocked");
        long userId = getLong(request, "userId");
        String path;
        if (blockUserId == userId) {
            request.getSession().setAttribute("wrongAction", MessageManager.getProperty("message.block.yourself"));
            path = PathManager.getProperty("controller.users");
        } else {
            boolean blockUnblock = userService.blockUnblock(blockUserId, isBlocked);
            log.info("user {} blockUnblock isComplete {}", blockUserId, blockUnblock);
            path = PathManager.getProperty("controller.users.success");
        }
        return path;
    }
}
