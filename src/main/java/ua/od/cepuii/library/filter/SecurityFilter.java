package ua.od.cepuii.library.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.od.cepuii.library.constants.Path;
import ua.od.cepuii.library.context.AppContext;
import ua.od.cepuii.library.dto.Report;
import ua.od.cepuii.library.dto.RequestParser;
import ua.od.cepuii.library.entity.enums.Role;
import ua.od.cepuii.library.service.UserService;

import java.io.IOException;
import java.util.*;

import static ua.od.cepuii.library.constants.AttributesName.*;

/**
 * SecurityFilter is a Servlet filter that enforces the access control of different commands based on user's role.
 *
 * @author Sergei Chernousov
 * @version 1.0
 */
public class SecurityFilter implements Filter {
    private static final Logger log = LoggerFactory.getLogger(SecurityFilter.class);
    private static final Map<Role, List<String>> accessMap = new EnumMap<>(Role.class);
    private static final String COMMON = "common";
    private static final String UNREGISTER = "unregister";
    private static final String ADMIN = "admin";
    private static final String READER = "reader";
    private static final String LIBRARIAN = "librarian";
    private static List<String> commonCommandList = new ArrayList<>();
    private static List<String> unregisterCommandList = new ArrayList<>();

    private UserService userService;

    @Override
    public void init(FilterConfig config) {
        log.debug("Filter initialization starts");

        initAccessLists(config);
        userService = AppContext.getInstance().getUserService();

        log.debug("Filter initialization finished");
    }

    private void initAccessLists(FilterConfig config) {
        accessMap.put(Role.ADMIN, asList(config.getInitParameter(ADMIN)));
        accessMap.put(Role.READER, asList(config.getInitParameter(READER)));
        accessMap.put(Role.LIBRARIAN, asList(config.getInitParameter(LIBRARIAN)));

        commonCommandList = asList(config.getInitParameter(COMMON));

        unregisterCommandList = asList(config.getInitParameter(UNREGISTER));
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (accessAllowed(request)) {
            log.info("access allow");
            chain.doFilter(request, response);
        } else {
            log.info("access deny");
            HttpServletRequest req = (HttpServletRequest) request;
            req.getSession().setAttribute(REPORTS, Report.newInstance(WRONG_ACTION, "message.access.deny"));
            req.getRequestDispatcher(Path.SHOW_PROFILE).forward(request, response);
        }
    }

    private boolean accessAllowed(ServletRequest request) {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        String commandName = request.getParameter(COMMAND);
        if (commandName == null || commandName.isEmpty()) {
            return false;
        }

        if (commandName.equals(LOGOUT)) {
            return true;
        }

        long userId = RequestParser.getLong(httpRequest, USER_ID);
        if (userId != 0 && userService.getById(userId).isBlocked()) {
            return false;
        }

        if (unregisterCommandList.contains(commandName)) {
            return true;
        }


        HttpSession session = httpRequest.getSession(false);
        if (session == null) {
            return false;
        }


        Role userRole = Role.valueOf((String) session.getAttribute(USER_ROLE));

        return accessMap.get(userRole).contains(commandName) || commonCommandList.contains(commandName);
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