package ua.od.cepuii.library.command;

import jakarta.servlet.http.HttpServletRequest;
import ua.od.cepuii.library.command.unregister.EmptyCommand;

import java.util.Map;

import static ua.od.cepuii.library.constants.AttributesName.*;

/**
 * This class defines the command to be executed based on the command parameter passed from the request.
 *
 * @author Sergei Chernousov
 * @version 1.0
 */
public class ActionFactory {

    /**
     * Defines the command to be executed based on the "command" parameter passed in the request.
     *
     * @param request HttpServletRequest to get the "command" parameter from.
     * @return An instance of the ActionCommand corresponding to the "command" parameter. If the "command" parameter
     * is not specified or not found, the method returns an instance of the EmptyCommand.
     */
    public ActionCommand defineCommand(HttpServletRequest request) {
        ActionCommand current = new EmptyCommand();
        String commandString = request.getParameter(COMMAND);
        if (commandString != null) {
            try {
                CommandEnum commandEnum = CommandEnum.valueOf(commandString.toUpperCase());
                current = commandEnum.getCommand();
            } catch (IllegalArgumentException e) {
                request.setAttribute(REPORTS, Map.of(WRONG_ACTION, commandString + ": command not found or wrong!"));
            }
        }
        return current;
    }
}
