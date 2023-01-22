package ua.od.cepuii.library.command.librarian;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.od.cepuii.library.command.ActionCommand;
import ua.od.cepuii.library.dto.RequestParser;
import ua.od.cepuii.library.entity.Loan;
import ua.od.cepuii.library.resource.ConfigurationManager;
import ua.od.cepuii.library.service.LoanService;

public class SetOrderStatus implements ActionCommand {

    private static final Logger log = LoggerFactory.getLogger(SetOrderStatus.class);
    private final LoanService loanService = new LoanService();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        Loan loan = RequestParser.getLoan(request);
        boolean fineSubtract = RequestParser.getBoolean(request, "subtractFine");
        boolean b = loanService.setOrderStatus(loan, fineSubtract);
        log.info("update loan {} ", b);
        if (b) {
            return ConfigurationManager.getProperty("path.controller.orders.success");
        }
        return ConfigurationManager.getProperty("path.controller.orders");
    }
}