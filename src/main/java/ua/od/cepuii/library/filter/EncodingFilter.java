package ua.od.cepuii.library.filter;

import jakarta.servlet.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * EncodingFilter class is used to set the encoding for the request and response.
 * The encoding is set to the value specified in the filter's init parameter.
 *
 * @author Sergei Chernousov
 * @version 1.0
 */
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
