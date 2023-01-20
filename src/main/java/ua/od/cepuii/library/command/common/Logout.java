package ua.od.cepuii.library.command.common;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ua.od.cepuii.library.command.ActionCommand;
import ua.od.cepuii.library.resource.ConfigurationManager;
import ua.od.cepuii.library.util.CookieUtil;

public class Logout implements ActionCommand {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        CookieUtil.cleanCookie(request);
        request.getSession().invalidate();
        request.getSession(true).setAttribute("logout", "true");
        return ConfigurationManager.getProperty("path.page.login");
    }

}
