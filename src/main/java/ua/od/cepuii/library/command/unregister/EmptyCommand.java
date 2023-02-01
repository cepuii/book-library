package ua.od.cepuii.library.command.unregister;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ua.od.cepuii.library.command.ActionCommand;
import ua.od.cepuii.library.util.PathManager;

public class EmptyCommand implements ActionCommand {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        return PathManager.getProperty("page.index");
    }
}
