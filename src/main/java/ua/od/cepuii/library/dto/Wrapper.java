package ua.od.cepuii.library.dto;

import jakarta.servlet.http.HttpServletRequest;
import ua.od.cepuii.library.util.ValidationUtil;

public class Wrapper {

    private Wrapper() {
    }

    public static Page getPageFromSession(HttpServletRequest request) {
        Page page = (Page) request.getSession().getAttribute("page");
        if (page == null) {
            page = new Page.Builder()
                    .currentPage(0)
                    .noOfRecords(0)
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
}
