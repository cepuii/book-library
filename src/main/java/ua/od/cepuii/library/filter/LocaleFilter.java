package ua.od.cepuii.library.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.od.cepuii.library.constants.AttributesName;

import java.io.IOException;

import static ua.od.cepuii.library.constants.AttributesName.LANG;
import static ua.od.cepuii.library.constants.AttributesName.REFERER;

/**
 * This filter is mapped to all the URLs in the application and is responsible for checking if a locale is set in the request parameters.
 * If it is, it sets the locale in the session and adds a cookie with the locale to the response.
 * If the locale is not set in the request parameters, it checks for the presence of a cookie with the locale
 * and sets it in the session if found.
 *
 * @author Sergei Chernousov
 * @version 1.0
 */
@WebFilter(urlPatterns = "/*")
public class LocaleFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(LocaleFilter.class);
    private static final int MAX_AGE = 60 * 60 * 24;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String lang = req.getParameter(AttributesName.LANGUAGE);
        if (lang != null) {
            req.getSession().setAttribute(LANG, lang);
            Cookie localeCookie = new Cookie(LANG, lang);
            localeCookie.setMaxAge(MAX_AGE);
            resp.addCookie(localeCookie);
            log.info("set locale to session: {}", lang);
            resp.sendRedirect(req.getHeader(REFERER));
        } else {
            checkLocaleInCookie(req);
            chain.doFilter(request, response);
        }
    }


    private void checkLocaleInCookie(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie ck : cookies) {
                if (LANG.equals(ck.getName()) && !ck.getValue().isEmpty()) {
                    String lang = ck.getValue();
                    req.getSession().setAttribute(LANG, lang);
                }
            }
        }
    }
}
