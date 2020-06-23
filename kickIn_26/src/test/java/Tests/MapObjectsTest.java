package Tests;

import kong.unirest.Cookie;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;
import nl.utwente.di.team26.Exception.Exceptions.NotFoundException;
import nl.utwente.di.team26.Product.model.Map.MapObject;
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

public class MapObjectsTest extends Tests {

    long eid;
    long[] mids;
    long[] rids;

    @Rule
    public TestName name = new TestName();

    @Before
    public void configureEnvironment() throws SQLException {
        addThreeUsers();
        eid = addTestEvent();
        mids = addTestMaps();
        createRelations(eid, mids);
        rids = addResources();
    }

    @Test
    public void addObjectsToMap() throws NotFoundException, SQLException {
        for (Roles role : roles) {
            System.out.println("Test " + name.getMethodName() + " for role: " + role.toString());
            Cookie loginCookie = getLoginCookie(role.getLevel());
            HttpResponse<String> addObject = Unirest
                    .post(getURIString("objects/"+mids[0]))
                    .header("Content-Type", "application/json")
                    .header("Cookie", loginCookie.toString())
                    .body(new MapObject[]{new MapObject(mids[0], 23, "Corners")})
                    .asString();
            HttpResponse<String> allObjOnMap = Unirest
                    .get(getURIString("objects/"+mids[0]))
                    .header("Cookie", loginCookie.toString())
                    .asString();
            switch (role) {
                case VISITOR:
                    assertEquals(HTTP_FORBIDDEN, addObject.getStatus());
                    break;
                case EDITOR:
                case ADMIN:
                    assertEquals(HTTP_NO_CONTENT, addObject.getStatus());
                    assertTrue(allObjOnMap.getBody().contains(String.valueOf(23)));
                    clearMap(mids[0]);
                    break;
            }
        }
    }
    @Test
    public void generateReports() throws NotFoundException, SQLException {
        addObjectsToMap(mids[0]);
        for (Roles role : roles) {
            System.out.println("Test " + name.getMethodName() + " for role: " + role.toString());
            Cookie loginCookie = getLoginCookie(role.getLevel());
            HttpResponse<String> mapReport = Unirest
                    .get(getURIString("objects/"+mids[0]+"/report"))
                    .header("Cookie", loginCookie.toString())
                    .asString();
            HttpResponse<String> eventReport = Unirest
                    .get(getURIString("event/"+eid))
                    .header("Cookie", loginCookie.toString())
                    .asString();
            switch (role) {
                case VISITOR:
                case EDITOR:
                case ADMIN:
                    assertEquals(HTTP_OK, mapReport.getStatus());
                    assertTrue(mapReport.getBody().contains("fence"));
                    assertTrue(eventReport.getBody().contains("fence"));
                    assertTrue(eventReport.getBody().contains(mapReport.getBody()));
                    break;
            }
        }
        clearMap(mids[0]);
    }
    @Test
    public void deleteObjectFromMap() throws NotFoundException, SQLException {
        for (Roles role : roles) {
            long oid = addObjectsToMap(mids[0]);
            System.out.println("Test " + name.getMethodName() + " for role: " + role.toString());
            Cookie loginCookie = getLoginCookie(role.getLevel());
            HttpResponse<String> response = Unirest
                    .delete(getURIString("objects/selected/"+mids[0]))
                    .header("Content-Type", "application/json")
                    .header("Cookie", loginCookie.toString())
                    .body(new long[]{oid})
                    .asString();
            switch (role) {
                case VISITOR:
                    assertEquals(HTTP_FORBIDDEN, response.getStatus());
                    clearMap(mids[0]);
                    break;
                case EDITOR:
                case ADMIN:
                    assertEquals(HTTP_NO_CONTENT, response.getStatus());
                    break;
            }
        }
    }
    @Test
    public void clearMap() throws NotFoundException, SQLException {
        for (Roles role : roles) {
            long oid = addObjectsToMap(mids[0]);
            System.out.println("Test " + name.getMethodName() + " for role: " + role.toString());
            Cookie loginCookie = getLoginCookie(role.getLevel());
            HttpResponse<String> response = Unirest
                    .delete(getURIString("objects/"+mids[0]))
                    .header("Cookie", loginCookie.toString())
                    .body(new MapObject(oid))
                    .asString();
            switch (role) {
                case VISITOR:
                    assertEquals(HTTP_FORBIDDEN, response.getStatus());
                    clearMap(mids[0]);
                    break;
                case EDITOR:
                case ADMIN:
                    assertEquals(HTTP_NO_CONTENT, response.getStatus());
                    break;
            }
        }
    }
    @Test
    public void changeObjectOnMap() throws NotFoundException, SQLException {
        for (Roles role : roles) {
            long oid = addObjectsToMap(mids[0]);
            System.out.println("Test " + name.getMethodName() + " for role: " + role.toString());
            Cookie loginCookie = getLoginCookie(role.getLevel());
            HttpResponse<String> response = Unirest
                    .put(getURIString("objects/selected/"+mids[0]))
                    .header("Content-Type", "application/json")
                    .header("Cookie", loginCookie.toString())
                    .body(new MapObject[]{new MapObject(oid, mids[0], 23, "Corners-2")})
                    .asString();
            switch (role) {
                case VISITOR:
                    assertEquals(HTTP_FORBIDDEN, response.getStatus());
                    break;
                case EDITOR:
                case ADMIN:
                    assertEquals(HTTP_NO_CONTENT, response.getStatus());
                    break;
            }
            clearMap(mids[0]);
        }
    }


    @After
    public void closeSession() throws NotFoundException, SQLException {
        deleteResources(rids);
        deleteRelations(eid, mids);
        deleteTestMap(mids);
        deleteTestEvent(eid);
        destroyUsers();
    }

}
