package ua.od.cepuii.library.dto;

import jakarta.servlet.http.HttpServletRequest;
import ua.od.cepuii.library.util.ValidationUtil;

public class Wrapper {

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
}
