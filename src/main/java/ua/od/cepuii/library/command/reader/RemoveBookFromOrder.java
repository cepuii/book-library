package ua.od.cepuii.library.command.reader;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.od.cepuii.library.command.ActionCommand;
import ua.od.cepuii.library.context.AppContext;
import ua.od.cepuii.library.dto.Report;
import ua.od.cepuii.library.dto.RequestParser;
import ua.od.cepuii.library.service.LoanService;
import ua.od.cepuii.library.util.PathManager;

import java.util.HashSet;
import java.util.Set;

import static ua.od.cepuii.library.constants.AttributesName.*;

public class RemoveBookFromOrder implements ActionCommand {
    private static final Logger log = LoggerFactory.getLogger(RemoveBookFromOrder.class);
    LoanService loanService = AppContext.getInstance().getLoanService();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        long loanId = RequestParser.getLong(request, LOAN_ID);
        long bookId = RequestParser.getLong(request, BOOK_ID);

        Report reports = loanService.delete(loanId, bookId);
        log.info("delete book:{} from loans{}, reports {}", bookId, loanId, reports);

        removeFromLoansInSession(request, bookId);

        return PathManager.getProperty("controller.orders");
    }

    @SuppressWarnings("unchecked")
    private static void removeFromLoansInSession(HttpServletRequest request, long bookId) {
        HttpSession session = request.getSession();
        Set<Long> loanItems = (HashSet<Long>) session.getAttribute(LOAN_ITEMS);
        loanItems.remove(bookId);
        session.setAttribute(LOAN_ITEMS, loanItems);
    }
}
