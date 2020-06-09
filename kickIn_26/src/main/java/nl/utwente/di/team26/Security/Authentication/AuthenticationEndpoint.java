package nl.utwente.di.team26.Security.Authentication;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import nl.utwente.di.team26.CONSTANTS;
import nl.utwente.di.team26.Exceptions.AuthenticationDeniedException;
import nl.utwente.di.team26.Security.Authentication.User.UserDao;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Date;

@Path("/authentication")
public class AuthenticationEndpoint {

    @Context
    HttpServletResponse response;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void authenticateUser(Credentials credentials) throws IOException {
        try {
            int userId = authenticate(credentials);

            Cookie cookie = createCookie(userId);
            cookie.setHttpOnly(true);
            response.addCookie(cookie);

            response.sendRedirect("./list.html");
        } catch (AuthenticationDeniedException e) {
            response.sendRedirect("./login.html");
        }
    }

    private int authenticate(Credentials credentials) throws AuthenticationDeniedException {
        // Authenticate against a database, LDAP, file or whatever
        // Throw an Exception if the credentials are invalid
        if (!whiteListed(credentials.getEmail())) {
            throw new AuthenticationDeniedException("You shall not pass");
        }

        // If matches password
        if (!matchingPassword(credentials)) {
            throw new AuthenticationDeniedException("You shall not pass");
        }

        return 0;
    }

    private boolean whiteListed(String emailAddress) {
        // TODO: look up the database.
        return true;
    }

    private boolean matchingPassword(Credentials credentials) {
        // TODO: read the database response.
        return true;
    }

    private Cookie createCookie(int userId) {


        String count = getCount();
        String token = createJWT(String.valueOf(userId), count);

        /*
        TODO: Store the token in the database with userId.
         Overwriting existing one if necessary.
        */
        return new Cookie(CONSTANTS.COOKIENAME, token);
    }

    private String getCount() {
        return String.valueOf(new UserDao().getCount());
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
