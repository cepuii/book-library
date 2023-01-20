package ua.od.cepuii.library.command.admin;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ua.od.cepuii.library.command.ActionCommand;
import ua.od.cepuii.library.resource.ConfigurationManager;

public class AddLibrarian implements ActionCommand {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {

        return ConfigurationManager.getProperty("path.page.add.librarian");
    }
}
