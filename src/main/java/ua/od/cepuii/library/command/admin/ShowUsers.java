package ua.od.cepuii.library.command.admin;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.od.cepuii.library.command.ActionCommand;
import ua.od.cepuii.library.context.AppContext;
import ua.od.cepuii.library.dto.FilterParams;
import ua.od.cepuii.library.dto.Page;
import ua.od.cepuii.library.dto.RequestParser;
import ua.od.cepuii.library.dto.UserTO;
import ua.od.cepuii.library.service.UserService;
import ua.od.cepuii.library.util.PathManager;

import java.util.Collection;

public class ShowUsers implements ActionCommand {
    private static final Logger log = LoggerFactory.getLogger(ShowUsers.class);

    private final UserService userService = AppContext.getInstance().getUserService();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {

        FilterParams filterParam = RequestParser.getFilterParams(request, "userSearch", "userRoleSearch");
        Page currentPage = RequestParser.getPage(request, userService, filterParam);

        Collection<UserTO> users = userService.getAll(currentPage, filterParam);
        request.setAttribute("users", users);

        request.getSession().setAttribute("filter", filterParam);
        request.setAttribute("page", currentPage);
        log.info("page attributes {}", currentPage);
        log.info("filter attributes {}", filterParam);

        RequestParser.setFromSessionToRequest(request, "wrongAction");
        return PathManager.getProperty("page.users");
    }
}
