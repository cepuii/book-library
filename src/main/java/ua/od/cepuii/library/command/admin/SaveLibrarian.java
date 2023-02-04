package ua.od.cepuii.library.command.admin;

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

import static ua.od.cepuii.library.constants.AttributesName.NEW_USER;
import static ua.od.cepuii.library.constants.AttributesName.REPORTS;

public class SaveLibrarian implements ActionCommand {
    private static final Logger log = LoggerFactory.getLogger(SaveLibrarian.class);

    private final UserService userService = AppContext.getInstance().getUserService();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {

        User user = RequestParser.getUser(request);
        Report report = userService.createOrUpdate(user);

        if (report.hasErrors()) {
            request.setAttribute(NEW_USER, user);
            request.setAttribute(REPORTS, report);
            return Path.ADD_LIBRARIAN_FORWARD;
        }

        log.info("librarian save, userId: {}", user.getId());
        request.getSession().setAttribute(REPORTS, report);
        return Path.SHOW_USERS;
    }
}
