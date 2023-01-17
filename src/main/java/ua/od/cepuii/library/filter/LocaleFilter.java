package ua.od.cepuii.library.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebFilter(urlPatterns = "/*")
public class LocaleFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(LocaleFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        String lang = req.getParameter("language");
        if (lang != null) {
            req.getSession().setAttribute("lang", lang);
            log.info("set locale to session: {}", lang);
            HttpServletResponse resp = (HttpServletResponse) response;
            resp.sendRedirect(req.getHeader("REFERER".toLowerCase()));
        } else {
            chain.doFilter(request, response);
        }
    }
}
