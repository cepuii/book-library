package ua.od.cepuii.library.command;

import ua.od.cepuii.library.command.implementation.*;

public enum CommandEnum {
    EMPTY_COMMAND(new GetAllBooks()),
    ADD_BOOK_TO_ORDER(new AddBookToOrder()),
    REMOVE_BOOK_FROM_ORDER(new RemoveBookFromOrder()),
    REMOVE_BOOK(new RemoveBook()),
    EDIT_BOOK(new EditBook()),
    SAVE_BOOK(new SaveBook()),
    SAVE_USER(new SaveUser()),
    ADD_AUTHOR(new AddAuthor()),
    ADD_LIBRARIAN(new AddLibrarian()),
    LOGIN(new LoginCommand()),
    SIGN_UP(new SignUpCommand()),
    SHOW_ORDERS(new ShowOrders()),
    SHOW_USERS(new ShowUsers()),
    BLOCK_USER(new BlockUser()),
    SET_ORDER_STATUS(new SetOrderStatus()),
    LOGOUT(new LogoutCommand());

    private final ActionCommand command;


    CommandEnum(ActionCommand command) {
        this.command = command;
    }

    public ActionCommand getCommand() {
        return this.command;
    }

}
