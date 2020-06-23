package Tests;

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

public class MapsResourceTest extends Tests {

    long[] mids;

    @Rule
    public TestName name = new TestName();

    @Before
    public void configureEnvironment() {
        addThreeUsers();
    }

    @Test
    public void getMaps() throws NotFoundException, SQLException {
        mids = addTestMaps();
        for (Roles role : roles) {
            System.out.println("Test " + name.getMethodName() + " for role: " + role.toString());
            Cookie loginCookie = getLoginCookie(role.getLevel());
            HttpResponse<String> getEvent = Unirest
                    .get(getURIString("maps"))
                    .header("Content-Type", "application/json")
                    .header("Cookie", loginCookie.toString())
                    .asString();
            switch (role) {
                case VISITOR:
                case EDITOR:
                case ADMIN:
                    assertEquals(HTTP_OK, getEvent.getStatus());
                    assertTrue(getEvent.getBody().contains("TestMap1"));
                    assertTrue(getEvent.getBody().contains("TestMap2"));
                    break;
            }
        }
        deleteTestMap(mids);
    }
    @Test
    public void postMap() throws NotFoundException, SQLException {
        for (Roles role : roles) {
            System.out.println("Test " + name.getMethodName() + " for role: " + role.toString());
            Cookie loginCookie = getLoginCookie(role.getLevel());
            HttpResponse<String> postMap = Unirest
                    .post(getURIString("maps"))
                    .header("Content-Type", "application/json")
                    .header("Cookie", loginCookie.toString())
                    .body(new Map("newName", "newDescription"))
                    .asString();
            switch (role) {
                case VISITOR:
                    assertEquals(HTTP_FORBIDDEN, postMap.getStatus());
                    break;
                case EDITOR:
                case ADMIN:
                    assertEquals(HTTP_CREATED, postMap.getStatus());
                    long mid = Long.parseLong(postMap.getBody());
                    deleteTestMap(new long[]{mid});
                    break;
            }
        }
    }
/*
    @Test
    public void deleteMaps() throws NotFoundException, SQLException {
        for (Roles role : roles) {
            mids = addTestMaps();
            System.out.println("Test " + name.getMethodName() + " for role: " + role.toString());
            Cookie loginCookie = getLoginCookie(role.getLevel());
            HttpResponse<String> deleteEvent = Unirest
                    .delete(getURIString("maps"))
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
                    break;
            }
        }
    }
*/


    @After
    public void closeSession() {
        destroyUsers();
    }

}