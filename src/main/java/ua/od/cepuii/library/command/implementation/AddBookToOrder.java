package ua.od.cepuii.library.command.implementation;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ua.od.cepuii.library.command.ActionCommand;
import ua.od.cepuii.library.resource.ConfigurationManager;

import java.util.HashSet;
import java.util.Set;

public class AddBookToOrder implements ActionCommand {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        String loanItems1 = "loanItems";
        if (session.getAttribute(loanItems1) == null) {
            session.setAttribute(loanItems1, new HashSet<>());
        }
        try {
            Set<Long> loanItems = (HashSet<Long>) session.getAttribute(loanItems1);
            loanItems.add(Long.parseLong(request.getParameter("bookId")));
            session.setAttribute(loanItems1, loanItems);
        } catch (NullPointerException e) {
            System.out.println(session.getAttribute(loanItems1));
        }
        return ConfigurationManager.getProperty("path.page.main");
    }
}
