package ua.od.cepuii.library.command;

import ua.od.cepuii.library.command.implementation.*;

public enum CommandEnum {
    EMPTY_COMMAND(new EmptyCommand()),
    ADD_BOOK_TO_ORDER(new AddBookToOrder()),
    REMOVE_BOOK_FROM_ORDER(new RemoveBookFromOrder()),

    LOGIN(new LoginCommand()),
    SIGN_UP(new SignUpCommand()),

    LOGOUT(new LogoutCommand());

    private final ActionCommand command;


    CommandEnum(ActionCommand command) {
        this.command = command;
    }

    public ActionCommand getCommand() {
        return this.command;
    }

}
