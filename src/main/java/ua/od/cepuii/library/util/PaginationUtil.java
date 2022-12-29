package ua.od.cepuii.library.util;

import jakarta.servlet.http.HttpServletRequest;

public class PaginationUtil {


    private PaginationUtil() {
    }

    public static int getCurrentPage(HttpServletRequest request) {
        String currentPageString = request.getParameter("currentPage");
        int currentPage = 1;
        if (ValidationUtil.isInteger(currentPageString)) {
            currentPage = Integer.parseInt(currentPageString);
        }
        return currentPage;
    }
}
