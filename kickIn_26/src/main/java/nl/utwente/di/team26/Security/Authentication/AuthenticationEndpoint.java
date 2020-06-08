package nl.utwente.di.team26.Security.Authentication;

import nl.utwente.di.team26.CONSTANTS;
import nl.utwente.di.team26.Exceptions.AuthenticationDeniedException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.security.SecureRandom;

@Path("/authentication")
public class AuthenticationEndpoint {

    @Context
    HttpServletResponse response;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void authenticateUser(Credentials credentials) throws IOException {
        try {
            authenticate(credentials);

            Cookie cookie = createCookie();
            cookie.setHttpOnly(true);
            response.addCookie(cookie);

            response.sendRedirect("./list.html");
        } catch (AuthenticationDeniedException e) {
            response.sendRedirect("./login.html");
        }
    }

    private void authenticate(Credentials credentials) throws AuthenticationDeniedException {
        // Authenticate against a database, LDAP, file or whatever
        // Throw an Exception if the credentials are invalid
        if (!whiteListed(credentials.getPassword())) {
            throw new AuthenticationDeniedException("You shall not pass");
        }

        // If matches password
        if (!matchingPassword(credentials)) {
            throw new AuthenticationDeniedException("You shall not pass");
        }
    }

    private boolean whiteListed(String emailAddress) {
        // TODO: look up the database.
        return true;
    }

    private boolean matchingPassword(Credentials credentials) {
        // TODO: read the database response.
        return true;
    }

    private Cookie createCookie() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] randomBytes = new byte[256];
        secureRandom.nextBytes(randomBytes);
        String token = new String(randomBytes);

        /*
        TODO: Store the token in the database with emailAddress.
         Overwriting existing one if necessary.
        */

        return new Cookie(CONSTANTS.COOKIENAME, token);
    }


}
