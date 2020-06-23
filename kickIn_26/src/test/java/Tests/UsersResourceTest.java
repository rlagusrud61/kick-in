package Tests;

import kong.unirest.Cookie;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import nl.utwente.di.team26.Exception.Exceptions.NotFoundException;
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

public class UsersResourceTest extends Tests {

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
    public void getUsers() throws NotFoundException, SQLException {
        long uid = addTestUser();
        for (Roles role : roles) {
            System.out.println("Test " + name.getMethodName() + " for role: " + role.toString());
            Cookie loginCookie = getLoginCookie(role.getLevel());
            HttpResponse<String> getUsers = Unirest
                    .get(getURIString("users"))
                    .header("Content-Type", "application/json")
                    .header("Cookie", loginCookie.toString())
                    .asString();
            switch (role) {
                case VISITOR:
                    assertEquals(HTTP_FORBIDDEN, getUsers.getStatus());
                    break;
                case EDITOR:
                case ADMIN:
                    assertEquals(HTTP_OK, getUsers.getStatus());
                    assertTrue(getUsers.getBody().contains("TestUser"));
                    break;
            }
        }
        deleteTestUser(uid);
    }

    @Test
    public void postUser() throws NotFoundException, SQLException {
        long uid;
        for (Roles role : roles) {
            System.out.println("Test " + name.getMethodName() + " for role: " + role.toString());
            Cookie loginCookie = getLoginCookie(role.getLevel());
            HttpResponse<String> postUser = Unirest
                    .post(getURIString("users"))
                    .header("Content-Type", "application/json")
                    .header("Cookie", loginCookie.toString())
                    .body(testUserInstance)
                    .asString();
            switch (role) {
                case VISITOR:
                case EDITOR:
                    assertEquals(HTTP_FORBIDDEN, postUser.getStatus());
                    break;
                case ADMIN:
                    assertEquals(HTTP_CREATED, postUser.getStatus());
                    uid = Long.parseLong(postUser.getBody());
                    deleteTestUser(uid);
                    break;
            }
        }
    }
}