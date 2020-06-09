package nl.utwente.di.team26.Security.Authentication;

import io.jsonwebtoken.*;
import nl.utwente.di.team26.CONSTANTS;
import nl.utwente.di.team26.Exceptions.AuthenticationDeniedException;
import nl.utwente.di.team26.Exceptions.TokenObsoleteException;
import nl.utwente.di.team26.Security.Authentication.User.AuthenticatedUser;
import nl.utwente.di.team26.Security.Authentication.User.User;
import nl.utwente.di.team26.Security.Authentication.User.UserDao;

import javax.annotation.Priority;
import javax.crypto.spec.SecretKeySpec;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Key;
import java.util.Map;

@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

    @Inject
    @AuthenticatedUser
    Event<String> userAuthenticatedEvent;

    @Context
    UriInfo uriInfo;

    String userId;
    UserDao userDao = new UserDao();
    SessionDao sessionDao = new SessionDao();

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        Map<String, Cookie> cookieJar = requestContext.getCookies();

        // Validate the Cookie Token
            try {
                if (!hasCookie(cookieJar)) {
                sendToLogin(requestContext);
                } else {
                    // Extract the token from the Cookie
                    String token = cookieJar.get(CONSTANTS.COOKIENAME).getValue();

                    try {
                        // Validate the token
                        validateToken(token);
                        userAuthenticatedEvent.fire(userId);
                    } catch (TokenObsoleteException e) {
                        e.printStackTrace();
                        sendToLogin(requestContext);
                    } catch (AuthenticationDeniedException e) {
                        e.printStackTrace();
                        abortWithUnauthorized(requestContext);
                    }
                }
            } catch (URISyntaxException e) {
                e.printStackTrace();
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

    private void validateToken(String token) throws AuthenticationDeniedException, TokenObsoleteException {
        // Check if the token was issued by the server and if it's not expired
        // Throw an Exception if the token is invalid
        try {
            Claims claims = decodeJWT(token);
            userId = claims.getSubject();
        } catch (ExpiredJwtException e) {
            throw new TokenObsoleteException("Time for a new Token!");
        } catch (JwtException e) {
            throw new AuthenticationDeniedException("Denied");
        }
    }

    public static Claims decodeJWT(String jwt) {
        //This line will throw an exception if it is not a signed JWS (as expected)
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        Key signingKey = new SecretKeySpec(CONSTANTS.SECRET.getBytes(), signatureAlgorithm.getJcaName());

        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }
}
