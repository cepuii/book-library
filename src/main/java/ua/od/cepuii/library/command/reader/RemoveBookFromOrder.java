package ua.od.cepuii.library.command.reader;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.od.cepuii.library.command.ActionCommand;
import ua.od.cepuii.library.dto.RequestParser;
import ua.od.cepuii.library.resource.ConfigurationManager;
import ua.od.cepuii.library.service.LoanService;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class RemoveBookFromOrder implements ActionCommand {
    private static final Logger log = LoggerFactory.getLogger(RemoveBookFromOrder.class);
    LoanService loanService = new LoanService();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        long loanId = Long.parseLong(request.getParameter("loanId"));
        try {
            //TODO if false show message "cant delete";
            loanService.delete(loanId);
            HttpSession session = request.getSession();
            Set<Long> loanItems = (HashSet<Long>) session.getAttribute("loanItems");
            long bookId = RequestParser.getLong(request, "bookId");
            loanItems.remove(bookId);
            session.setAttribute("loanItems", loanItems);
        } catch (SQLException e) {
            log.error(e.getMessage());

        }
        return ConfigurationManager.getProperty("path.controller.orders");
    }
}
