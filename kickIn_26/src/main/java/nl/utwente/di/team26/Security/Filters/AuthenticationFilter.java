package nl.utwente.di.team26.Security.Filters;

import io.jsonwebtoken.*;
import nl.utwente.di.team26.CONSTANTS;
import nl.utwente.di.team26.Exceptions.AuthenticationDeniedException;
import nl.utwente.di.team26.Exceptions.NotFoundException;
import nl.utwente.di.team26.Exceptions.TokenObsoleteException;
import nl.utwente.di.team26.Product.dao.Events.EventMapDao;
import nl.utwente.di.team26.Security.User.User;
import nl.utwente.di.team26.Security.User.UserDao;

import javax.annotation.Priority;
import javax.crypto.spec.SecretKeySpec;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.*;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Key;
import java.security.Principal;
import java.sql.SQLException;
import java.util.Map;

@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

    @Context
    UriInfo uriInfo;

    private final UserDao userDao = new UserDao();

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        Map<String, Cookie> cookieJar = requestContext.getCookies();
        String authenticatedUserId = null;
        User authenticatedUser = null;

        // Validate the Cookie Token
        if (!hasCookie(cookieJar)) {
            sendToLogin(requestContext);
        } else {
            // Extract the token from the Cookie
            String token = cookieJar.get(CONSTANTS.COOKIENAME).getValue();

            try {
                // Validate the token
                authenticatedUserId = validateToken(token);
                authenticatedUser = findUser(Integer.parseInt(authenticatedUserId));
            } catch (TokenObsoleteException e) {
                e.printStackTrace();
                sendToLogin(requestContext);
                return;
            } catch (AuthenticationDeniedException e) {
                e.printStackTrace();
                abortWithUnauthorized(requestContext);
                return;
            }
        }

        if (authenticatedUserId != null && authenticatedUser != null) {
            final SecurityContext securityContext = requestContext.getSecurityContext();

            String finalAuthenticatedUserId = authenticatedUserId;
            User finalAuthenticatedUser = authenticatedUser;

            requestContext.setSecurityContext(new SecurityContext() {
                @Override
                public Principal getUserPrincipal() {
                    return () -> finalAuthenticatedUserId;
                }

                @Override
                public boolean isUserInRole(String forLevel) {
                    int userClearanceLevel = finalAuthenticatedUser.getClarificationLevel();
                    return userClearanceLevel >= Integer.parseInt(forLevel);
                }

                @Override
                public boolean isSecure() {
                    return securityContext.isSecure();
                }

                @Override
                public String getAuthenticationScheme() {
                    return CONSTANTS.AUTH_SCHEME;
                }
            });
        }

    }

    private boolean hasCookie(Map<String, Cookie> cookieJar) {

        // Check if the Authorization header is valid
        // It must not be null and must be prefixed with "Bearer" plus a whitespace
        // The authentication scheme comparison must be case-insensitive
        return cookieJar.containsKey(CONSTANTS.COOKIENAME);
    }

    private void sendToLogin(ContainerRequestContext requestContext) {
        requestContext.abortWith(Response.seeOther(requestContext.getUriInfo().getAbsolutePath()).build());
    }


    private void abortWithUnauthorized(ContainerRequestContext requestContext) {

        // Abort the filter chain with a 401 status code response
        requestContext.abortWith(
                Response.status(Response.Status.UNAUTHORIZED).build());
    }

    private String validateToken(String token) throws AuthenticationDeniedException, TokenObsoleteException {
        // Check if the token was issued by the server and if it's not expired
        // Throw an Exception if the token is invalid
        //return the userId which always put into the subject to get the user.
        try {
            return decodeJWT(token).getSubject();
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

    private User findUser(int userId) {
        // Hit the the database or a service to find a user by its username and return it
        // Return the User instance
        User user = null;
        try {
            user = userDao.getObject(CONSTANTS.getConnection(), userId);
        } catch (NotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return user;
    }
}
