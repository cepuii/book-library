package ua.od.cepuii.library.command.librarian;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.od.cepuii.library.command.ActionCommand;
import ua.od.cepuii.library.constants.Path;
import ua.od.cepuii.library.context.AppContext;
import ua.od.cepuii.library.dto.Report;
import ua.od.cepuii.library.dto.RequestParser;
import ua.od.cepuii.library.entity.Loan;
import ua.od.cepuii.library.service.LoanService;

import static ua.od.cepuii.library.constants.AttributesName.REPORTS;
import static ua.od.cepuii.library.constants.AttributesName.SUBTRACT_FINE;

public class SetOrderStatus implements ActionCommand {

    private static final Logger log = LoggerFactory.getLogger(SetOrderStatus.class);
    private final LoanService loanService = AppContext.getInstance().getLoanService();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {

        Loan loan = RequestParser.getLoan(request);
        boolean fineSubtract = RequestParser.getBoolean(request, SUBTRACT_FINE);
        Report reports = loanService.setOrderStatus(loan, fineSubtract);

        request.getSession().setAttribute(REPORTS, reports);
        log.info("update loan {} ", reports);
        return Path.SHOW_ORDERS;
    }
}
