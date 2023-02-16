package ua.od.cepuii.library.util;

import ua.od.cepuii.library.constants.AttributesName;
import ua.od.cepuii.library.dto.Report;
import ua.od.cepuii.library.entity.AbstractEntity;
import ua.od.cepuii.library.entity.User;

/**
 * A utility class that provides methods for validating user input, passwords, and entity attributes.
 *
 * @author Sergei Chernousov
 * @version 1.0
 */
public class ValidationUtil {
    private static final String EMAIL_REGEX = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$";
    private static final String PASSWORD_REGEX = "^(?=.*[a-zA-Z])(?=.*[0-9]).{4,20}$";
    private static final String DIGITAL_REGEX = "[0-9]+";
    private static final String MESSAGE_SIGN_UP_PASSWORD = "message.signUp.password";
    private static final String MESSAGE_SIGN_UP_EMAIL = "message.signUp.email";
    private static final String MESSAGE_CHANGE_PASSWORD_SAME = "message.change.password.same";

    private ValidationUtil() {

    }

    private static boolean validateString(String string, String regex) {
        return string != null && string.matches(regex);
    }

    /**
     * Checks if the given string contains only digits.
     *
     * @param digitString a string to check
     * @return {@code true} if the string contains only digits; {@code false} otherwise
     */
    public static boolean isDigit(String digitString) {
        return validateString(digitString, DIGITAL_REGEX);
    }

    /**
     * Checks if the given email address is valid.
     *
     * @param email an email address to check
     * @return {@code true} if the email is valid; {@code false} otherwise
     */
    public static boolean validateEmail(String email) {
        return validateString(email, EMAIL_REGEX);
    }

    /**
     * Checks if the given password is invalid according to the password regular expression.
     *
     * @param password a password to check
     * @return {@code true} if the password is invalid; {@code false} otherwise
     */
    public static boolean isPassInvalid(String password) {
        return !validateString(password, PASSWORD_REGEX);
    }

    /**
     * Checks if the given entity is new (has an ID of 0).
     *
     * @param entity an entity to check
     * @return {@code true} if the entity is new; {@code false} otherwise
     */
    public static boolean isNew(AbstractEntity entity) {
        return entity.getId() == 0;
    }

    /**
     * Validates the given user object.
     *
     * @param user a user object to validate
     * @return a {@code Report} object containing validation errors, if any
     */
    public static Report validateUser(User user) {
        Report report = Report.newInstance();
        if (ValidationUtil.isPassInvalid(user.getPassword())) {
            report.addError(AttributesName.BAD_PASSWORD, MESSAGE_SIGN_UP_PASSWORD);
        }
        if (!ValidationUtil.validateEmail(user.getEmail())) {
            report.addError(AttributesName.BAD_EMAIL, MESSAGE_SIGN_UP_EMAIL);
        }
        return report;
    }

    /**
     * Validates the given old and new passwords.
     *
     * @param oldPassword the old password to validate
     * @param newPassword the new password to validate
     * @return a {@code Report} object containing validation errors, if any
     */
    public static Report validatePasswords(String oldPassword, String newPassword) {
        Report report = Report.newInstance();
        if (isPassInvalid(oldPassword)) {
            report.addError(AttributesName.BAD_OLD_PASSWORD, MESSAGE_SIGN_UP_PASSWORD);
        } else if (isPassInvalid(newPassword)) {
            report.addError(AttributesName.BAD_PASSWORD, MESSAGE_SIGN_UP_PASSWORD);
        } else if (oldPassword.equals(newPassword)) {
            report.addError(AttributesName.BAD_PASSWORDS, MESSAGE_CHANGE_PASSWORD_SAME);
        }
        return report;
    }
}
