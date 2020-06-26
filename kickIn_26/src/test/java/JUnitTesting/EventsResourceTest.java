package JUnitTesting;

import kong.unirest.Cookie;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import nl.utwente.di.team26.Exception.Exceptions.NotFoundException;
import nl.utwente.di.team26.Product.model.Event.Event;
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

public class EventsResourceTest extends Tests {

    long eid;

    @Before
    public void setUp() throws SQLException {
        addThreeUsers();
        eid = addTestEvent();
    }

    @After
    public void tearDown() throws NotFoundException, SQLException {
        deleteTestEvent(eid);
        destroyUsers();
    }

    @Rule
    public TestName name = new TestName();

    @Test
    public void getEvents() {
        for (Roles role : roles) {
            System.out.println("Test " + name.getMethodName() + " for role: " + role.toString());
            Cookie loginCookie = getLoginCookie(role.getLevel());
            HttpResponse<String> getEvent = Unirest
                    .get(getURIString("events"))
                    .header("Content-Type", "application/json")
                    .header("Cookie", loginCookie.toString())
                    .asString();
            switch (role) {
                case VISITOR:
                case EDITOR:
                case ADMIN:
                    assertEquals(HTTP_OK, getEvent.getStatus());
                    assertTrue(getEvent.getBody().contains("TestEvent"));
                    break;
            }
        }
    }

    @Test
    public void postEvent() throws NotFoundException, SQLException {
        long eid2;
        for (Roles role : roles) {
            System.out.println("Test " + name.getMethodName() + " for role: " + role.toString());
            Cookie loginCookie = getLoginCookie(role.getLevel());
            HttpResponse<String> postEvent = Unirest
                    .post(getURIString("events"))
                    .header("Content-Type", "application/json")
                    .header("Cookie", loginCookie.toString())
                    .body(testEventInstance)
                    .asString();
            switch (role) {
                case VISITOR:
                    assertEquals(HTTP_FORBIDDEN, postEvent.getStatus());
                    break;
                case EDITOR:
                case ADMIN:
                    assertEquals(HTTP_CREATED, postEvent.getStatus());
                    eid2 = Long.parseLong(postEvent.getBody());
                    deleteTestEvent(eid2);
                    break;
            }
        }
    }

    @Test
    public void postEventWrongFormat() {
        for (Roles role : roles) {
            System.out.println("Test " + name.getMethodName() + " for role: " + role.toString());
            Cookie loginCookie = getLoginCookie(role.getLevel());
            HttpResponse<String> postEvent = Unirest
                    .post(getURIString("events"))
                    .header("Content-Type", "application/json")
                    .header("Cookie", loginCookie.toString())
                    .body(new Event("wrongDate", "wrongDate", "On Campus", "1-234-32"))
                    .asString();
            switch (role) {
                case VISITOR:
                    assertEquals(HTTP_FORBIDDEN, postEvent.getStatus());
                    break;
                case EDITOR:
                case ADMIN:
                    assertEquals(HTTP_BAD_REQUEST, postEvent.getStatus());
                    assertTrue(postEvent.getBody().toLowerCase().contains("date"));
                    break;
            }
        }
    }

/*
    @Test
    public void deleteAllEvents() {
        for (Roles role : roles) {
            System.out.println("Test " + name.getMethodName() + " for role: " + role.toString());
            Cookie loginCookie = getLoginCookie(role.getLevel());
            HttpResponse<String> deleteEvent = Unirest
                    .delete(getURIString("events"))
                    .header("Content-Type", "application/json")
                    .header("Cookie", loginCookie.toString())
                    .asString();
            switch (role) {
                case VISITOR:
                    assertEquals(HTTP_FORBIDDEN, deleteEvent.getStatus());
                    break;
                case EDITOR:
                case ADMIN:
                    assertEquals(HTTP_NO_CONTENT, deleteEvent.getStatus());
                    break;
            }
        }
    }
*/
}