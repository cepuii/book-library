package ua.od.cepuii.library.command.common;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ua.od.cepuii.library.command.ActionCommand;
import ua.od.cepuii.library.constants.Path;
import ua.od.cepuii.library.util.CookieUtil;

import static ua.od.cepuii.library.constants.AttributesName.*;

public class Logout implements ActionCommand {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {

        CookieUtil.cleanCookie(request);
        String lang = (String) request.getSession().getAttribute(LANG);

        request.getSession().invalidate();

        request.getSession(true).setAttribute(LOGOUT, TRUE);
        request.getSession().setAttribute(LANG, lang);

        return Path.SHOW_BOOKS;
    }

}
