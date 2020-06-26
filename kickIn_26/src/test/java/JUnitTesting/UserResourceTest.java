package JUnitTesting;

import kong.unirest.Cookie;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import nl.utwente.di.team26.Exception.Exceptions.NotFoundException;
import nl.utwente.di.team26.Product.model.Authentication.User;
import nl.utwente.di.team26.Security.User.Roles;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import java.sql.SQLException;

import static java.net.HttpURLConnection.*;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class UserResourceTest extends Tests{

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
    public void getUser() throws NotFoundException, SQLException {
        long uid = addTestUser();
        for (Roles role : roles) {
            System.out.println("Test " + name.getMethodName() + " for role: " + role.toString());
            Cookie loginCookie = getLoginCookie(role.getLevel());
            HttpResponse<String> getUser = Unirest
                    .get(getURIString("user/" + uid))
                    .header("Content-Type", "application/json")
                    .header("Cookie", loginCookie.toString())
                    .asString();
            switch (role) {
                case VISITOR:
                case EDITOR:
                    assertEquals(HTTP_FORBIDDEN, getUser.getStatus());
                    break;
                case ADMIN:
                    assertEquals(HTTP_OK, getUser.getStatus());
                    assertTrue(getUser.getBody().contains("TestUser"));
                    break;
            }
        }
        deleteTestUser(uid);
    }

    @Test
    public void putUser() throws NotFoundException, SQLException {
        String newUserPassword = "password-2";
        String newUserEmail = "testusernewemail@email.com";
        String newUserNickname = "TestUser-Edited";
        for (Roles role : roles) {
            long uid = addTestUser();
            System.out.println("Test " + name.getMethodName() + " for role: " + role.toString());
            Cookie loginCookie = getLoginCookie(role.getLevel());
            HttpResponse<String> putUser = Unirest
                    .put(getURIString("user/" + uid))
                    .header("Content-Type", "application/json")
                    .header("Cookie", loginCookie.toString())
                    .body(new User(uid, newUserEmail, newUserPassword, newUserNickname, 1))
                    .asString();
            switch (role) {
                case VISITOR:
                case EDITOR:
                    assertEquals(HTTP_FORBIDDEN, putUser.getStatus());
                    break;
                case ADMIN:
                    assertEquals(HTTP_NO_CONTENT, putUser.getStatus());
                    break;
            }
            deleteTestUser(uid);
        }
    }

    @Test
    public void deleteUser() throws NotFoundException, SQLException {
        for (Roles role : roles) {
            long uid = addTestUser();
            System.out.println("Test " + name.getMethodName() + " for role: " + role.toString());
            Cookie loginCookie = getLoginCookie(role.getLevel());
            HttpResponse<String> deleteUser = Unirest
                    .delete(getURIString("user/" + uid))
                    .header("Content-Type", "application/json")
                    .header("Cookie", loginCookie.toString())
                    .asString();
            switch (role) {
                case VISITOR:
                case EDITOR:
                    assertEquals(HTTP_FORBIDDEN, deleteUser.getStatus());
                    deleteTestUser(uid);
                    break;
                case ADMIN:
                    assertEquals(HTTP_NO_CONTENT, deleteUser.getStatus());
                    break;
            }
        }
    }
}