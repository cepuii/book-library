package ua.od.cepuii.library.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.od.cepuii.library.resource.MessageManager;

import java.io.IOException;
import java.util.Locale;

@WebFilter(urlPatterns = "/*")
public class LocaleFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(LocaleFilter.class);
    private static final Locale UA = new Locale("uk", "UA");

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String lang = req.getParameter("language");
        if (lang != null) {
            req.getSession().setAttribute("lang", lang);
            Cookie localeCookie = new Cookie("lang", lang);
            localeCookie.setMaxAge(60 * 60 * 24);
            localeCookie.setPath("/");
            resp.addCookie(localeCookie);
            if (lang.equals("uk")) {
                MessageManager.setResourceBundleLocale(UA);
            } else {
                MessageManager.setResourceBundleLocale(Locale.ENGLISH);
            }
            log.info("set locale to session: {}", lang);
            resp.sendRedirect(req.getHeader("REFERER".toLowerCase()));
        } else {
            chain.doFilter(request, response);
        }
    }
}
