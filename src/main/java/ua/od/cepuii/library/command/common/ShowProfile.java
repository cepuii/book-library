package ua.od.cepuii.library.command.common;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.od.cepuii.library.command.ActionCommand;
import ua.od.cepuii.library.constants.Path;
import ua.od.cepuii.library.context.AppContext;
import ua.od.cepuii.library.dto.*;
import ua.od.cepuii.library.service.LoanService;
import ua.od.cepuii.library.service.UserService;

import java.util.Collection;

import static ua.od.cepuii.library.constants.AttributesName.*;
/**
 * This class is responsible for showing user`s profile information and orders history.
 *
 * @author Sergei Chernousov
 * @version 1.0
 */
public class ShowProfile implements ActionCommand {
    private static final Logger log = LoggerFactory.getLogger(ShowProfile.class);

    private final UserService userService = AppContext.getInstance().getUserService();
    private final LoanService loanService = AppContext.getInstance().getLoanService();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {

        long userId = RequestParser.getLong(request, USER_ID);
        UserTO user = userService.getById(userId);

        log.info("show user profile, {}", user);

        request.setAttribute(USER, user);

        //TODO finish search
        FilterParams filterParam = RequestParser.getFilterParams(request, "userSearch", "userRoleSearch");
        Page page = RequestParser.getPage(request, loanService, filterParam);
        Collection<LoanTO> loanHistory = loanService.getLoanHistory(userId, page);

        request.setAttribute(PAGE, page);
        request.setAttribute(LOANS, loanHistory);
        RequestParser.setMapFromSessionToRequest(request, "reports");

        return Path.PROFILE_PAGE_FORWARD;
    }
}
