package ua.od.cepuii.library.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.od.cepuii.library.entity.enums.Role;
import ua.od.cepuii.library.resource.ConfigurationManager;
import ua.od.cepuii.library.resource.MessageManager;

import java.io.IOException;
import java.util.*;

public class SecurityFilter implements Filter {
    private static final Logger log = LoggerFactory.getLogger(SecurityFilter.class);

    private static final Map<Role, List<String>> accessMap = new EnumMap<>(Role.class);
    private static List<String> common = new ArrayList<>();
    private static List<String> unregister = new ArrayList<>();

    @Override
    public void init(FilterConfig config) {
        log.debug("Filter initialization starts");

        initAccessLists(config);

        log.debug("Filter initialization finished");
    }

    private void initAccessLists(FilterConfig config) {
        accessMap.put(Role.ADMIN, asList(config.getInitParameter("admin")));
        accessMap.put(Role.READER, asList(config.getInitParameter("reader")));
        accessMap.put(Role.LIBRARIAN, asList(config.getInitParameter("librarian")));

        common = asList(config.getInitParameter("common"));

        unregister = asList(config.getInitParameter("unregister"));
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (accessAllowed(request)) {
            log.info("access allow");
            chain.doFilter(request, response);
        } else {
            log.info("access deny");
            HttpServletRequest req = (HttpServletRequest) request;
            req.getSession().setAttribute("wrongAction", MessageManager.getProperty("message.access.deny"));
            request.getRequestDispatcher(ConfigurationManager.getProperty("path.controller.books")).forward(request, response);
        }
    }

    private boolean accessAllowed(ServletRequest request) {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        String commandName = request.getParameter("command");
        if (commandName == null || commandName.isEmpty()) {
            return false;
        }

        if (unregister.contains(commandName)) {
            return true;
        }

        HttpSession session = httpRequest.getSession(false);
        if (session == null) {
            return false;
        }

        Role userRole = Role.valueOf((String) session.getAttribute("userRole"));
        if (userRole == null) {
            return false;
        }

        return accessMap.get(userRole).contains(commandName) || common.contains(commandName);
    }

    private List<String> asList(String param) {
        List<String> list = new ArrayList<>();
        StringTokenizer st = new StringTokenizer(param);
        while (st.hasMoreTokens()) {
            list.add(st.nextToken());
        }
        return list;
    }

}