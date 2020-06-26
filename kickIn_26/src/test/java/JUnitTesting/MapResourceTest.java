package JUnitTesting;

import kong.unirest.Cookie;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import nl.utwente.di.team26.Exception.Exceptions.NotFoundException;
import nl.utwente.di.team26.Product.model.Map.Map;
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

public class MapResourceTest extends Tests{

    long eid;
    long[] mids;

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
    public void getMap() throws NotFoundException, SQLException {
        mids = addTestMaps();
        for (Roles role : roles) {
            System.out.println("Test " + name.getMethodName() + " for role: " + role.toString());
            Cookie loginCookie = getLoginCookie(role.getLevel());
            HttpResponse<String> getEvent = Unirest
                    .get(getURIString("map/" + mids[0]))
                    .header("Content-Type", "application/json")
                    .header("Cookie", loginCookie.toString())
                    .asString();
            switch (role) {
                case VISITOR:
                case EDITOR:
                case ADMIN:
                    assertEquals(HTTP_OK, getEvent.getStatus());
                    assertTrue(getEvent.getBody().contains("TestMap1"));
                    break;
            }
        }
        deleteTestMap(mids);
    }
    @Test
    public void putMap() throws NotFoundException, SQLException {
        String newDescription = "Test Map1 Description Has now been edited.";
        String newName = "Test Map1-Edited.";
        for (Roles role : roles) {
            mids = addTestMaps();
            System.out.println("Test " + name.getMethodName() + " for role: " + role.toString());
            Cookie loginCookie = getLoginCookie(role.getLevel());
            HttpResponse<String> putEvent = Unirest
                    .put(getURIString("map/" + mids[0]))
                    .header("Content-Type", "application/json")
                    .header("Cookie", loginCookie.toString())
                    .body(new Map(mids[0], newName, newDescription))
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
            deleteTestMap(mids);
        }
    }
    @Test
    public void deleteMap() throws NotFoundException, SQLException {
        for (Roles role : roles) {
            mids = addTestMaps();
            System.out.println("Test " + name.getMethodName() + " for role: " + role.toString());
            Cookie loginCookie = getLoginCookie(role.getLevel());
            HttpResponse<String> deleteEvent = Unirest
                    .delete(getURIString("map/" + mids[0]))
                    .header("Content-Type", "application/json")
                    .header("Cookie", loginCookie.toString())
                    .asString();
            switch (role) {
                case VISITOR:
                    assertEquals(HTTP_FORBIDDEN, deleteEvent.getStatus());
                    deleteTestMap(mids);
                    break;
                case EDITOR:
                case ADMIN:
                    assertEquals(HTTP_NO_CONTENT, deleteEvent.getStatus());
                    deleteTestMap(new long[]{mids[1]});
                    break;
            }
        }
    }
}