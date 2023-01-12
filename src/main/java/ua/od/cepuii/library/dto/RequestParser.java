package ua.od.cepuii.library.dto;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.od.cepuii.library.entity.Loan;
import ua.od.cepuii.library.entity.enums.LoanStatus;
import ua.od.cepuii.library.util.ValidationUtil;

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
        if (titleSearch != null && !titleSearch.isEmpty()) {
            bookFilterParam.setTitle(titleSearch);
        }
        if (authorSearch != null && !authorSearch.isEmpty()) {
            bookFilterParam.setAuthor(authorSearch);
        }
        if (orderBy != null && !orderBy.isEmpty()) {
            bookFilterParam.setOrderBy(orderBy);
        }
        if (descending != null && !descending.isEmpty()) {
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
}
