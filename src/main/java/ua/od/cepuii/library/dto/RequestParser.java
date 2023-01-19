package ua.od.cepuii.library.dto;

import jakarta.servlet.http.HttpServletRequest;
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
import ua.od.cepuii.library.resource.MessageManager;
import ua.od.cepuii.library.service.Service;
import ua.od.cepuii.library.util.ValidationUtil;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

public class RequestParser {
    private static final Logger log = LoggerFactory.getLogger(RequestParser.class);

    private RequestParser() {
    }

    public static Page getPageFromSession(HttpServletRequest request, Service service, FilterAndSortParams filterParam) {
        Page page = getPageFromSession(request);
//        if (request.getParameter("modified") != null) {
        int pageAmount = service.getPageAmount(page, filterParam);
        page.setPageAmount(pageAmount);
//        }
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

    public static FilterAndSortParams getFilterParams(HttpServletRequest request, String firstParam, String secondParam) {
        FilterAndSortParams filter = (FilterAndSortParams) request.getSession().getAttribute("filter");
        String firstValue = request.getParameter(firstParam);
        String secondValue = request.getParameter(secondParam);
        String orderBy = request.getParameter("orderBy");
        String descending = request.getParameter("descending");
        if (request.getParameter("cleanFilter") != null) {
            return FilterAndSortParams.cleanFilter();
        }
        if (filter == null) {
            filter = new FilterAndSortParams(firstValue, secondValue, orderBy, Boolean.parseBoolean(descending));
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
                .userId((long) request.getSession().getAttribute("userId"))
                .bookId(Long.parseLong(request.getParameter("bookId")))
                .duration(Integer.parseInt(request.getParameter("days")))
                .status(getStatus(request))
                .build();
        log.info("loan parse: {}", loan);
        return loan;
    }

    private static LoanStatus getStatus(HttpServletRequest request) {
        return request.getParameter("status") == null ? LoanStatus.RAW : LoanStatus.valueOf(request.getParameter("status"));
    }

    public static Book getBook(HttpServletRequest request) throws RequestParserException {
        return Book.builder()
                .id(getLong(request, "bookId"))
                .title(request.getParameter("title"))
                .publicationType(PublicationType.valueOf(request.getParameter("publicationType")))
                .datePublication(getInt(request, "datePublication"))
                .total(getInt(request, "total"))
                .fine(getInt(request, "fine"))
                .authors(getAuthors(request))
                .build();
    }

    private static int getInt(HttpServletRequest request, String paramName) throws RequestParserException {
        String stringParam = request.getParameter(paramName);
        if (ValidationUtil.isDigit(stringParam)) {
            return Integer.parseInt(stringParam);
        }
        throw new RequestParserException(MessageManager.getProperty("message.wrongParam") + ": " + stringParam);
    }

    private static Collection<Author> getAuthors(HttpServletRequest request) {
        String[] authorIds = request.getParameterValues("authorId");
        String[] authorNames = request.getParameterValues("authorName");
        Collection<Author> authors = new HashSet<>();
        for (int i = 0; i < authorIds.length; i++) {
            authors.add(new Author(Integer.parseInt(authorIds[i]), authorNames[i]));
        }
        return authors;
    }

    public static long getLong(HttpServletRequest request, String paramName) throws RequestParserException {
        String stringParam = request.getParameter(paramName);
        if (ValidationUtil.isDigit(stringParam)) {
            return Long.parseLong(stringParam);
        }
        return Long.parseLong(String.valueOf(request.getSession().getAttribute(paramName)));
    }

    public static Author getNewAuthor(HttpServletRequest request) {
        return new Author(Objects.requireNonNull(request.getParameter("newAuthor")));
    }

    public static Author getAuthorFromString(String authorString) {
        String[] split = authorString.replace(',', '=').replaceAll("['}]", "").split("=");
        return new Author(Long.parseLong(split[1].strip()), split[3].strip());
    }

    public static boolean getBoolean(HttpServletRequest request, String boolParam) {
        return Boolean.parseBoolean(request.getParameter(boolParam));
    }

    public static User getUser(HttpServletRequest request) {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        Role role = getRole(request);
        log.info("user: {}, {}", email, role);
        return User.builder()
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

    public static LoanStatus getLoanStatus(HttpServletRequest request) {
        String status = request.getParameter("loanStatus");
        return LoanStatus.valueOf(status);
    }


    public static void setUserInfo(HttpServletRequest request, User user) {
        request.getSession().setAttribute("userId", user.getId());
        request.getSession().setAttribute("user", user.getEmail());
        request.getSession().setAttribute("userRole", user.getRole().toString());
    }

}
