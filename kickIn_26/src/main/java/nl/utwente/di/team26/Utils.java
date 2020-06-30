package nl.utwente.di.team26;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import nl.utwente.di.team26.Exception.Exceptions.NotFoundException;
import nl.utwente.di.team26.Product.dao.Authentication.UserDao;
import nl.utwente.di.team26.Product.model.Authentication.User;
import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;

import javax.crypto.spec.SecretKeySpec;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.security.Key;
import java.sql.SQLException;
import java.util.Arrays;

import static org.passay.AllowedRegexRule.ERROR_CODE;

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

    public static String generatePassayPassword() {
        PasswordGenerator gen = new PasswordGenerator();
        CharacterData lowerCaseChars = EnglishCharacterData.LowerCase;
        CharacterRule lowerCaseRule = new CharacterRule(lowerCaseChars);
        lowerCaseRule.setNumberOfCharacters(2);

        CharacterData upperCaseChars = EnglishCharacterData.UpperCase;
        CharacterRule upperCaseRule = new CharacterRule(upperCaseChars);
        upperCaseRule.setNumberOfCharacters(2);

        CharacterData digitChars = EnglishCharacterData.Digit;
        CharacterRule digitRule = new CharacterRule(digitChars);
        digitRule.setNumberOfCharacters(2);

        CharacterData specialChars = new CharacterData() {
            public String getErrorCode() {
                return ERROR_CODE;
            }

            public String getCharacters() {
                return "!@#$%^&*()_+";
            }
        };
        CharacterRule splCharRule = new CharacterRule(specialChars);
        splCharRule.setNumberOfCharacters(2);

        return gen.generatePassword(15, splCharRule, lowerCaseRule,
                upperCaseRule, digitRule);
    }

    public static User findUser(long userId) throws SQLException, NotFoundException {
        // Hit the the database or a service to find a user by its username and return it
        // Return the UserDao instance
        UserDao userDao = new UserDao();
        return userDao.getUserInstance(userId);
    }

    public static void main(String[] args) {
        System.out.println(verifyHash("$argon2id$v=19$m=1048576,t=4,p=8$+gojZnkDa2hoVV9c6NToHw$AJBOOK1tKfqnCa8C9E6fwhwoId3YgIPATtJycq9uUFg", "password"));
    }
}
