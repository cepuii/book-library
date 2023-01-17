package ua.od.cepuii.library.command.implementation;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.od.cepuii.library.command.ActionCommand;
import ua.od.cepuii.library.dto.RequestParser;
import ua.od.cepuii.library.entity.enums.LoanStatus;
import ua.od.cepuii.library.resource.ConfigurationManager;
import ua.od.cepuii.library.service.LoanService;

public class SetOrderStatus implements ActionCommand {

    private static final Logger log = LoggerFactory.getLogger(SetOrderStatus.class);
    private final LoanService loanService = new LoanService();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        long loanId = RequestParser.getLong(request, "loanId");
        LoanStatus loanStatus = RequestParser.getLoanStatus(request);
        boolean b = loanService.setOrderStatus(loanId, loanStatus);
        log.info("set status: {} to loanId: {} in result: {} ", loanStatus, loanId, b);
        if (b) {
            return ConfigurationManager.getProperty("path.controller.orders") + "&" + ConfigurationManager.getProperty("path.success");
        }
        return ConfigurationManager.getProperty("path.controller.orders");
    }
}
