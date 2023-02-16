package ua.od.cepuii.library.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ua.od.cepuii.library.entity.User;

import static ua.od.cepuii.library.constants.AttributesName.*;

public class CookieUtil {

    private static final int MAX_AGE = 60 * 60 * 24;

    private CookieUtil() {
    }

    public static void setStringToCookie(HttpServletResponse response, String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(MAX_AGE);
        response.addCookie(cookie);
    }

    public static void setUserToCookie(HttpServletResponse response, User user) {
        setStringToCookie(response, USER_ID, String.valueOf(user.getId()));
        setStringToCookie(response, USER_EMAIL, user.getEmail());
        setStringToCookie(response, USER_ROLE, user.getRole().name());
    }


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

