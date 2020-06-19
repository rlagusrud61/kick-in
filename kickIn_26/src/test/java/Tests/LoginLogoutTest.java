package Tests;

import kong.unirest.Cookie;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import nl.utwente.di.team26.CONSTANTS;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static java.net.HttpURLConnection.HTTP_NO_CONTENT;
import static java.net.HttpURLConnection.HTTP_OK;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class LoginLogoutTest extends Tests {

    @Before
    public void configureEnvironment() {
        addThreeUsers();
    }

    @Test
    public void login() {
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
    public void logout() {
        Cookie anyLoginCookie = getLoginCookie(0);
        HttpResponse<String> response = Unirest
                .delete(getURIString("authentication"))
                .header("Cookie", anyLoginCookie.toString())
                .asString();
        assertTrue("The max age of the Cookie should now be 0:", response.getCookies().getNamed(CONSTANTS.COOKIENAME).toString().contains("Max-Age=0"));
    }

    @After
    public void tearDownSession() {
        destroyUsers();
    }

}
