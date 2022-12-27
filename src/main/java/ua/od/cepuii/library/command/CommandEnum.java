package ua.od.cepuii.library.command;

import ua.od.cepuii.library.command.implementation.*;

public enum CommandEnum {
    EMPTY(new EmptyCommand()),
    ADDBOOKTOORDER(new AddBookToOrder()),
    REMOVEBOOKFROMORDER(new RemoveBookFromOrder()),

    LOGIN(new LoginCommand()),

    LOGOUT(new LogoutCommand());

    private final ActionCommand command;


    CommandEnum(ActionCommand command) {
        this.command = command;
    }

    public ActionCommand getCommand() {
        return this.command;
    }

}
