package ua.od.cepuii.library.command.reader;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.od.cepuii.library.command.ActionCommand;
import ua.od.cepuii.library.context.AppContext;
import ua.od.cepuii.library.dto.RequestParser;
import ua.od.cepuii.library.service.LoanService;
import ua.od.cepuii.library.util.PathManager;

import java.util.HashSet;
import java.util.Set;

public class RemoveBookFromOrder implements ActionCommand {
    private static final Logger log = LoggerFactory.getLogger(RemoveBookFromOrder.class);
    LoanService loanService = AppContext.getInstance().getLoanService();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        long loanId = RequestParser.getLong(request, "loanId");
        long bookId = RequestParser.getLong(request, "bookId");

        try {
            //TODO if false show message "cant delete";
            loanService.delete(loanId, bookId);
            HttpSession session = request.getSession();
            Set<Long> loanItems = (HashSet<Long>) session.getAttribute("loanItems");
            loanItems.remove(bookId);
            session.setAttribute("loanItems", loanItems);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return PathManager.getProperty("controller.orders");
    }
}
