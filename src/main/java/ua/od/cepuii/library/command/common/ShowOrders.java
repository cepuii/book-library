package ua.od.cepuii.library.command.common;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.od.cepuii.library.command.ActionCommand;
import ua.od.cepuii.library.context.AppContext;
import ua.od.cepuii.library.dto.FilterParams;
import ua.od.cepuii.library.dto.LoanTO;
import ua.od.cepuii.library.dto.Page;
import ua.od.cepuii.library.dto.RequestParser;
import ua.od.cepuii.library.entity.enums.Role;
import ua.od.cepuii.library.resource.ConfigurationManager;
import ua.od.cepuii.library.service.LoanService;

import java.util.Collection;

import static ua.od.cepuii.library.dto.RequestParser.*;

public class ShowOrders implements ActionCommand {
    private static final Logger log = LoggerFactory.getLogger(ShowOrders.class);
    private final LoanService loanService = AppContext.getInstance().getLoanService();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        Page page = getPageFromSession(request);
        FilterParams filter = getFilterParams(request, "", "");
        Role userRole = RequestParser.getRole(request);
        Collection<LoanTO> loans;
        if (Role.LIBRARIAN == userRole) {
            loans = loanService.getAll(filter, page);
        } else {
            long userId = getLong(request, "userId");
            loans = loanService.getAllByUserId(userId, page);
            log.info("load loans for user: {}", userId);
        }
        log.info("loans {}", loans.size());
        request.setAttribute("loans", loans);
        return ConfigurationManager.getProperty("path.page.orders");
    }
}
