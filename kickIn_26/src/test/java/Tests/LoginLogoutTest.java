package Tests;

import kong.unirest.Cookie;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import nl.utwente.di.team26.Constants;
import nl.utwente.di.team26.Product.model.Authentication.Credentials;
import nl.utwente.di.team26.Product.model.Map.Map;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import static java.net.HttpURLConnection.*;
import static junit.framework.Assert.*;

public class LoginLogoutTest extends Tests {

    @Before
    public void configureEnvironment() {
        addThreeUsers();
    }

    @Rule
    public TestName name = new TestName();

    @Test
    public void validLogin() {
        System.out.println("Test " + name.getMethodName());
        HttpResponse<String> response = Unirest
                .post(getURIString("authentication"))
                .header("Content-Type", "application/json")
                .body(new Credentials(userNames[0] + "@email.com", defaultPassword))
                .asString();
        assertEquals("After login we should be : ", HTTP_NO_CONTENT, response.getStatus());
        //a go around way of making sure a cookie exists.
        assertTrue(response.getCookies().getNamed(Constants.COOKIENAME).isHttpOnly());
    }

    @Test
    public void validLogout() {
        System.out.println("Test " + name.getMethodName());
        Cookie anyLoginCookie = getLoginCookie(0);
        HttpResponse<String> response = Unirest
                .delete(getURIString("authentication"))
                .header("Cookie", anyLoginCookie.toString())
                .asString();
        assertEquals("Status is no content: ", HTTP_NO_CONTENT, response.getStatus());
        assertTrue("The max age of the Cookie should now be 0:", response.getCookies().getNamed(Constants.COOKIENAME).toString().contains("Max-Age=0"));
    }

    @Test
    public void invalidLogin() {
        System.out.println("Test " + name.getMethodName());
        String wrongPassword = "WrongPassword";
        HttpResponse<String> response = Unirest
                .post(getURIString("authentication"))
                .header("Content-Type", "application/json")
                .body(new Credentials(userNames[0] + "@email.com", wrongPassword))
                .asString();
        assertEquals("After login we should be : ", HTTP_FORBIDDEN, response.getStatus());
        //a go around way of making sure a cookie exists.
        assertNull(response.getCookies().getNamed(Constants.COOKIENAME));
    }

    @Test
    public void logOutWithoutCookie() {
        System.out.println("Test " + name.getMethodName());
        HttpResponse<String> response = Unirest
                .delete(getURIString("authentication"))
                .asString();
        assertEquals("Status is badRequest: ", HTTP_BAD_REQUEST, response.getStatus());
        assertTrue("Error message should tell cause", response.getBody().toLowerCase().contains("session"));
    }

    @Test
    public void invalidLogoutWithWrongCookie() {
        System.out.println("Test " + name.getMethodName());
        HttpResponse<String> response = Unirest
                .delete(getURIString("authentication"))
                .header("Cookie", "securityTeam26=InvalidString;Path=/;Max-Age=7200000;HttpOnly;")
                .asString();
        assertEquals("If ain't logged in you can't log out: ", response.getStatus(), HTTP_BAD_REQUEST);
    }

    @Test
    public void makingRequestsWithoutLogin() {
        System.out.println("Test " + name.getMethodName());
        HttpResponse<String> response1 = Unirest
                .get(getURIString("events"))
                .header("Content-Type", "application/json")
                .asString();
        HttpResponse<String> response2 = Unirest
                .get(getURIString("maps"))
                .header("Content-Type", "application/json")
                .asString();
        HttpResponse<String> response3 = Unirest
                .get(getURIString("resources"))
                .header("Content-Type", "application/json")
                .asString();
        assertEquals("Status is badRequest: ", HTTP_BAD_REQUEST, response1.getStatus());
        assertEquals("Status is badRequest: ", HTTP_BAD_REQUEST, response2.getStatus());
        assertEquals("Status is badRequest: ", HTTP_BAD_REQUEST, response3.getStatus());
    }

    @Test
    public void makingRequestsWithInvalidCookie() {
        System.out.println("Test " + name.getMethodName());
        HttpResponse<String> response1 = Unirest
                .get(getURIString("events"))
                .header("Content-Type", "application/json")
                .header("Cookie", "securityTeam26=InvalidString;Path=/;Max-Age=7200000;HttpOnly;")
                .asString();
        HttpResponse<String> response2 = Unirest
                .get(getURIString("maps"))
                .header("Content-Type", "application/json")
                .header("Cookie", "securityTeam26=InvalidString;Path=/;Max-Age=7200000;HttpOnly;")
                .asString();
        HttpResponse<String> response3 = Unirest
                .get(getURIString("resources"))
                .header("Content-Type", "application/json")
                .header("Cookie", "securityTeam26=InvalidString;Path=/;Max-Age=7200000;HttpOnly;")
                .asString();
        System.out.println(response1.getBody());
        assertEquals("Status is badRequest: ", HTTP_BAD_REQUEST, response1.getStatus());
        assertEquals("Status is badRequest: ", HTTP_BAD_REQUEST, response2.getStatus());
        assertEquals("Status is badRequest: ", HTTP_BAD_REQUEST, response3.getStatus());
    }

    @After
    public void tearDownSession() {
        destroyUsers();
    }

}
