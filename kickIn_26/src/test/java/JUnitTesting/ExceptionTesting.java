package JUnitTesting;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import kong.unirest.Cookie;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import nl.utwente.di.team26.Constants;
import nl.utwente.di.team26.Product.model.Map.Map;
import nl.utwente.di.team26.Security.User.Roles;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;

import static java.net.HttpURLConnection.HTTP_NOT_FOUND;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ExceptionTesting extends Tests {

    @Before
    public void setUp() {
        addThreeUsers();
    }

    @After
    public void tearDown() {
        destroyUsers();
    }

    @Rule
    public TestName name = new TestName();

    @Test
    public void exceptionTesting() {
        //these are explicit not found error testing not tested else where, only done with map
        //the function is similar for all others.

        System.out.println("Test " + name.getMethodName());
        Cookie loginCookie = getLoginCookie(Roles.ADMIN.getLevel());
        HttpResponse<String> getMapThatDoesNotExist = Unirest
                .get(getURIString("map/" + 234523453))
                .header("Content-Type", "application/json")
                .header("Cookie", loginCookie.toString())
                .asString();
        HttpResponse<String> putMapThatDoesNOtExist = Unirest
                .put(getURIString("map/" + 234523453))
                .header("Content-Type", "application/json")
                .header("Cookie", loginCookie.toString())
                .body(new Map(234523453, "DoesNotExist", "Does Not Exist"))
                .asString();
        HttpResponse<String> nonExistentEndpoint = Unirest
                .get(getURIString("masdfasfp/" + 234523453))
                .header("Content-Type", "application/json")
                .header("Cookie", loginCookie.toString())
                .asString();
        assertEquals(HTTP_NOT_FOUND, putMapThatDoesNOtExist.getStatus());
        assertEquals(HTTP_NOT_FOUND, getMapThatDoesNotExist.getStatus());
        assertTrue(nonExistentEndpoint.getBody().toLowerCase().contains("HTTP 404 Not Found".toLowerCase()));
    }

}
