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

/**
 * This class is responsible for saving order status changes made to a user's orders in the system.
 *
 * @author Sergei Chernousov
 * @version 1.0
 */
public class SetOrderStatus implements ActionCommand {

    private static final Logger log = LoggerFactory.getLogger(SetOrderStatus.class);
    private final LoanService loanService = AppContext.getInstance().getLoanService();

    /**
     * Changes the status of a {@link Loan}. The status is determined by the passed {@code HttpServletRequest}
     * instance. If a fine was subtracted from the user account, this information is also passed in the request.
     *
     * @param request  the {@link HttpServletRequest} instance that contains information about the loan status change
     * @param response the {@link HttpServletResponse} instance
     * @return the path to the next page to show to the user
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {

        Loan loan = RequestParser.getLoan(request);
        boolean fineSubtract = RequestParser.getBoolean(request, SUBTRACT_FINE);
        Report report = loanService.setOrderStatus(loan, fineSubtract);

        request.getSession().setAttribute(REPORTS, report.getReports());
        log.info("update loan {} ", report);
        return Path.SHOW_ORDERS;
    }
}
