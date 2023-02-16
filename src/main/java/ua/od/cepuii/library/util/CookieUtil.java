package ua.od.cepuii.library.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ua.od.cepuii.library.entity.User;

import static ua.od.cepuii.library.constants.AttributesName.*;

/**
 * A utility class for working with HTTP cookies.
 *
 * @author Sergei Chernousov
 * @version 1.0
 */
public class CookieUtil {
    // Max age of cookies in seconds (24 hours)
    private static final int MAX_AGE = 60 * 60 * 24;

    private CookieUtil() {
    }

    /**
     * Sets a string value to a cookie with the given key and adds it to the response.
     *
     * @param response the HTTP servlet response to add the cookie to
     * @param key      the key of the cookie
     * @param value    the string value to set
     */
    public static void setStringToCookie(HttpServletResponse response, String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(MAX_AGE);
        response.addCookie(cookie);
    }

    /**
     * Sets the user's ID, email, and role to cookies and adds them to the response.
     *
     * @param response the HTTP servlet response to add the cookies to
     * @param user     the user to set the cookies for
     */
    public static void setUserToCookie(HttpServletResponse response, User user) {
        setStringToCookie(response, USER_ID, String.valueOf(user.getId()));
        setStringToCookie(response, USER_EMAIL, user.getEmail());
        setStringToCookie(response, USER_ROLE, user.getRole().name());
    }

    /**
     * Deletes all cookies from the given HTTP servlet request.
     *
     * @param request the HTTP servlet request to delete the cookies from
     */
    public static void cleanCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie ck : cookies) {
                ck.setValue("");
                ck.setMaxAge(0);
            }
        }
    }
}

