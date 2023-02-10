package ua.od.cepuii.library.command.admin;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ua.od.cepuii.library.command.ActionCommand;
import ua.od.cepuii.library.constants.Path;
import ua.od.cepuii.library.dto.RequestParser;

import static ua.od.cepuii.library.constants.AttributesName.REPORTS;

public class AddLibrarian implements ActionCommand {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        RequestParser.setMapFromSessionToRequest(request, REPORTS);
        return Path.ADD_LIBRARIAN_PAGE;
    }
}
