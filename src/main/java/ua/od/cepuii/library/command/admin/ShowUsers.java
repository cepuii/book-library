package ua.od.cepuii.library.command.admin;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.od.cepuii.library.command.ActionCommand;
import ua.od.cepuii.library.constants.Path;
import ua.od.cepuii.library.context.AppContext;
import ua.od.cepuii.library.dto.FilterParams;
import ua.od.cepuii.library.dto.Page;
import ua.od.cepuii.library.dto.RequestParser;
import ua.od.cepuii.library.dto.UserTO;
import ua.od.cepuii.library.service.UserService;

import java.util.Collection;

import static ua.od.cepuii.library.constants.AttributesName.*;

public class ShowUsers implements ActionCommand {
    private static final Logger log = LoggerFactory.getLogger(ShowUsers.class);

    private final UserService userService = AppContext.getInstance().getUserService();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {

        //TODO change filter, make the same names
        FilterParams filterParam = RequestParser.getFilterParams(request, "userSearch", "userRoleSearch");
        Page page = RequestParser.getPage(request, userService, filterParam);

        Collection<UserTO> users = userService.getAll(page, filterParam);
        request.setAttribute(USERS, users);

        request.getSession().setAttribute(FILTER, filterParam);
        request.setAttribute(PAGE, page);
        log.info("page attributes {}", page);
        log.info("filter attributes {}", filterParam);

        RequestParser.setMapFromSessionToRequest(request, REPORTS);
        return Path.USERS_PAGE;
    }
}
