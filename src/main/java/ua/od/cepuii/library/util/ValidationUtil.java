package ua.od.cepuii.library.util;

public class ValidationUtil {


    public static boolean isInteger(String currentPageString) {
        return currentPageString != null && currentPageString.matches("[0-9]+");
    }
}
