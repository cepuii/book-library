package ua.od.cepuii.library.command.common;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.od.cepuii.library.command.ActionCommand;
import ua.od.cepuii.library.constants.Path;
import ua.od.cepuii.library.context.AppContext;
import ua.od.cepuii.library.dto.Report;
import ua.od.cepuii.library.dto.RequestParser;
import ua.od.cepuii.library.service.UserService;
import ua.od.cepuii.library.util.PathManager;

import static ua.od.cepuii.library.constants.AttributesName.*;

public class ChangePassword implements ActionCommand {

    private static final Logger log = LoggerFactory.getLogger(ChangePassword.class);
    private final UserService userService = AppContext.getInstance().getUserService();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        if (request.getMethod().equalsIgnoreCase(GET)) {
            RequestParser.setMapFromSessionToRequest(request, REPORTS);
            return Path.CHANGE_PASSWORD_PAGE;
        }

        String oldPassword = request.getParameter(OLD_PASS);
        String newPassword = request.getParameter(PASSWORD);
        String confirmPassword = request.getParameter(CONFIRM_PASS);

        long userId = RequestParser.getLong(request, USER_ID);
        Report report = userService.updatePassword(userId, oldPassword, newPassword, confirmPassword);
        request.getSession().setAttribute(REPORTS, report.getReports());
        if (report.hasErrors()) {
            return Path.CHANGE_PASSWORD;
        }
        log.info("userId: {}, changePassword", userId);
        return PathManager.getProperty("controller.profile");
    }


}
