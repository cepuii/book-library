package ua.od.cepuii.library.constants;

/**
 * The `Path` class is a final class that contains string constants for various paths to controller with different commands
 * and pages. The attributes names are used in different parts of the application to identify and access request attributes.
 * This class is not meant to be instantiated, and its constructor is private.
 */
public class Path {
    public static final String INDEX_PAGE = "/index.jsp";
    public static final String LOGIN_PAGE = "/jsp/login.jsp";
    public static final String LOGIN_PAGE_FORWARD = "/jsp/login.jsp?forward=true";
    public static final String SIGN_UP_PAGE = "/jsp/signup.jsp";
    public static final String SIGN_UP_PAGE_FORWARD = "/jsp/signup.jsp?forward=true";
    public static final String MAIN_PAGE = "/jsp/main.jsp";
    public static final String PROFILE_PAGE = "/jsp/profile.jsp";
    public static final String PROFILE_PAGE_FORWARD = "/jsp/profile.jsp?forward=true";
    public static final String CHANGE_PASSWORD_PAGE = "/jsp/changePassword.jsp";
    public static final String USERS_PAGE = "/jsp/users.jsp";
    public static final String ADD_LIBRARIAN_PAGE = "jsp/addLibrarian.jsp";
    public static final String EDIT_BOOK_PAGE = "/jsp/editBook.jsp";
    public static final String ORDERS_PAGE = "/jsp/orders.jsp";
    public static final String ERROR_PAGE = "/jsp/error/error.jsp";
    public static final String SHOW_BOOKS = "/controller?command=show_books";
    public static final String SHOW_USERS = "/controller?command=show_users";
    public static final String SHOW_ORDERS = "/controller?command=show_orders";
    public static final String SHOW_PROFILE = "/controller?command=show_profile";
    public static final String SIGN_UP_FORWARD = "/controller?command=sign_up&forward=true";
    public static final String LOGIN = "/controller?command=login";
    public static final String LOGIN_FORWARD = "/controller?command=login&forward=true";
    public static final String CHANGE_PASSWORD = "/controller?command=change_password";
    public static final String ADD_LIBRARIAN_FORWARD = "/controller?command=add_librarian&forward=true";
    public static final String EDIT_BOOK_PAGE_FORWARD = "/jsp/editBook.jsp?forward=true";

    private Path() {
    }
}
