package ua.od.cepuii.library.command.implementation;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.od.cepuii.library.command.ActionCommand;
import ua.od.cepuii.library.resource.ConfigurationManager;
import ua.od.cepuii.library.resource.MessageManager;
import ua.od.cepuii.library.util.ValidationUtil;

import java.util.HashSet;
import java.util.Set;

public class AddBookToOrder implements ActionCommand {
    private static final Logger log = LoggerFactory.getLogger(AddBookToOrder.class);
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        String days = request.getParameter("days");
        if (!ValidationUtil.isInteger(days)) {
            log.error("wrong duration");
            request.setAttribute("wrongDuration", MessageManager.getProperty("message.wrongDuration"));
            return ConfigurationManager.getProperty("path.page.main") + ConfigurationManager.getProperty("path.page.forward");
        }
        String loanItems1 = "loanItems";
        if (session.getAttribute(loanItems1) == null) {
            session.setAttribute(loanItems1, new HashSet<>());
        }
        try {
            Set<Long> loanItems = (HashSet<Long>) session.getAttribute(loanItems1);
            long bookId = Long.parseLong(request.getParameter("bookId"));
            loanItems.add(bookId);
            log.info(String.format("add bookId: %d to session", bookId));
            session.setAttribute(loanItems1, loanItems);
        } catch (NullPointerException e) {
            log.error(MessageManager.getProperty("message.somethingwrong"));
        }
        return ConfigurationManager.getProperty("path.page.main");
    }
}
