package ua.od.cepuii.library.command.common;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.od.cepuii.library.command.ActionCommand;
import ua.od.cepuii.library.dto.LoanTO;
import ua.od.cepuii.library.dto.Page;
import ua.od.cepuii.library.dto.RequestParser;
import ua.od.cepuii.library.dto.UserTO;
import ua.od.cepuii.library.resource.ConfigurationManager;
import ua.od.cepuii.library.service.LoanService;
import ua.od.cepuii.library.service.UserService;

import java.util.Collection;

public class ShowProfile implements ActionCommand {
    private static final Logger log = LoggerFactory.getLogger(ShowProfile.class);

    private final UserService userService = new UserService();
    private final LoanService loanService = new LoanService();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        long userId = RequestParser.getLong(request, "userId");
        UserTO user = userService.getById(userId);
        log.info("show user profile, {}", user);
        request.setAttribute("user", user);
        Page page = RequestParser.getPageFromSession(request);
        Collection<LoanTO> loanHistory = loanService.getLoanHistory(userId, page);
        request.setAttribute("loans", loanHistory);
        RequestParser.setFromSessionToRequest(request, "emailExist");
        RequestParser.setFromSessionToRequest(request, "success");
        return ConfigurationManager.getProperty("path.page.profile");
    }
}
