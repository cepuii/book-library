package ua.od.cepuii.library.command.unregister;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ua.od.cepuii.library.command.ActionCommand;
import ua.od.cepuii.library.constants.Path;
import ua.od.cepuii.library.context.AppContext;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

public class VerifyToken implements ActionCommand {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
                .setAudience(Collections.singletonList(AppContext.getInstance().getClientId()))
                .build();

// (Receive idTokenString by HTTPS POST)
        String idTokenString = request.getParameter("credential");
        GoogleIdToken idToken = null;
        try {
            idToken = verifier.verify(idTokenString);
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }
        if (idToken != null) {
            GoogleIdToken.Payload payload = idToken.getPayload();
            // Print user identifier
            String userId = payload.getSubject();

            String email = payload.getEmail();
            request.setAttribute("password", userId);
            request.setAttribute("email", email);
            if (request.getHeader("Referer").endsWith("sign_up")) {

                return Path.SIGN_UP_WITH_GOOGLE_FORWARD;
            }
            return Path.LOGIN_WITH_GOOGLE_FORWARD;

        } else {
            System.out.println("Invalid ID token.");
        }
        return "";
    }

}

