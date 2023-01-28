package ua.od.cepuii.library.util;

import ua.od.cepuii.library.entity.AbstractEntity;
import ua.od.cepuii.library.entity.User;

public class ValidationUtil {
    private static final String EMAIL_REGEX = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$";
    private static final String PASSWORD_REGEX = "^(?=.*[a-zA-Z])(?=.*[0-9]).{4,20}$";
    private static final String DIGITAL_REGEX = "[0-9]+";

    private ValidationUtil() {

    }

    private static boolean validateString(String string, String regex) {
        return string != null && string.matches(regex);
    }

    public static boolean isDigit(String digitString) {
        return validateString(digitString, DIGITAL_REGEX);
    }

    public static boolean validateEmail(String email) {
        return validateString(email, EMAIL_REGEX);
    }

    public static boolean validatePass(String password) {
        return validateString(password, PASSWORD_REGEX);
    }

    public static boolean isNew(AbstractEntity entity) {
        return entity.getId() == 0;
    }

    public static boolean validateUser(User user) {
        return validateEmail(user.getEmail()) && validatePass(user.getPassword());
    }
}
