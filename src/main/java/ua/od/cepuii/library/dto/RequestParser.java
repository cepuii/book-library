package ua.od.cepuii.library.dto;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.od.cepuii.library.entity.Author;
import ua.od.cepuii.library.entity.Book;
import ua.od.cepuii.library.entity.Loan;
import ua.od.cepuii.library.entity.User;
import ua.od.cepuii.library.entity.enums.LoanStatus;
import ua.od.cepuii.library.entity.enums.PublicationType;
import ua.od.cepuii.library.entity.enums.Role;
import ua.od.cepuii.library.exception.RequestParserException;
import ua.od.cepuii.library.service.Service;
import ua.od.cepuii.library.util.PasswordUtil;
import ua.od.cepuii.library.util.ValidationUtil;

import java.util.Collection;
import java.util.HashSet;

public class RequestParser {
    private static final Logger log = LoggerFactory.getLogger(RequestParser.class);

    private RequestParser() {
    }

    public static Page getPageFromSession(HttpServletRequest request, Service service, FilterParams filterParam) {
        Page page = getPageFromSession(request);
        if (request.getParameter("modified") != null) {
            page.setCurrentPage(1);
        }
        int pageAmount = service.getPageAmount(page, filterParam);
        page.setPageAmount(pageAmount);
        return page;
    }

    public static Page getPageFromSession(HttpServletRequest request) {
        Page page = (Page) request.getSession().getAttribute("page");
        if (page == null) {
            page = Page.builder()
                    .currentPage(1)
                    .noOfRecords(5)
                    .build();
        }
        String currentPage = request.getParameter("currentPage");
        if (ValidationUtil.isDigit(currentPage)) {
            page.setCurrentPage(Integer.parseInt(currentPage));
        }
        return page;
    }

    public static FilterParams getFilterParams(HttpServletRequest request, String firstParam, String secondParam) {
        FilterParams filter = (FilterParams) request.getSession().getAttribute("filter");
        String firstValue = request.getParameter(firstParam);
        String secondValue = request.getParameter(secondParam);
        String orderBy = request.getParameter("orderBy");
        String descending = request.getParameter("descending");
        if (request.getParameter("cleanFilter") != null) {
            return FilterParams.cleanFilter();
        }
        if (filter == null) {
            filter = new FilterParams(firstValue, secondValue, orderBy, Boolean.parseBoolean(descending));
        }
        if (firstValue != null) {
            filter.setFirstParam(firstValue);
        }
        if (secondValue != null) {
            filter.setSecondParam(secondValue);
        }
        if (orderBy != null) {
            filter.setOrderBy(orderBy);
        }
        if (descending != null) {
            filter.setDescending(Boolean.parseBoolean(descending));
        }
        return filter;
    }

    public static Loan getLoan(HttpServletRequest request) {
        Loan loan = Loan.builder()
                .id(getLong(request, "loanId"))
                .userId(getLong(request, "userId"))
                .bookId(getLong(request, "bookId"))
                .fine(getInt(request, "fine"))
                .status(getStatus(request))
                .build();
        if (request.getParameter("days") != null) {
            loan.setDuration(getInt(request, "days"));
        }
        log.info("loan parse: {}", loan);
        return loan;
    }

    private static LoanStatus getStatus(HttpServletRequest request) {
        String loanStatus = request.getParameter("loanStatus");
        if (loanStatus == null) {
            loanStatus = request.getParameter("status");
        }
        return loanStatus == null ? LoanStatus.RAW : LoanStatus.valueOf(loanStatus);
    }

    public static Book getBook(HttpServletRequest request) throws RequestParserException {
        return Book.builder()
                .id(getLong(request, "bookId"))
                .title(getString(request, "title"))
                .publicationType(PublicationType.valueOf(request.getParameter("publicationType")))
                .datePublication(getInt(request, "datePublication"))
                .total(getInt(request, "total"))
                .fine(getInt(request, "fine"))
                .authors(getAuthors(request))
                .build();
    }

    private static String getString(HttpServletRequest request, String title) {
        String parameter = request.getParameter(title);
        return parameter == null ? "" : parameter;
    }

    private static int getInt(HttpServletRequest request, String paramName) throws RequestParserException {
        String stringParam = request.getParameter(paramName);
        if (ValidationUtil.isDigit(stringParam)) {
            return Integer.parseInt(stringParam);
        }
        return 0;
    }

    private static Collection<Author> getAuthors(HttpServletRequest request) {
        String[] authorIds = request.getParameterValues("authorId");
        String[] authorNames = request.getParameterValues("authorName");
        Collection<Author> authors = new HashSet<>();
        if (authorIds != null) {
            for (int i = 0; i < authorIds.length; i++) {
                authors.add(new Author(Integer.parseInt(authorIds[i]), authorNames[i]));
            }
        }
        String[] newAuthors = request.getParameterValues("newAuthor");
        if (newAuthors != null) {
            for (String newAuthor : newAuthors) {
                authors.add(new Author(newAuthor));
            }
        }
        return authors;
    }

    public static long getLong(HttpServletRequest request, String paramName) {
        String stringParam = request.getParameter(paramName);
        if (ValidationUtil.isDigit(stringParam)) {
            return Long.parseLong(stringParam);
        }
        String value = String.valueOf(request.getSession().getAttribute(paramName));
        if (ValidationUtil.isDigit(value)) {
            return Long.parseLong(value);
        }
        return 0;
    }

    public static boolean getBoolean(HttpServletRequest request, String boolParam) {
        return Boolean.parseBoolean(request.getParameter(boolParam));
    }

    public static User getUser(HttpServletRequest request) {
        long userId = getLong(request, "userId");
        String email = request.getParameter("email");
        String password = PasswordUtil.getHash(request.getParameter("password").getBytes());
        Role role = getRole(request);
        log.info("user: {}, {}", email, role);
        return User.builder()
                .id(userId)
                .email(email)
                .password(password)
                .role(role)
                .build();
    }

    public static Role getRole(HttpServletRequest request) {
        String roleRequest = request.getParameter("role");
        String roleSession = (String) request.getSession().getAttribute("userRole");
        if (roleRequest != null) {
            return Role.valueOf(roleRequest);
        } else if (roleSession != null) {
            return Role.valueOf(roleSession);
        }
        return Role.READER;
    }

    public static void setUserInfo(HttpServletRequest request, User user) {
        HttpSession session = request.getSession();
        session.setAttribute("userId", user.getId());
        session.setAttribute("userEmail", user.getEmail());
        session.setAttribute("userRole", user.getRole().toString());

    }

    public static void setFromSessionToRequest(HttpServletRequest request, String s) {
        request.setAttribute(s, request.getSession().getAttribute(s));
        request.getSession().removeAttribute(s);
    }
}
