package ua.od.cepuii.library.command.unregister;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.od.cepuii.library.command.ActionCommand;
import ua.od.cepuii.library.constants.AttributesName;
import ua.od.cepuii.library.constants.Constants;
import ua.od.cepuii.library.constants.Path;
import ua.od.cepuii.library.context.AppContext;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

import static ua.od.cepuii.library.constants.AttributesName.EMAIL;
import static ua.od.cepuii.library.constants.AttributesName.PASSWORD;

public class VerifyToken implements ActionCommand {

    private static final Logger log = LoggerFactory.getLogger(VerifyToken.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {

        String idTokenString = request.getParameter(AttributesName.CREDENTIAL);
        GoogleIdToken idToken = getGoogleIdToken(idTokenString);

        if (idToken == null) {
            log.error("can`t verify GoogleIdToken ");
            return request.getHeader(AttributesName.REFERER);
        }
        GoogleIdToken.Payload payload = idToken.getPayload();
        String userId = payload.getSubject();
        String email = payload.getEmail();
        request.setAttribute(EMAIL, email);
        request.setAttribute(PASSWORD, email + userId);

        if (request.getHeader(AttributesName.REFERER).endsWith(Constants.SIGN_UP)) {
            log.info("sign up with google, userEmail: {}", email);
            return Path.SIGN_UP_FORWARD;
        }
        log.info("login with google, userEmail: {}", email);
        return Path.LOGIN_FORWARD;
    }

    private static GoogleIdToken getGoogleIdToken(String idTokenString) {

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
                .setAudience(Collections.singletonList(AppContext.getInstance().getClientId()))
                .build();

        GoogleIdToken idToken = null;
        try {
            idToken = verifier.verify(idTokenString);
        } catch (GeneralSecurityException | IOException e) {
            log.error(e.getMessage());
        }
        return idToken;
    }

}

