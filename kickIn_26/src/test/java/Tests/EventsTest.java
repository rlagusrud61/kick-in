package Tests;

import kong.unirest.Cookie;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import nl.utwente.di.team26.Exception.Exceptions.NotFoundException;
import nl.utwente.di.team26.Security.User.Roles;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;

import static java.net.HttpURLConnection.*;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;


public class EventsTest extends Tests{

    @Before
    public void configureEnvironment() {
        addThreeUsers();
    }

    @Test
    public void getAllEventsWithoutLogin() {
        //this is the most basic request, and if this returns 400,
        // all other will as well due to implementation of filters being deployed on server not class specific.
        HttpResponse<String> response = Unirest
                .get(getURIString("/events"))
                .header("Cookie", "")
                .asString();
        assertEquals("Login without cookie: should result in 400-", response.getStatus(), HTTP_BAD_REQUEST);
    }

    @Test
    public void getAllEventsAfterLogin() throws SQLException, NotFoundException {
        long eid = addTestEvent();
        for (int i = 0; i< 3; i++) {
            Cookie adminCookie = getLoginCookie(i);
            System.out.println(adminCookie);
            HttpResponse<JsonNode> getAllEvents = Unirest
                    .get(getURIString("/events"))
                    .header("Cookie", adminCookie.toString())
                    .asJson();
            assertEquals("After Login Should get all Events:", HTTP_OK, getAllEvents.getStatus());
            assertTrue("the resultant ok response should have an eventId field for any object: ", getAllEvents.getBody().toString().contains("eventId"));
        }
        deleteTestEvent(eid);
    }

    @Test
    public void addDeleteEventAsVisitor() throws SQLException, NotFoundException {
        Cookie getVisitorCookie = getLoginCookie(Roles.VISITOR.getLevel());
        HttpResponse<String> addEvent = Unirest
                .post(getURIString("events"))
                .header("Content-Type", "application/json")
                .header("Cookie", getVisitorCookie.toString())
                .body(testEvent)
                .asString();
        assertEquals("You can not add event as a VISITOR", HTTP_FORBIDDEN, addEvent.getStatus());

        long eid = addTestEvent();

        HttpResponse<String> deleteEvent = Unirest
                .delete(getURIString("event/" + eid))
                .header("Cookie", getVisitorCookie.toString())
                .asString();
        assertEquals("You can not delete event as VISITOR: ", HTTP_FORBIDDEN, deleteEvent.getStatus());
        deleteTestEvent(eid);
    }
    @Test
    public void addDeleteEventAsEditor() {
        Cookie getVisitorCookie = getLoginCookie(Roles.EDITOR.getLevel());
        HttpResponse<String> addEvent = Unirest
                .post(getURIString("events"))
                .header("Content-Type", "application/json")
                .header("Cookie", getVisitorCookie.toString())
                .body(testEvent)
                .asString();
        assertEquals("You can add Event as Editor: ", HTTP_CREATED, addEvent.getStatus());

        long eid = Long.parseLong(addEvent.getBody());

        HttpResponse<String> deleteEvent = Unirest
                .delete(getURIString("event/" + eid))
                .header("Cookie", getVisitorCookie.toString())
                .asString();
        assertEquals("You can delete event as VISITOR: ", HTTP_NO_CONTENT, deleteEvent.getStatus());
    }
    @Test
    public void addDeleteEventAsAdmin() {
        Cookie loginCookie = getLoginCookie(Roles.ADMIN.getLevel());
        HttpResponse<String> addEvent = Unirest
                .post(getURIString("events"))
                .header("Content-Type", "application/json")
                .header("Cookie", loginCookie.toString())
                .body(testEvent)
                .asString();
        assertEquals("You can add Event as Admin: ", HTTP_CREATED, addEvent.getStatus());

        long eid = Long.parseLong(addEvent.getBody());

        HttpResponse<String> deleteEvent = Unirest
                .delete(getURIString("event/" + eid))
                .header("Cookie", loginCookie.toString())
                .asString();
        assertEquals("You can delete event as Admin: ", HTTP_NO_CONTENT, deleteEvent.getStatus());
    }

    @Test
    public void addEventWithWrongFormat() {
        //only the date is checked here since all other is not entered by user
        //all other is taken by either the programming code or the is tested above(a successful add) or is just a string

        Cookie loginCookie = getLoginCookie(Roles.ADMIN.getLevel());
        HttpResponse<String> addEvent = Unirest
                .post(getURIString("events"))
                .header("Content-Type", "application/json")
                .header("Cookie", loginCookie.toString())
                .body(testEventInvalid)
                .asString();
        assertEquals("Bad date format should not be accepted: ", HTTP_BAD_REQUEST, addEvent.getStatus());
        assertTrue("Error message should be about Date: ", addEvent.getBody().toLowerCase().contains("date"));
        System.out.println(addEvent.getBody());
    }

    @Test
    public void saveEventVisitorEditorAdmin() throws SQLException, NotFoundException {
        long eid = addTestEvent();
        String newDescription = "Test Event Description Has now been edited.";
        String newEventName = "Test Event Title-Edited.";
        for (int i = 0; i < 3; i++) {
            Cookie loginCookie = getLoginCookie(i);
            HttpResponse<String> saveEvent = Unirest
                    .put(getURIString("event/"+eid))
                    .header("Content-Type", "application/json")
                    .header("Cookie", loginCookie.toString())
                    .body("{" +
                            "\"eventId\":" + eid + ","+
                            "\"name\": \"" + newEventName +"\"," +
                            "\"description\": \""+newDescription+"\"," +
                            "\"location\": \"On Campus\"," +
                            "\"date\": \"2020-01-02\"" +
                            "}")
                    .asString();
            assertEquals("Save as " +userNames[i]+":", (i == 0) ? HTTP_FORBIDDEN : HTTP_NO_CONTENT, saveEvent.getStatus());
            if (i != 0) {
                assertTrue("Event should now be uptoDate: ", getTestEventById(eid).contains(newDescription));
                assertTrue("Event should now be uptoDate: ", getTestEventById(eid).contains(newEventName));
                assertTrue("Event should now be uptoDate: ", getTestEventById(eid).contains("\"lastEditedBy\": \"" + userNames[i] + "\""));
            }
        }
    }

    @After
    public void tearDownSession() {
        destroyUsers();
    }

}
