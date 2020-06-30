package nl.utwente.di.team26.Security;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import nl.utwente.di.team26.Constants;
import nl.utwente.di.team26.Exception.Exceptions.AuthenticationDeniedException;
import nl.utwente.di.team26.Exception.Exceptions.NotFoundException;
import nl.utwente.di.team26.Product.dao.Authentication.SessionDao;
import nl.utwente.di.team26.Product.dao.Authentication.UserDao;
import nl.utwente.di.team26.Product.model.Authentication.Credentials;
import nl.utwente.di.team26.Product.model.Authentication.Session;
import nl.utwente.di.team26.Product.model.Authentication.User;
import nl.utwente.di.team26.Security.Filters.Secured;
import nl.utwente.di.team26.Utils;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
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
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response echo(Credentials credentials) {
        return Response.ok(credentials).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response authenticateUser(Credentials credentials) throws AuthenticationDeniedException, SQLException {
        System.out.println(credentials);
        User user = authenticateCredentials(credentials);
        String cookie = createCookie(user.getUserId());
        return Response
                .noContent()
                .header("Set-Cookie", cookie).build();
    }

    @Secured
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteToken(@Context HttpHeaders headers) throws SQLException, NotFoundException {
        long userId = Utils.getUserFromContext(securityContext);
        sessionDao.clearTokensForUser(userId);
        String cookie = createRemovalCookie();
        return Response.noContent().header("Set-Cookie", cookie).build();
    }

    private User authenticateCredentials(Credentials credentials) throws AuthenticationDeniedException, SQLException {
        // Authenticate against a database, LDAP, file or whatever
        // Throw an Exception if the credentials are invalid
        User userInstance = userDao.getUserByEmail(credentials);
        if (Utils.verifyHash(userInstance.getPassword(), credentials.getPassword())) {
            return userInstance;
        }
        throw new AuthenticationDeniedException("Credentials could not be verified to be true." +
                "hash: " + userInstance.getPassword()  + "  " +
                "pass: " + credentials.getPassword());
    }

    private String createCookie(long userId) throws SQLException {
        String tokenId = getMaxId() + 1;
        String token = createJWT(String.valueOf(userId), tokenId);

        try (Connection conn = Constants.getConnection()) {
            sessionDao.create(new Session(token, userId));
        }
        return String.format("%s=%s;Path=%s;Max-Age="+ Constants.TTK+";HttpOnly", Constants.COOKIENAME, token, "/");
    }

    private String createRemovalCookie() {
        return Constants.COOKIENAME +"=;Path=/;Expires=Thu, 01-Jan-1970 00:00:10 GMT;Max-Age=0";
    }

    private String getMaxId() throws SQLException {
        String maxIdSet;
        maxIdSet = String.valueOf(sessionDao.maxId());
        return maxIdSet;
    }

    public static String createJWT(String subject, String count) {

        //The JWT signature algorithm we will be using to sign the token
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        Key signingKey = new SecretKeySpec(Constants.SECRET.getBytes(), signatureAlgorithm.getJcaName());

        long nowMillis = System.currentTimeMillis();
        long expMillis = nowMillis + Constants.TTK;
        Date now = new Date(nowMillis);
        Date exp = new Date(expMillis);

        //Let's set the JWT Claims
        JwtBuilder builder = Jwts.builder()
                .setId(count)
                .setIssuer(Constants.ISSUER)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(signingKey, signatureAlgorithm);

        //Builds the JWT and serializes it to a compact, URL-safe string
        return builder.compact();
    }

}
