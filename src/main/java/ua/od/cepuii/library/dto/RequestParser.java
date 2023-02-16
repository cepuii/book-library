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
import ua.od.cepuii.library.service.Pageable;
import ua.od.cepuii.library.util.ValidationUtil;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

import static ua.od.cepuii.library.constants.AttributesName.*;

public class RequestParser {
    private static final Logger log = LoggerFactory.getLogger(RequestParser.class);

    private RequestParser() {
    }

    public static Page getPage(HttpServletRequest request, Pageable service, FilterParams filterParam) {
        Page page = getPage(request);
        int pageAmount = service.getPageAmount(page, filterParam);
        page.setPageAmount(pageAmount);
        return page;
    }

    public static Page getPage(HttpServletRequest request) {
        Page page = (Page) request.getAttribute(PAGE);
        if (page == null || request.getParameter(MODIFIED) != null) {
            page = Page.builder()
                    .currentPage(1)
                    .noOfRecords(5)
                    .build();
        }
        String currentPage = request.getParameter(CURRENT_PAGE);
        if (ValidationUtil.isDigit(currentPage)) {
            page.setCurrentPage(Integer.parseInt(currentPage));
        }
        return page;
    }

    public static FilterParams getFilterParams(HttpServletRequest request, String firstParam, String secondParam) {
        FilterParams filter = (FilterParams) request.getSession().getAttribute(FILTER);
        String firstValue = request.getParameter(firstParam);
        String secondValue = request.getParameter(secondParam);
        String orderBy = request.getParameter(ORDER_BY);
        String descending = request.getParameter(DESCENDING);
        if (request.getParameter(CLEAN_FILTER) != null) {
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
                .id(getLong(request, LOAN_ID))
                .userId(getLong(request, USER_ID))
                .bookId(getLong(request, BOOK_ID))
                .fine(getInt(request, FINE))
                .status(getStatus(request))
                .build();
        if (request.getParameter(DAYS) != null) {
            loan.setDuration(getInt(request, DAYS));
        }
        log.info("loan parse: {}", loan);
        return loan;
    }

    private static LoanStatus getStatus(HttpServletRequest request) {
        String loanStatus = request.getParameter(LOAN_STATUS);
        if (loanStatus == null) {
            loanStatus = request.getParameter(STATUS);
        }
        return loanStatus == null ? LoanStatus.RAW : LoanStatus.valueOf(loanStatus);
    }

    public static Book getBook(HttpServletRequest request) {
        return Book.builder()
                .id(getLong(request, BOOK_ID))
                .title(getString(request, TITLE))
                .publicationType(PublicationType.valueOf(request.getParameter(PUBLICATION_TYPE)))
                .datePublication(getInt(request, DATE_PUBLICATION))
                .total(getInt(request, TOTAL))
                .fine(getInt(request, FINE))
                .authors(getAuthors(request))
                .build();
    }

    private static String getString(HttpServletRequest request, String paramName) {
        String parameter = request.getParameter(paramName);
        return parameter == null ? "" : parameter;
    }

    private static int getInt(HttpServletRequest request, String paramName) {
        String stringParam = request.getParameter(paramName);
        if (ValidationUtil.isDigit(stringParam)) {
            return Integer.parseInt(stringParam);
        }
        return 0;
    }

    private static Collection<Author> getAuthors(HttpServletRequest request) {
        String[] authorIds = request.getParameterValues(AUTHOR_ID);
        String[] authorNames = request.getParameterValues(AUTHOR_NAME);
        Collection<Author> authors = new HashSet<>();
        if (authorIds != null) {
            for (int i = 0; i < authorIds.length; i++) {
                authors.add(new Author(Integer.parseInt(authorIds[i]), authorNames[i]));
            }
        }
        String[] newAuthors = request.getParameterValues(NEW_AUTHOR);
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
        long userId = getLong(request, USER_ID);
        String email = request.getParameter(EMAIL);
        if (email == null) {
            email = (String) request.getAttribute(EMAIL);
        }
        String password = request.getParameter(PASSWORD);
        if (password == null) {
            password = (String) request.getAttribute(PASSWORD);
        }
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
        String roleRequest = request.getParameter(ROLE);
        String roleSession = (String) request.getSession().getAttribute(USER_ROLE);
        if (roleRequest != null) {
            return Role.valueOf(roleRequest);
        } else if (roleSession != null) {
            return Role.valueOf(roleSession);
        }
        return Role.READER;
    }

    public static void setUserInfo(HttpServletRequest request, User user) {
        HttpSession session = request.getSession();
        session.setAttribute(USER_ID, user.getId());
        session.setAttribute(USER_EMAIL, user.getEmail());
        session.setAttribute(USER_ROLE, user.getRole().toString());

    }

    @SuppressWarnings("unchecked")
    public static void setMapFromSessionToRequest(HttpServletRequest request, String s) {
        Map<String, String> attribute = (Map<String, String>) request.getSession().getAttribute(s);
        if (attribute != null) {
            request.setAttribute(s, attribute);
            request.getSession().removeAttribute(s);
        }
    }

    public static String getParameterOrAttribute(HttpServletRequest request, String s) {
        String parameter = request.getParameter(s);
        String attribute = (String) request.getAttribute(s);
        return parameter == null ? attribute : parameter;
    }
}
