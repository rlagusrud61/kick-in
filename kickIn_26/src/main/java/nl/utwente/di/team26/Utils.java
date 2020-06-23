package nl.utwente.di.team26;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.security.Key;
import java.util.Arrays;

public class Utils {

    public static long getUserFromContext(SecurityContext securityContext) {
        return Long.parseLong(securityContext.getUserPrincipal().getName());
    }

    public static Response returnOkResponse(String message) {
        return Response.ok(message).build();
    }

    public static Response returnNoContent() {
        return Response.noContent().build();
    }

    public static Response returnCreated(long oid) {
        return Response.status(Response.Status.CREATED).entity(oid).build();
    }

    public static Response returnCreated() {
        return Response.status(Response.Status.CREATED).build();
    }

    public static String hashPassword(String password) {
        Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
        return argon2.hash(4, 1024 * 1024, 8, password);
    }

    public static boolean verifyHash(String hash, String password) {
        Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
        return argon2.verify(hash, password);
    }

    public static void main(String[] args) {
        Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
        for (int i = 0; i< 1; i++) {
            System.out.println(hashPassword("hk"));
        }
    }
}
