package ua.od.cepuii.library.dto;

import jakarta.servlet.http.HttpServletRequest;
import ua.od.cepuii.library.util.ValidationUtil;

public class Wrapper {

    public static Page getCurrentPage(HttpServletRequest request) {
        String currentPageString = request.getParameter("currentPage");
        String noOfRecordsString = request.getParameter("noOfRecords");
        String lastPage = request.getParameter("lastPage");
        int currentPage = 0;
        int noOfRecords = 0;
        int pageAmount = 0;
        if (ValidationUtil.isInteger(currentPageString)) {
            currentPage = Integer.parseInt(currentPageString);
        }
        if (ValidationUtil.isInteger(noOfRecordsString)) {
            noOfRecords = Integer.parseInt(noOfRecordsString);
        }
        if (ValidationUtil.isInteger(lastPage)) {
            pageAmount = Integer.parseInt(lastPage);
        }
        return new Page.Builder()
                .currentPage(currentPage)
                .noOfRecords(noOfRecords)
                .pageAmount(pageAmount)
                .build();
    }
}
