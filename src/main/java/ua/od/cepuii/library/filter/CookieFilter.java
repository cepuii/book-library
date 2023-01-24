package ua.od.cepuii.library.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

public class CookieFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        if (req.getSession().getAttribute("userEmail") == null && req.getSession().getAttribute("logout") == null) {
            checkUserInCookie(req);
        }
        chain.doFilter(request, response);
    }

    private static void checkUserInCookie(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie ck : cookies) {
                if ("userEmail".equals(ck.getName()) && !ck.getValue().isEmpty()) {
                    req.getSession().setAttribute("userEmail", ck.getValue());
                } else if ("userId".equals(ck.getName()) && !ck.getValue().isEmpty()) {
                    req.getSession().setAttribute("userId", ck.getValue());
                } else if ("userRole".equals(ck.getName()) && !ck.getValue().isEmpty()) {
                    req.getSession().setAttribute("userRole", ck.getValue());
                }
            }
        }
    }
}
