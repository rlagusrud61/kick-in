package nl.utwente.di.team26.Security.Authentication;

import nl.utwente.di.team26.CONSTANTS;
import nl.utwente.di.team26.Exceptions.AuthenticationDeniedException;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

    @Context
    UriInfo uriInfo;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        Map<String, Cookie> cookieJar = requestContext.getCookies();

        // Validate the Authorization header
        if (!hasCookie(cookieJar)) {
            try {
                sendToLogin(requestContext);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        } else {
            // Extract the token from the Cookie
            String token = cookieJar.get(CONSTANTS.COOKIENAME).getValue();

            try {
                // Validate the token
                validateToken(token);
            } catch (AuthenticationDeniedException e) {
                abortWithUnauthorized(requestContext);
            }
        }

    }

    private boolean hasCookie(Map<String, Cookie> cookieJar) {

        // Check if the Authorization header is valid
        // It must not be null and must be prefixed with "Bearer" plus a whitespace
        // The authentication scheme comparison must be case-insensitive
        return cookieJar.containsKey(CONSTANTS.COOKIENAME);
    }

    private void sendToLogin(ContainerRequestContext requestContext) throws URISyntaxException {
        requestContext.abortWith(Response.seeOther(new URI("http://localhost:8080/kickInTeam26/")).build());
    }


    private void abortWithUnauthorized(ContainerRequestContext requestContext) {

        // Abort the filter chain with a 401 status code response
        requestContext.abortWith(
                Response.status(Response.Status.UNAUTHORIZED).build());
    }

    private void validateToken(String token) throws AuthenticationDeniedException {
        // Check if the token was issued by the server and if it's not expired
        // Throw an Exception if the token is invalid
    }
}
