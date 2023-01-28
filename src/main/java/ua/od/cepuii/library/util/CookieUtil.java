package ua.od.cepuii.library.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ua.od.cepuii.library.entity.User;

public class CookieUtil {
    private CookieUtil() {
    }

    public static void setStringToCookie(HttpServletResponse response, String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60 * 60 * 24);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public static void setUserToCookie(HttpServletResponse response, User user) {
        setStringToCookie(response, "userId", String.valueOf(user.getId()));
        setStringToCookie(response, "userEmail", user.getEmail());
        setStringToCookie(response, "userRole", user.getRole().name());
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

