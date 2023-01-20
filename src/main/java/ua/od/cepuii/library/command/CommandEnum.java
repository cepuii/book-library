package ua.od.cepuii.library.command;

import ua.od.cepuii.library.command.admin.*;
import ua.od.cepuii.library.command.common.*;
import ua.od.cepuii.library.command.librarian.SetOrderStatus;
import ua.od.cepuii.library.command.reader.AddBookToOrder;
import ua.od.cepuii.library.command.reader.RemoveBookFromOrder;
import ua.od.cepuii.library.command.unregisted.Login;
import ua.od.cepuii.library.command.unregisted.ShowBooks;
import ua.od.cepuii.library.command.unregisted.SignUp;

public enum CommandEnum {
    SHOW_BOOKS(new ShowBooks()),
    ADD_BOOK_TO_ORDER(new AddBookToOrder()),
    REMOVE_BOOK_FROM_ORDER(new RemoveBookFromOrder()),
    REMOVE_BOOK(new RemoveBook()),
    EDIT_BOOK(new EditBook()),
    SAVE_BOOK(new SaveBook()),
    SAVE_USER(new SaveLibrarian()),
    SAVE_USER_CHANGE(new SaveUserChange()),
    ADD_AUTHOR(new AddAuthor()),
    ADD_LIBRARIAN(new AddLibrarian()),
    LOGIN(new Login()),
    SIGN_UP(new SignUp()),
    SHOW_ORDERS(new ShowOrders()),
    SHOW_USERS(new ShowUsers()),
    SHOW_PROFILE(new ShowProfile()),
    CHANGE_PASSWORD(new ChangePassword()),
    BLOCK_USER(new BlockUser()),
    SET_ORDER_STATUS(new SetOrderStatus()),
    LOGOUT(new Logout());

    private final ActionCommand command;


    CommandEnum(ActionCommand command) {
        this.command = command;
    }

    public ActionCommand getCommand() {
        return this.command;
    }

}
