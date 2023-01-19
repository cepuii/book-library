package ua.od.cepuii.library.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebFilter("/*")
public class AuthFilter implements Filter {
    private static final Logger log = LoggerFactory.getLogger(AuthFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        if (req.getSession().getAttribute("user") == null && req.getSession().getAttribute("logout") == null) {
            checkUserInCookie(req);
        }
        chain.doFilter(request, response);
    }

    private static void checkUserInCookie(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie ck : cookies) {
                if ("user".equals(ck.getName()) && !ck.getValue().isEmpty()) {
                    req.getSession().setAttribute("user", ck.getValue());
                } else if ("userId".equals(ck.getName()) && !ck.getValue().isEmpty()) {
                    req.getSession().setAttribute("userId", ck.getValue());
                } else if ("userRole".equals(ck.getName()) && !ck.getValue().isEmpty()) {
                    req.getSession().setAttribute("userRole", ck.getValue());
                }
            }
        }
    }
}