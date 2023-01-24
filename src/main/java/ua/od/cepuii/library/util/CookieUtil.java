package ua.od.cepuii.library.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ua.od.cepuii.library.entity.User;

public class CookieUtil {
    private CookieUtil() {
    }

    public static void setUserToCookie(HttpServletResponse response, User user) {
        Cookie cookie = new Cookie("userId", String.valueOf(user.getId()));
        cookie.setMaxAge(60 * 60 * 24);
        cookie.setPath("/");
        response.addCookie(cookie);
        cookie = new Cookie("userEmail", user.getEmail());
        cookie.setMaxAge(60 * 60 * 24);
        cookie.setPath("/");
        response.addCookie(cookie);
        cookie = new Cookie("userRole", user.getRole().name());
        cookie.setMaxAge(60 * 60 * 24);
        cookie.setPath("/");
        response.addCookie(cookie);
        cookie = new Cookie("userBlock", String.valueOf(user.isBlocked()));
        cookie.setMaxAge(60 * 60 * 24);
        cookie.setPath("/");
        response.addCookie(cookie);
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

