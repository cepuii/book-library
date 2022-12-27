package ua.od.cepuii.library.command.implementation;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ua.od.cepuii.library.command.ActionCommand;
import ua.od.cepuii.library.resource.ConfigurationManager;

import java.util.HashSet;
import java.util.Set;

public class RemoveBookFromOrder implements ActionCommand {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        String loanItems1 = "loanItems";
        Set<Long> loanItems = (HashSet<Long>) session.getAttribute(loanItems1);
        long bookId = Long.parseLong(request.getParameter("bookId"));
        loanItems.remove(bookId);
        session.setAttribute(loanItems1, loanItems);
        return ConfigurationManager.getProperty("path.page.main");
    }
}
