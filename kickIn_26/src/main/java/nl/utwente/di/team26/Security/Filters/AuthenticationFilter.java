package nl.utwente.di.team26.Security.Filters;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import nl.utwente.di.team26.Constants;
import nl.utwente.di.team26.Exception.Exceptions.NotFoundException;
import nl.utwente.di.team26.Exception.Exceptions.SessionNotFoundException;
import nl.utwente.di.team26.Exception.Exceptions.TokenInvalidException;
import nl.utwente.di.team26.Exception.Exceptions.TokenObsoleteException;
import nl.utwente.di.team26.Product.dao.Authentication.SessionDao;
import nl.utwente.di.team26.Product.dao.Authentication.UserDao;
import nl.utwente.di.team26.Product.model.Authentication.User;

import javax.annotation.Priority;
import javax.crypto.spec.SecretKeySpec;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.*;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
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
    private final SessionDao sessionDao = new SessionDao();

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        Map<String, Cookie> cookieJar = requestContext.getCookies();
        String authenticatedUserId = null;
        User authenticatedUser = null;

        // Validate the Cookie Token
        if (!hasCookie(cookieJar)) {
            sendCause(requestContext, "You currently are not part of a Session, please Login before continuing");
        } else {
            // Extract the token from the Cookie
            String token = cookieJar.get(Constants.COOKIENAME).getValue();
            System.out.println(cookieJar.get(Constants.COOKIENAME).toString());
            try {
                // Validate the token
                authenticatedUserId = validateToken(token);
                authenticatedUser = findUser(Long.parseLong(authenticatedUserId));
            } catch (TokenObsoleteException | TokenInvalidException e) {
                sendCause(requestContext, e.getMessage());
            } catch (NotFoundException e) {
                sendUnauthorized(requestContext, e.getMessage());
            } catch (SQLException e) {
                sendError(requestContext, e.getMessage());
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
                    int userClearanceLevel = finalAuthenticatedUser.getClearanceLevel();
                    return userClearanceLevel >= Integer.parseInt(forLevel);
                }

                @Override
                public boolean isSecure() {
                    return securityContext.isSecure();
                }

                @Override
                public String getAuthenticationScheme() {
                    return Constants.AUTH_SCHEME;
                }
            });
        }

    }

    private boolean hasCookie(Map<String, Cookie> cookieJar) {

        // Check if the Authorization header is valid
        // It must not be null and must be prefixed with "Bearer" plus a whitespace
        // The authentication scheme comparison must be case-insensitive
        return cookieJar.containsKey(Constants.COOKIENAME);
    }

    private void sendCause(ContainerRequestContext requestContext, String msg) {
        requestContext.abortWith(Response.status(Response.Status.BAD_REQUEST).entity(msg).build());
    }

    private void sendUnauthorized(ContainerRequestContext requestContext, String msg) {
        requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity(msg).build());
    }

    private void sendError(ContainerRequestContext requestContext, String msg) {
        requestContext.abortWith(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(msg).build());
    }

    private String validateToken(String token) throws TokenObsoleteException, SQLException, TokenInvalidException {
        // Check if the token was issued by the server and if it's not expired
        // Throw an Exception if the token is invalid
        //return the userId which always put into the subject to get the user.
        try {
            String validTokenSubject = decodeJWT(token).getSubject();
            checkTokenExists(token);
            return validTokenSubject;
        } catch (SessionNotFoundException | ExpiredJwtException e) {
            throw new TokenObsoleteException("The token has expired or is no longer in the Database. Please login-again.");
        } catch (SQLException e) {
            throw new SQLException(e);
        } catch (Exception e) {
            throw new TokenInvalidException("Token is not Valid. Please try clearing cookies and logging in Again.");
        }
    }

    public static Claims decodeJWT(String jwt) {
        //This line will throw an exception if it is not a signed JWS (as expected)
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        Key signingKey = new SecretKeySpec(Constants.SECRET.getBytes(), signatureAlgorithm.getJcaName());

        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }

    private User findUser(long userId) throws SQLException, NotFoundException {
        // Hit the the database or a service to find a user by its username and return it
        // Return the UserDao instance
        User user = null;
        user = userDao.getUserInstance(userId);
        return user;
    }

    private void checkTokenExists(String token) throws SessionNotFoundException, SQLException {
        try {
            sessionDao.checkExist(token);
        } catch (NotFoundException e) {
            throw new SessionNotFoundException("Session Not Found");
        }
    }
}
