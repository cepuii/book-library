package ua.od.cepuii.library.command;

import ua.od.cepuii.library.command.admin.*;
import ua.od.cepuii.library.command.common.*;
import ua.od.cepuii.library.command.librarian.SetOrderStatus;
import ua.od.cepuii.library.command.reader.AddBookToOrder;
import ua.od.cepuii.library.command.reader.RemoveBookFromOrder;
import ua.od.cepuii.library.command.unregister.*;

/**
 * The CommandEnum class represents a set of predefined commands available for use. Each command is associated
 * with an {@link ActionCommand} object.
 *
 * @author Sergei Chernousov
 * @version 1.0
 */
public enum CommandEnum {
    SHOW_BOOKS(new ShowBooks()),
    ADD_BOOK_TO_ORDER(new AddBookToOrder()),
    REMOVE_BOOK_FROM_ORDER(new RemoveBookFromOrder()),
    REMOVE_BOOK(new RemoveBook()),
    EDIT_BOOK(new EditBook()),
    SAVE_BOOK(new SaveBook()),
    SAVE_USER(new SaveLibrarian()),
    SAVE_USER_CHANGE(new SaveUserChange()),
    ADD_LIBRARIAN(new AddLibrarian()),
    LOGIN(new Login()),
    SHOW_LOGIN_PAGE(new ShowLoginPage()),
    SIGN_UP(new SignUp()),
    SHOW_SIGN_UP_PAGE(new ShowSignUpPage()),
    SHOW_ORDERS(new ShowOrders()),
    SHOW_USERS(new ShowUsers()),
    SHOW_PROFILE(new ShowProfile()),
    CHANGE_PASSWORD(new ChangePassword()),
    BLOCK_USER(new BlockUser()),
    SET_ORDER_STATUS(new SetOrderStatus()),
    VERIFY_TOKEN(new VerifyToken()),
    LOGOUT(new Logout());

    private final ActionCommand command;


    CommandEnum(ActionCommand command) {
        this.command = command;
    }

    public ActionCommand getCommand() {
        return this.command;
    }

}
