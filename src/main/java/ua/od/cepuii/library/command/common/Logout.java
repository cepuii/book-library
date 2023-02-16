package ua.od.cepuii.library.command.common;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ua.od.cepuii.library.command.ActionCommand;
import ua.od.cepuii.library.constants.AttributesName;
import ua.od.cepuii.library.constants.Path;
import ua.od.cepuii.library.util.CookieUtil;

import static ua.od.cepuii.library.constants.AttributesName.*;

/**
 * This class is responsible for cleaning up the cookie, invalidating the current session
 * and creating a new session with the language attribute set.
 *
 * @author Sergei Chernousov
 * @version 1.0
 */
public class Logout implements ActionCommand {

    /**
     * The execute method removes the cookie by calling {@link CookieUtil#cleanCookie(HttpServletRequest)},
     * invalidates the current session, creates a new session with attribute {@link  AttributesName#LOGOUT} set to {@link  AttributesName#TRUE}
     * and sets the language attribute from the previous session to the new session.
     *
     * @param request  the HttpServletRequest object.
     * @param response the HttpServletResponse object.
     * @return the path to the show books page.
     */
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
