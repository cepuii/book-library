package ua.od.cepuii.library.util;

import ua.od.cepuii.library.dto.Report;
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

    public static Report validatePasswords(String oldPassword, String newPassword, String confirmPassword) {
        Report report = Report.newInstance();
        if (!validatePass(oldPassword)) {
            report.addError("badOldPassword", "message.signUp.password");
        } else if (!validatePass(newPassword)) {
            report.addError("badPassword", "message.signUp.password");
        } else if (oldPassword.equals(newPassword)) {
            report.addError("badPasswords", "message.change.password.same");
        } else if (!validatePass(confirmPassword)) {
            report.addError("badConfirm", "message.signUp.password.confirm");
        } else if (!confirmPassword.equals(newPassword)) {
            report.addError("badConfirm", "message.signUp.password.confirm");
        }
        return report;
    }
}
