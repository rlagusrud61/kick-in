package nl.utwente.di.team26.Security;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import nl.utwente.di.team26.CONSTANTS;
import nl.utwente.di.team26.Exceptions.AuthenticationDeniedException;
import nl.utwente.di.team26.Security.User.Credentials;
import nl.utwente.di.team26.Security.User.User;
import nl.utwente.di.team26.Security.User.UserDao;
import nl.utwente.di.team26.Security.Session.Session;
import nl.utwente.di.team26.Security.Session.SessionDao;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.security.Key;
import java.sql.SQLException;
import java.util.Date;

@Path("/authentication")
public class AuthenticationEndpoint {

    @Context
    HttpServletResponse response;

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
        System.out.println(credentials);
        try {
            User user = authenticate(credentials);

            Cookie cookie = createCookie(user.getUserId());
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            response.addCookie(cookie);

            response.sendRedirect("http://localhost:8080/kickInTeam26/list.html");
        } catch (AuthenticationDeniedException e) {
            response.sendError(Response.Status.UNAUTHORIZED.getStatusCode(), e.getMessage());
        } catch (SQLException throwables) {
            response.sendError(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), throwables.getMessage());
        }
    }

    private User authenticate(Credentials credentials) throws AuthenticationDeniedException {
        // Authenticate against a database, LDAP, file or whatever
        // Throw an Exception if the credentials are invalid

        User user = matchingPassword(credentials);
        if (user.equals(new User())) {
            throw new AuthenticationDeniedException("You shall not pass");
        }
        return user;
    }

    private User matchingPassword(Credentials credentials) throws AuthenticationDeniedException {
        System.out.println(credentials);
        return userDao.authenticateUser(CONSTANTS.getConnection(), credentials);
    }

    private Cookie createCookie(int userId) throws SQLException {


        String tokenId = getCount() + 1;
        String token = createJWT(String.valueOf(userId), tokenId);

        sessionDao.create(CONSTANTS.getConnection(), new Session(token, userId));
        return new Cookie(CONSTANTS.COOKIENAME, token);
    }

    private String getCount() {
        String countOf = null;
        try {
            countOf = String.valueOf(sessionDao.countAll(CONSTANTS.getConnection()));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return countOf;
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
