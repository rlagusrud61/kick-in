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

public class MapsTest extends Tests{

    @Before
    public void configureEnvironment() {
        addThreeUsers();
    }

    @Test
    public void getAllMapsWithoutLogin() {
        //this is the most basic request, and if this returns 400,
        // all other will as well due to implementation of filters being deployed on server not class specific.
        HttpResponse<String> getAllMaps = Unirest
                .get(getURIString("/maps"))
                .header("Cookie", "")
                .asString();
        assertEquals("Login without cookie: should result in 400-", getAllMaps.getStatus(), HTTP_BAD_REQUEST);
    }
    @Test
    public void getAllMapsAfterLogin() throws SQLException, NotFoundException {
        long[] mids = addTestMaps();
        for (int i = 0; i< 3; i++) {
            Cookie adminCookie = getLoginCookie(i);

            HttpResponse<JsonNode> getAllEvents = Unirest
                    .get(getURIString("/maps"))
                    .header("Cookie", adminCookie.toString())
                    .asJson();
            assertEquals("After Login Should get all Maps:", HTTP_OK, getAllEvents.getStatus());
            assertTrue("the resultant ok response should have an mapId field for any object: ", getAllEvents.getBody().toString().contains("mapId"));
        }
        deleteTestMap(mids);
    }

    @Test
    public void addDeleteMapAsVisitor() throws SQLException, NotFoundException {
        Cookie getVisitorCookie = getLoginCookie(Roles.VISITOR.getLevel());
        HttpResponse<String> addEvent = Unirest
                .post(getURIString("events"))
                .header("Content-Type", "application/json")
                .header("Cookie", getVisitorCookie.toString())
                .body(testMaps[1])
                .asString();
        assertEquals("You can not add event as a VISITOR", HTTP_FORBIDDEN, addEvent.getStatus());

        long[] mids = addTestMaps();

        HttpResponse<String> deleteEvent = Unirest
                .delete(getURIString("map/" + mids[1]))
                .header("Cookie", getVisitorCookie.toString())
                .asString();
        assertEquals("You can not delete event as VISITOR: ", HTTP_FORBIDDEN, deleteEvent.getStatus());
        deleteTestMap(mids);
    }
    @Test
    public void addDeleteMapAsEditor() {
        Cookie loginCookie = getLoginCookie(Roles.EDITOR.getLevel());
        HttpResponse<String> addMap = Unirest
                .post(getURIString("maps"))
                .header("Content-Type", "application/json")
                .header("Cookie", loginCookie.toString())
                .body(testMaps[1])
                .asString();
        assertEquals("You can add Map as Editor: ", HTTP_CREATED, addMap.getStatus());

        long mid = Long.parseLong(addMap.getBody());

        HttpResponse<String> deleteEvent = Unirest
                .delete(getURIString("map/" + mid))
                .header("Cookie", loginCookie.toString())
                .asString();
        assertEquals("You can delete map as Editor: ", HTTP_NO_CONTENT, deleteEvent.getStatus());
    }
    @Test
    public void addDeleteMapAsAdmin() {
        Cookie getLoginCookie = getLoginCookie(Roles.ADMIN.getLevel());
        HttpResponse<String> addMap = Unirest
                .post(getURIString("maps"))
                .header("Content-Type", "application/json")
                .header("Cookie", getLoginCookie.toString())
                .body(testMaps[1])
                .asString();
        assertEquals("You can add Map as Admin: ", HTTP_CREATED, addMap.getStatus());

        long mid = Long.parseLong(addMap.getBody());

        HttpResponse<String> deleteEvent = Unirest
                .delete(getURIString("map/" + mid))
                .header("Cookie", getLoginCookie.toString())
                .asString();
        assertEquals("You can delete Map as Admin: ", HTTP_NO_CONTENT, deleteEvent.getStatus());
    }

    @Test
    public void saveMapVisitorEditorAdmin() throws SQLException, NotFoundException {
        long[] mids = addTestMaps();
        String newMapDescription = "Test Map1 Description Has now been edited.";
        String newMapName = "Test Map1 Title-Edited.";
        for (int i = 0; i < 3; i++) {
            Cookie loginCookie = getLoginCookie(i);
            HttpResponse<String> saveEvent = Unirest
                    .put(getURIString("map/"+mids[1]))
                    .header("Content-Type", "application/json")
                    .header("Cookie", loginCookie.toString())
                    .body("{" +
                            "\"mapId\":" + mids[1] + ","+
                            "\"name\": \"" + newMapName +"\"," +
                            "\"description\": \""+newMapDescription+"\"" +
                            "}")
                    .asString();
            assertEquals("Save as " +userNames[i]+":", (i == 0) ? HTTP_FORBIDDEN : HTTP_NO_CONTENT, saveEvent.getStatus());
            if (i != 0) {
                assertTrue("Map should now be uptoDate: ", getTestMapById(mids[1]).contains(newMapDescription));
                assertTrue("Map should now be uptoDate: ", getTestMapById(mids[1]).contains(newMapName));
                assertTrue("Map should now be uptoDate: ", getTestMapById(mids[1]).contains("\"lastEditedBy\": \"" + userNames[i] + "\""));
            }
        }
    }

    @After
    public void tearDownSession() {
        destroyUsers();
    }

}
