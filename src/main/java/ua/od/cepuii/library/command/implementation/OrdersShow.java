package ua.od.cepuii.library.command.implementation;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.od.cepuii.library.command.ActionCommand;
import ua.od.cepuii.library.dto.LoanTO;
import ua.od.cepuii.library.dto.Page;
import ua.od.cepuii.library.dto.RequestParser;
import ua.od.cepuii.library.resource.ConfigurationManager;
import ua.od.cepuii.library.service.LoanService;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;

public class OrdersShow implements ActionCommand {
    private static final Logger log = LoggerFactory.getLogger(OrdersShow.class);
    private final LoanService loanService = new LoanService();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        Page page = RequestParser.getPageFromSession(request);
        long userId = (long) request.getSession().getAttribute("userId");
        Collection<LoanTO> loans = null;
        try {
            log.info("load loans for user: {}", userId);
            loans = loanService.getAllByUserId(userId, page);
            log.info("loans {}", Arrays.toString(loans.toArray()));
        } catch (SQLException e) {
            log.info(e.getMessage());
            e.printStackTrace();
            return ConfigurationManager.getProperty("path.page.error");
        }
        request.setAttribute("loans", loans);
        return ConfigurationManager.getProperty("path.page.orders");
    }
}
