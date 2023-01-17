package ua.od.cepuii.library.command.implementation;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.od.cepuii.library.command.ActionCommand;
import ua.od.cepuii.library.resource.ConfigurationManager;
import ua.od.cepuii.library.service.LoanService;

import java.sql.SQLException;

public class RemoveBookFromOrder implements ActionCommand {
    private static final Logger log = LoggerFactory.getLogger(RemoveBookFromOrder.class);
    LoanService loanService = new LoanService();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        long loanId = Long.parseLong(request.getParameter("loanId"));
        try {
            //TODO if false show message "cant delete";
            loanService.delete(loanId);
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return ConfigurationManager.getProperty("path.controller.orders");
    }
}
