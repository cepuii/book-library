package ua.od.cepuii.library.command.reader;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.od.cepuii.library.command.ActionCommand;
import ua.od.cepuii.library.dto.RequestParser;
import ua.od.cepuii.library.entity.Loan;
import ua.od.cepuii.library.resource.ConfigurationManager;
import ua.od.cepuii.library.resource.MessageManager;
import ua.od.cepuii.library.service.LoanService;
import ua.od.cepuii.library.util.ValidationUtil;

import java.util.HashSet;
import java.util.Set;

public class AddBookToOrder implements ActionCommand {
    private static final Logger log = LoggerFactory.getLogger(AddBookToOrder.class);

    private final LoanService loanService = new LoanService();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        String days = request.getParameter("days");
        if (!ValidationUtil.isDigit(days)) {
            log.error("wrong duration");
            request.setAttribute("wrongDuration", MessageManager.getProperty("message.wrongDuration"));
            return ConfigurationManager.getProperty("path.page.main.forward");
        }
        try {
            Loan loan = RequestParser.getLoan(request);
            long loanCreate = loanService.create(loan);
            if (loanCreate != 0) {
                long bookId = Long.parseLong(request.getParameter("bookId"));
                String loanItems1 = "loanItems";
                if (session.getAttribute(loanItems1) == null) {
                    session.setAttribute(loanItems1, new HashSet<>());
                }
                @SuppressWarnings("unchecked")
                Set<Long> loanItems = (HashSet<Long>) session.getAttribute(loanItems1);
                log.info("add bookId: {} to session", bookId);
                loanItems.add(bookId);
                log.info("loan create: {}", loanCreate);
                session.setAttribute(loanItems1, loanItems);
            }
        } catch (NullPointerException e) {
            log.error(MessageManager.getProperty("message.somethingWrong"));
        }
        return ConfigurationManager.getProperty("path.controller.books");
    }
}