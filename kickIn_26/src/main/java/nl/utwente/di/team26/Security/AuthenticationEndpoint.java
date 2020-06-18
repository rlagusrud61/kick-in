package nl.utwente.di.team26.Security;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import nl.utwente.di.team26.CONSTANTS;
import nl.utwente.di.team26.Exception.Exceptions.AuthenticationDeniedException;
import nl.utwente.di.team26.Product.dao.Authentication.SessionDao;
import nl.utwente.di.team26.Product.dao.Authentication.UserDao;
import nl.utwente.di.team26.Product.model.Authentication.Session;
import nl.utwente.di.team26.Security.Filters.Secured;
import nl.utwente.di.team26.Product.model.Authentication.Credentials;
import nl.utwente.di.team26.Product.model.Authentication.User;
import nl.utwente.di.team26.Utils;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.IOException;
import java.security.Key;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

@Path("/authentication")
public class AuthenticationEndpoint {

    @Context
    HttpServletResponse response;

    @Context
    HttpServletRequest request;

    @Context
    SecurityContext securityContext;
    @Context
    UriInfo uriInfo;

    UserDao userDao = new UserDao();
    SessionDao sessionDao = new SessionDao();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String echo(String something) {
        return something;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void authenticateUser(Credentials credentials) throws IOException {
        try {
            User user = authenticateCredentials(credentials);

            Cookie cookie = createCookie(user.getUserId());
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            response.addCookie(cookie);
            response.sendRedirect(uriInfo.getAbsolutePathBuilder().replacePath("/kickInTeam26/list.html").toString());
        } catch (AuthenticationDeniedException e) {
            e.printStackTrace();
            response.sendError(Response.Status.UNAUTHORIZED.getStatusCode(), e.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), e.getMessage());
        }
    }

    @Secured
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public void deleteToken(@Context HttpHeaders headers) throws IOException {

        try {

            long userId = Utils.getUserFromContext(securityContext);

            try(Connection conn = CONSTANTS.getConnection()) {
                sessionDao.clearTokensForUser(conn, userId, true);
            }

            Cookie cookie = createRemovalCookie();
            response.addCookie(cookie);
            response.sendRedirect(uriInfo.getAbsolutePathBuilder().replacePath("/kickInTeam26/login.html").toString());

        } catch (Exception e) {
            response.sendError(Response.Status.BAD_REQUEST.getStatusCode(),CONSTANTS.FAILURE + ": " + e.getMessage());
        }


    }

    private User authenticateCredentials(Credentials credentials) throws AuthenticationDeniedException, SQLException {
        // Authenticate against a database, LDAP, file or whatever
        // Throw an Exception if the credentials are invalid
        try (Connection conn = CONSTANTS.getConnection()) {
            return userDao.authenticateUser(conn, credentials);
        }
    }

    private Cookie createCookie(long userId) throws SQLException {
        String tokenId = getMaxId() + 1;
        String token = createJWT(String.valueOf(userId), tokenId);

        try (Connection conn = CONSTANTS.getConnection()) {
            sessionDao.create(conn, new Session(token, userId));
        }

        return new Cookie(CONSTANTS.COOKIENAME, token);
    }

    private Cookie createRemovalCookie() {
        Cookie cookie = new Cookie(CONSTANTS.COOKIENAME, "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        cookie.setComment("EXPIRING COOKIE at " + System.currentTimeMillis());
        return cookie;
    }

    private String getMaxId() throws SQLException {
        String maxIdSet;
        try(Connection conn = CONSTANTS.getConnection()) {
            maxIdSet = String.valueOf(sessionDao.maxId(conn));
        }
        return maxIdSet;
    }

    public static String createJWT(String subject, String count) {

        //The JWT signature algorithm we will be using to sign the token
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        Key signingKey = new SecretKeySpec(CONSTANTS.SECRET.getBytes(), signatureAlgorithm.getJcaName());

        long nowMillis = System.currentTimeMillis();
        long expMillis = nowMillis + CONSTANTS.TTK;
        Date now = new Date(nowMillis);
        Date exp = new Date(expMillis);

        //Let's set the JWT Claims
        JwtBuilder builder = Jwts.builder()
                .setId(count)
                .setIssuer(CONSTANTS.ISSUER)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(signingKey, signatureAlgorithm);

        //Builds the JWT and serializes it to a compact, URL-safe string
        return builder.compact();
    }

}
