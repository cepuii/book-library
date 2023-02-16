package ua.od.cepuii.library.command.reader;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.od.cepuii.library.command.ActionCommand;
import ua.od.cepuii.library.constants.Path;
import ua.od.cepuii.library.context.AppContext;
import ua.od.cepuii.library.dto.Report;
import ua.od.cepuii.library.dto.RequestParser;
import ua.od.cepuii.library.service.LoanService;

import java.util.HashSet;
import java.util.Set;

import static ua.od.cepuii.library.constants.AttributesName.*;

/**
 * This class is responsible for removing book from the user order.
 *
 * @author Sergei Chernousov
 * @version 1.0
 */
public class RemoveBookFromOrder implements ActionCommand {
    private static final Logger log = LoggerFactory.getLogger(RemoveBookFromOrder.class);
    LoanService loanService = AppContext.getInstance().getLoanService();

    /**
     * The method gets the loan id and book id from the request, and uses the loan service to remove the book from the order.
     * It also removes the book from the loans stored in the session.
     *
     * @param request  the HTTP request from the servlet
     * @param response the HTTP response from the servlet
     * @return the path to the page that displays the orders
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        long loanId = RequestParser.getLong(request, LOAN_ID);
        long bookId = RequestParser.getLong(request, BOOK_ID);

        Report reports = loanService.delete(loanId, bookId);
        request.getSession().setAttribute(REPORTS, reports.getReports());
        log.info("delete book:{} from loans{}, reports {}", bookId, loanId, reports);

        removeFromLoansInSession(request, bookId);

        return Path.SHOW_ORDERS;
    }

    @SuppressWarnings("unchecked")
    private static void removeFromLoansInSession(HttpServletRequest request, long bookId) {
        HttpSession session = request.getSession();
        Set<Long> loanItems = (HashSet<Long>) session.getAttribute(LOAN_ITEMS);
        loanItems.remove(bookId);
        session.setAttribute(LOAN_ITEMS, loanItems);
    }
}
