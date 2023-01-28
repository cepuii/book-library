package ua.od.cepuii.library.command;

import jakarta.servlet.http.HttpServletRequest;
import ua.od.cepuii.library.command.unregister.EmptyCommand;
import ua.od.cepuii.library.resource.MessageManager;

public class ActionFactory {


    public ActionCommand defineCommand(HttpServletRequest request) {
        ActionCommand current = new EmptyCommand();
        String commandString = request.getParameter("command");
        if (commandString != null) {
            try {
                CommandEnum commandEnum = CommandEnum.valueOf(commandString.toUpperCase());
                current = commandEnum.getCommand();
            } catch (IllegalArgumentException e) {
                request.setAttribute("wrongAction", commandString + MessageManager.getProperty("message.wrongAction"));
            }
        }
        return current;
    }
}
