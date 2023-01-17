package ua.od.cepuii.library.util;

import ua.od.cepuii.library.entity.AbstractEntity;
import ua.od.cepuii.library.entity.User;

public class ValidationUtil {


    public static boolean isInteger(String currentPageString) {
        return currentPageString != null && currentPageString.matches("[0-9]+");
    }

    public static boolean validateEmail(String email) {
        return true;
    }

    public static boolean validatePass(String password) {
        return true;
    }

    public static boolean isNew(AbstractEntity entity) {
        return entity.getId() == 0;
    }

    public static boolean validateUser(User user) {
        return true;
    }
}
