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
import ua.od.cepuii.library.entity.User;
import ua.od.cepuii.library.service.UserService;

import static ua.od.cepuii.library.constants.AttributesName.REPORTS;

public class SaveUserChange implements ActionCommand {
    private static final Logger log = LoggerFactory.getLogger(SaveUserChange.class);
    private final UserService userService = AppContext.getInstance().getUserService();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        User user = RequestParser.getUser(request);

        Report reports = userService.isExistEmail(user.getEmail());
        request.getSession().setAttribute(REPORTS, reports);

        if (reports.hasErrors()) {
            log.error("email already exist: {}", user.getEmail());
        } else {
            userService.createOrUpdate(user);
        }

        return Path.SHOW_PROFILE;
    }
}
