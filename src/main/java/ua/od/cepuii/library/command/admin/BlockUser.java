package ua.od.cepuii.library.command.admin;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.od.cepuii.library.command.ActionCommand;
import ua.od.cepuii.library.constants.Path;
import ua.od.cepuii.library.context.AppContext;
import ua.od.cepuii.library.dto.Report;
import ua.od.cepuii.library.service.UserService;

import static ua.od.cepuii.library.constants.AttributesName.*;
import static ua.od.cepuii.library.dto.RequestParser.getBoolean;
import static ua.od.cepuii.library.dto.RequestParser.getLong;

/**
 * This class is responsible for blocking or unblocking user.
 * It uses the {@link UserService} to call the appropriate method.
 *
 * @author Sergei Chernousov
 * @version 1.0
 */
public class BlockUser implements ActionCommand {

    private static final Logger log = LoggerFactory.getLogger(BlockUser.class);

    private final UserService userService = AppContext.getInstance().getUserService();

    /**
     * This method handles the request for blocking or unblocking a user. It retrieves the
     * relevant userID and block status from the request, performs the block or unblock action
     * through the {@link UserService}, and sets the resulting reports in the session. It also logs
     * the result of the action and returns the path to the show users page.
     *
     * @param request  HttpServletRequest request object
     * @param response HttpServletResponse response object
     * @return Path.SHOW_USERS the path to the show users page
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        long blockUserId = getLong(request, BLOCK_USER_ID);
        boolean isBlocked = getBoolean(request, IS_BLOCKED);
        long userId = getLong(request, USER_ID);

        Report report = userService.blockUnblock(userId, blockUserId, isBlocked);

        request.getSession().setAttribute(REPORTS, report.getReports());
        log.info("user block/enable, userId: {}, {}", blockUserId, report);

        return Path.SHOW_USERS;
    }
}
