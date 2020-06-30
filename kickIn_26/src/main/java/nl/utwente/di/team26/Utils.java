package nl.utwente.di.team26;

import nl.utwente.di.team26.Exception.Exceptions.NotFoundException;
import nl.utwente.di.team26.Product.dao.Authentication.UserDao;
import nl.utwente.di.team26.Product.model.Authentication.User;
import org.apache.commons.codec.binary.Hex;
import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;

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
        char[] passwordChars = password.toCharArray();
        byte[] saltBytes = Constants.SECRET.getBytes();

        byte[] hashedBytes = genHash(passwordChars, saltBytes, Constants.ITERATION, Constants.KEY_LENGTH);
        return Hex.encodeHexString(hashedBytes);
    }

    public static byte[] genHash(final char[] password, final byte[] salt, final int iterations, final int keyLength) {
        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
            PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, keyLength);
            SecretKey key = skf.generateSecret( spec );
            return key.getEncoded( );
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean verifyHash(String hash, String password) {
        return hash.equals(hashPassword(password));
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
        System.out.println(hashPassword("hk"));
    }
}
