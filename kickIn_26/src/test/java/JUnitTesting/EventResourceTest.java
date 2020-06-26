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

public class EventResourceTest extends Tests{

    long eid;

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
    public void getEvent() throws NotFoundException, SQLException {
        eid = addTestEvent();
        for (Roles role : roles) {
            System.out.println("Test " + name.getMethodName() + " for role: " + role.toString());
            Cookie loginCookie = getLoginCookie(role.getLevel());
            HttpResponse<String> getEvent = Unirest
                    .get(getURIString("event/" + eid))
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
        deleteTestEvent(eid);
    }

    @Test
    public void putEvent() throws NotFoundException, SQLException {
        String newDescription = "Test Event Description Has now been edited.";
        String newEventName = "Test Event Title-Edited.";
        for (Roles role : roles) {
            eid = addTestEvent();
            System.out.println("Test " + name.getMethodName() + " for role: " + role.toString());
            Cookie loginCookie = getLoginCookie(role.getLevel());
            HttpResponse<String> putEvent = Unirest
                    .put(getURIString("event/" + eid))
                    .header("Content-Type", "application/json")
                    .header("Cookie", loginCookie.toString())
                    .body(new Event(eid, newEventName, newDescription, "OnCampus", "2020-01-01"))
                    .asString();
            switch (role) {
                case VISITOR:
                    assertEquals(HTTP_FORBIDDEN, putEvent.getStatus());
                    break;
                case EDITOR:
                case ADMIN:
                    assertEquals(HTTP_NO_CONTENT, putEvent.getStatus());
                    break;
            }
            deleteTestEvent(eid);
        }
    }

    @Test
    public void deleteEvent() throws NotFoundException, SQLException {
        for (Roles role : roles) {
            eid = addTestEvent();
            System.out.println("Test " + name.getMethodName() + " for role: " + role.toString());
            Cookie loginCookie = getLoginCookie(role.getLevel());
            HttpResponse<String> deleteEvent = Unirest
                    .delete(getURIString("event/" + eid))
                    .header("Content-Type", "application/json")
                    .header("Cookie", loginCookie.toString())
                    .asString();
            switch (role) {
                case VISITOR:
                    assertEquals(HTTP_FORBIDDEN, deleteEvent.getStatus());
                    deleteTestEvent(eid);
                    break;
                case EDITOR:
                case ADMIN:
                    assertEquals(HTTP_NO_CONTENT, deleteEvent.getStatus());
                    break;
            }
        }
    }
}