package ua.od.cepuii.library.filter;

import jakarta.servlet.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class EncodingFilter implements Filter {
    private static final Logger log = LoggerFactory.getLogger(EncodingFilter.class);
    private String encoding;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        encoding = filterConfig.getInitParameter("encoding");
        log.debug("initialize encodingFilter with param: {}", encoding);

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String reqEncoding = request.getCharacterEncoding();
        if (reqEncoding == null) {
            log.info("set character encoding: {}", encoding);
            request.setCharacterEncoding(encoding);
            response.setCharacterEncoding(encoding);
        }
        chain.doFilter(request, response);
    }
}
