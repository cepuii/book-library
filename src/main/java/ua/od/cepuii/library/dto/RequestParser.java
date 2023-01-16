package ua.od.cepuii.library.dto;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.od.cepuii.library.entity.Author;
import ua.od.cepuii.library.entity.Book;
import ua.od.cepuii.library.entity.Loan;
import ua.od.cepuii.library.entity.enums.LoanStatus;
import ua.od.cepuii.library.entity.enums.PublicationType;
import ua.od.cepuii.library.exception.RequestParserException;
import ua.od.cepuii.library.resource.MessageManager;
import ua.od.cepuii.library.util.ValidationUtil;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

public class RequestParser {
    private static final Logger log = LoggerFactory.getLogger(RequestParser.class);

    private RequestParser() {
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
        if (ValidationUtil.isInteger(currentPage)) {
            page.setCurrentPage(Integer.parseInt(currentPage));
        }
        return page;
    }

    public static BookFilterParam getBookFilter(HttpServletRequest request) {
        BookFilterParam bookFilterParam = (BookFilterParam) request.getSession().getAttribute("filter");
        String titleSearch = request.getParameter("titleSearch");
        String authorSearch = request.getParameter("authorSearch");
        String orderBy = request.getParameter("orderBy");
        String descending = request.getParameter("descending");
        if (request.getParameter("cleanFilter") != null) {
            return BookFilterParam.cleanFilter();
        }
        if (bookFilterParam == null) {
            bookFilterParam = new BookFilterParam(titleSearch, authorSearch, orderBy, Boolean.parseBoolean(descending));
        }
        if (titleSearch != null) {
            bookFilterParam.setTitle(titleSearch);
        }
        if (authorSearch != null) {
            bookFilterParam.setAuthor(authorSearch);
        }
        if (orderBy != null) {
            bookFilterParam.setOrderBy(orderBy);
        }
        if (descending != null) {
            bookFilterParam.setDescending(Boolean.parseBoolean(descending));
        }
        return bookFilterParam;
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

    public static Book getBook(HttpServletRequest request) {
        return Book.builder()
                .id(Long.parseLong(request.getParameter("id")))
                .title(request.getParameter("title"))
                .publicationType(PublicationType.valueOf(request.getParameter("publicationType")))
                .datePublication(Integer.parseInt(request.getParameter("datePublication")))
                .total(Integer.parseInt(request.getParameter("total")))
                .authors(getAuthors(request))
                .build();
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

    public static long getLong(HttpServletRequest request, String paramName) {
        String stringParam = request.getParameter(paramName);
        if (ValidationUtil.isInteger(stringParam)) {
            return Long.parseLong(stringParam);
        }
        throw new RequestParserException(MessageManager.getProperty("message.wrongParam") + ": " + stringParam);
    }

    public static Author getNewAuthor(HttpServletRequest request) {
        return new Author(Objects.requireNonNull(request.getParameter("newAuthor")));
    }

    public static Author getAuthorFromString(String authorString) {
        String[] split = authorString.replace(',', '=').replaceAll("['}]", "").split("=");
        return new Author(Long.parseLong(split[1].strip()), split[3].strip());
    }
}
