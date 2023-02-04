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

public class BlockUser implements ActionCommand {

    private static final Logger log = LoggerFactory.getLogger(BlockUser.class);

    private final UserService userService = AppContext.getInstance().getUserService();

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
