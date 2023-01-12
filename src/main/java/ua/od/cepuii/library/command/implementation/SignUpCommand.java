package ua.od.cepuii.library.command.implementation;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ua.od.cepuii.library.command.ActionCommand;
import ua.od.cepuii.library.entity.enums.Role;
import ua.od.cepuii.library.resource.ConfigurationManager;
import ua.od.cepuii.library.resource.MessageManager;
import ua.od.cepuii.library.service.UserService;
import ua.od.cepuii.library.util.ValidationUtil;

public class SignUpCommand implements ActionCommand {

    private final UserService userService = new UserService();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String path;
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String role = request.getParameter("role");
        if (ValidationUtil.validateEmail(email) || ValidationUtil.validatePass(password)) {
            long userId = userService.create(email, password, role == null ? Role.READER : Role.valueOf(role));
            HttpSession session = request.getSession();
            session.setAttribute("userId", userId);
            session.setAttribute("user", email);
            path = ConfigurationManager.getProperty("path.page.main");
        } else {
            request.setAttribute("errorLoginPassMessage", MessageManager.getProperty("message.signuperror"));
            path = ConfigurationManager.getProperty("path.page.signup");
        }

        return path;
    }
}
