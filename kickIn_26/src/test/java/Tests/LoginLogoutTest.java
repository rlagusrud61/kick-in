package Tests;

import kong.unirest.Cookie;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import nl.utwente.di.team26.CONSTANTS;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static java.net.HttpURLConnection.*;
import static junit.framework.Assert.*;

public class LoginLogoutTest extends Tests {

    @Before
    public void configureEnvironment() {
        addThreeUsers();
    }

    @Test
    public void validLogin() {
        HttpResponse<String> response = Unirest
                .post(getURIString("authentication"))
                .header("Content-Type", "application/json")
                .body("{\"email\":\"" + userNames[0] + "@email.com\",\"password\":\""+defaultPassword+"\"}")
                .asString();
        assertEquals("After login we should be : ", HTTP_NO_CONTENT, response.getStatus());
        //a go around way of making sure a cookie exists.
        assertTrue(response.getCookies().getNamed(CONSTANTS.COOKIENAME).isHttpOnly());
    }

    @Test
    public void validLogout() {
        Cookie anyLoginCookie = getLoginCookie(0);
        HttpResponse<String> response = Unirest
                .delete(getURIString("authentication"))
                .header("Cookie", anyLoginCookie.toString())
                .asString();
        assertEquals("Status is no content: ", HTTP_NO_CONTENT, response.getStatus());
        assertTrue("The max age of the Cookie should now be 0:", response.getCookies().getNamed(CONSTANTS.COOKIENAME).toString().contains("Max-Age=0"));
    }

    @Test
    public void invalidLogin() {
        String wrongPassword = "WrongPassword";
        HttpResponse<String> response = Unirest
                .post(getURIString("authentication"))
                .header("Content-Type", "application/json")
                .body("{\"email\":\"" + userNames[0] + "@email.com\",\"password\":\""+wrongPassword+"\"}")
                .asString();
        assertEquals("After login we should be : ", HTTP_FORBIDDEN, response.getStatus());
        //a go around way of making sure a cookie exists.
        assertNull(response.getCookies().getNamed(CONSTANTS.COOKIENAME));
    }

    @Test
    public void logOutWithoutCookie() {
        HttpResponse<String> response = Unirest
                .delete(getURIString("authentication"))
                .asString();
        assertEquals("Status is no content: ", HTTP_BAD_REQUEST, response.getStatus());
        assertTrue("Error message should tell cause", response.getBody().toLowerCase().contains("session"));
    }

    @Test
    public void invalidLogoutWithWrongCookie() {
        String someInvalidString = "InvalidString";
        Cookie anyLoginCookie = getLoginCookie(0);
        String value = anyLoginCookie.toString().split(";")[0].split("=")[1];
        String invalidCookie = anyLoginCookie.toString().replace(value, someInvalidString);
        System.out.println(invalidCookie);
        HttpResponse<String> response = Unirest
                .delete(getURIString("authentication"))
                .header("Cookie", invalidCookie)
                .asString();
        System.out.println(response.getStatus());
    }

    @After
    public void tearDownSession() {
        destroyUsers();
    }

}
