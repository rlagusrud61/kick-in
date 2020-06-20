package Tests;

import kong.unirest.Cookie;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import nl.utwente.di.team26.Exception.Exceptions.NotFoundException;
import nl.utwente.di.team26.Product.model.Event.EventMap;
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

public class EventMapTests extends Tests {

    long eid;
    long[] mids;

    @Rule
    public TestName name = new TestName();

    @Before
    public void configureEnvironment() throws SQLException {
        addThreeUsers();
        eid = addTestEvent();
        mids = addTestMaps();
    }

    @Test
    public void createARelation() throws NotFoundException, SQLException {
        for (Roles role : roles) {
            System.out.println("Test " + name.getMethodName() + " for role: " + role.toString());
            Cookie loginCookie = getLoginCookie(role.getLevel());
            HttpResponse<String> response = Unirest
                    .post(getURIString("eventMap"))
                    .header("Content-Type", "application/json")
                    .header("Cookie", loginCookie.toString())
                    .body(new EventMap(eid, mids[0]))
                    .asString();
            switch (role) {
                case VISITOR:
                    assertEquals(response.getStatus(), HTTP_FORBIDDEN);
                    break;
                case EDITOR:
                case ADMIN:
                    assertEquals(response.getStatus(), HTTP_CREATED);
                    deleteRelations(eid, new long[]{mids[0]});
                    break;
            }
        }
    }
    @Test
    public void deleteARelation() throws NotFoundException, SQLException {
        for (Roles role : roles) {
            createRelations(eid, new long[]{mids[0]});
            System.out.println("Test " + name.getMethodName() + " for role: " + role.toString());
            Cookie loginCookie = getLoginCookie(role.getLevel());
            HttpResponse<String> response = Unirest
                    .delete(getURIString("eventMap/"+eid+"/"+mids[0]))
                    .header("Cookie", loginCookie.toString())
                    .asString();
            switch (role) {
                case VISITOR:
                    assertEquals(response.getStatus(), HTTP_FORBIDDEN);
                    deleteRelations(eid, new long[]{mids[0]});
                    break;
                case EDITOR:
                case ADMIN:
                    assertEquals(response.getStatus(), HTTP_NO_CONTENT);
                    break;
            }
        }
    }
    @Test
    public void deleteAllRelations() throws NotFoundException, SQLException {
        for (Roles role : roles) {
            createRelations(eid, mids);
            System.out.println("Test " + name.getMethodName() + " for role: " + role.toString());
            Cookie loginCookie = getLoginCookie(role.getLevel());
            HttpResponse<String> response = Unirest
                    .delete(getURIString("eventMap"))
                    .header("Cookie", loginCookie.toString())
                    .asString();
            switch (role) {
                case VISITOR:
                case EDITOR:
                    assertEquals(response.getStatus(), HTTP_FORBIDDEN);
                    deleteRelations(eid, mids);
                    break;
                case ADMIN:
                    assertEquals(response.getStatus(), HTTP_NO_CONTENT);
                    break;
            }
        }
    }
    @Test
    public void generateMapListForEvent() throws SQLException, NotFoundException {
        createRelations(eid, mids);
        for (Roles role : roles) {
            System.out.println("Test " + name.getMethodName() + " for role: " + role.toString());
            Cookie loginCookie = getLoginCookie(role.getLevel());
            HttpResponse<String> response = Unirest
                    .get(getURIString("eventMap/event/"+eid))
                    .header("Cookie", loginCookie.toString())
                    .asString();
            switch (role) {
                case VISITOR:
                case EDITOR:
                case ADMIN:
                    assertEquals(response.getStatus(), HTTP_OK);
                    assertTrue(response.getBody().contains(String.valueOf(mids[0])));
                    assertTrue(response.getBody().contains(String.valueOf(mids[1])));
                    break;
            }
        }
        deleteRelations(eid, mids);
    }
    @Test
    public void generateEventListForMap() throws SQLException, NotFoundException {
        createRelations(eid, mids);
        for (Roles role : roles) {
            System.out.println("Test " + name.getMethodName() + " for role: " + role.toString());
            Cookie loginCookie = getLoginCookie(role.getLevel());
            HttpResponse<String> response = Unirest
                    .get(getURIString("eventMap/map/"+mids[0]))
                    .header("Cookie", loginCookie.toString())
                    .asString();
            switch (role) {
                case VISITOR:
                case EDITOR:
                case ADMIN:
                    assertEquals(response.getStatus(), HTTP_OK);
                    assertTrue(response.getBody().contains(String.valueOf(eid)));
                    break;
            }
        }
        deleteRelations(eid, mids);
    }
    @Test
    public void clearEvent() throws NotFoundException, SQLException {
        for (Roles role : roles) {
            createRelations(eid, mids);
            System.out.println("Test " + name.getMethodName() + " for role: " + role.toString());
            Cookie loginCookie = getLoginCookie(role.getLevel());
            HttpResponse<String> response = Unirest
                    .delete(getURIString("eventMap/event/"+eid))
                    .header("Cookie", loginCookie.toString())
                    .asString();
            switch (role) {
                case VISITOR:
                    assertEquals(HTTP_FORBIDDEN, response.getStatus());
                    deleteRelations(eid, mids);
                    break;
                case EDITOR:
                case ADMIN:
                    assertEquals(HTTP_NO_CONTENT, response.getStatus());
                    break;
            }
        }
    }

    @After
    public void closeSession() throws NotFoundException, SQLException {
        deleteTestMap(mids);
        deleteTestEvent(eid);
        destroyUsers();
    }

}
