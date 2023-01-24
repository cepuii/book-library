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
import ua.od.cepuii.library.resource.ConfigurationManager;
import ua.od.cepuii.library.service.UserService;

import java.util.Collection;

public class ShowUsers implements ActionCommand {
    private static final Logger log = LoggerFactory.getLogger(ShowUsers.class);

    private final UserService userService = AppContext.getInstance().getUserService();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        FilterParams filterParam = RequestParser.getFilterParams(request, "userSearch", "userRoleSearch");
        Page currentPage = RequestParser.getPageFromSession(request, userService, filterParam);
        try {
            Collection<UserTO> users = userService.getAll(currentPage, filterParam);
            request.setAttribute("users", users);
            request.getSession().setAttribute("filter", filterParam);
            request.getSession().setAttribute("page", currentPage);
            log.info("page attributes {}", currentPage);
            log.info("filter attributes {}", filterParam);
            return ConfigurationManager.getProperty("path.page.users");
        } catch (Exception e) {
            log.error(e.getMessage());
            return ConfigurationManager.getProperty("path.page.error");
        }
    }
}
