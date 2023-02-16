package ua.od.cepuii.library.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

import static ua.od.cepuii.library.constants.AttributesName.*;

/**
 * Filter class that is used to check user information in cookies and if this information exist, authorize user and redirect
 * to previous page.
 *
 * @author Sergei Chernousov
 * @version 1.0
 */
public class CookieFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        if (req.getSession().getAttribute(USER_EMAIL) == null && req.getSession().getAttribute(LOGOUT) == null) {
            checkUserInCookie(req);
        }
        chain.doFilter(request, response);
    }



    private void checkUserInCookie(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie ck : cookies) {
                if (USER_EMAIL.equals(ck.getName()) && !ck.getValue().isEmpty()) {
                    req.getSession().setAttribute(USER_EMAIL, ck.getValue());
                } else if (USER_ID.equals(ck.getName()) && !ck.getValue().isEmpty()) {
                    req.getSession().setAttribute(USER_ID, ck.getValue());
                } else if (USER_ROLE.equals(ck.getName()) && !ck.getValue().isEmpty()) {
                    req.getSession().setAttribute(USER_ROLE, ck.getValue());
                }
            }
        }
    }
}
