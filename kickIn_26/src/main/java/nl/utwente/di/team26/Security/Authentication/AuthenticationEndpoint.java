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

@Path("/authentication")
public class AuthenticationEndpoint {

    @Context
    HttpServletResponse response;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public void authenticateUser(Credentials credentials) throws IOException {
        try {
            authenticate(credentials.getEmail());
            Cookie cookie = createCookie(credentials);
            cookie.setHttpOnly(true);
            response.addCookie(cookie);
        } catch (AuthenticationDeniedException e) {
            response.sendRedirect("./login.html");
        }
    }

    private void authenticate(String emailAddress) throws AuthenticationDeniedException {
        // Authenticate against a database, LDAP, file or whatever
        // Throw an Exception if the credentials are invalid
        if (!whiteListed(emailAddress)) {
            throw new AuthenticationDeniedException("You are not Allowed");
        }
    }

    private boolean whiteListed(String emailAddress) {
        return true;
    }

    private Cookie createCookie(Credentials credentials) {
        //Store the token in the database with emailAddress.
        //Overwriting existing one if necessary.
        return new Cookie(CONSTANTS.COOKIENAME, credentials.googleToken);
    }


}
