package Tests;

import kong.unirest.Cookie;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import nl.utwente.di.team26.CONSTANTS;
import nl.utwente.di.team26.Exception.Exceptions.NotFoundException;
import nl.utwente.di.team26.Product.model.TypeOfResource.Drawing;
import nl.utwente.di.team26.Product.model.TypeOfResource.Material;
import nl.utwente.di.team26.Security.User.Roles;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;

import static java.net.HttpURLConnection.*;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class ResourcesTest extends Tests {

    @Rule
    public TestName name = new TestName();

    @Before
    public void configureEnvironment() {
        addThreeUsers();
    }

    //@Path("/resources")
    @Test
    public void getAllResources() throws NotFoundException, SQLException {
        long[] rids = addResources();
        for (Roles role : roles) {
            System.out.println("Test " + name.getMethodName() + " for role: " + role.toString());
            Cookie loginCookie = getLoginCookie(role.getLevel());
            HttpResponse<String> response = Unirest
                    .get(getURIString("resources"))
                    .header("Cookie", loginCookie.toString())
                    .asString();
            //statement
            switch (role) {
                case VISITOR:
                case EDITOR:
                case ADMIN:
                    assertEquals(HTTP_OK, response.getStatus());
                    assertTrue(response.getBody().contains("TestMaterial"));
                    assertTrue(response.getBody().contains("TestDrawing"));
                    break;
            }
        }
        deleteResources(rids);
    }

//    @Test
//    public void deleteAllResources() throws NotFoundException, SQLException {
//        Connection conn = CONSTANTS.getConnection();
//        conn.setAutoCommit(false);
//        Savepoint sp = conn.setSavepoint("BeforeNuke");
//        for (Roles role : roles) {
//            long[] rids = addResources();
//            System.out.println("Test " + name.getMethodName() + " for role: " + role.toString());
//            Cookie loginCookie = getLoginCookie(role.getLevel());
//            HttpResponse<String> response = Unirest
//                    .delete(getURIString("resources"))
//                    .header("Cookie", loginCookie.toString())
//                    .asString();
//            //statement
//            switch (role) {
//                case VISITOR:
//                case EDITOR:
//                    assertEquals(HTTP_FORBIDDEN, response.getStatus());
//                    deleteResources(rids);
//                    break;
//                case ADMIN:
//                    assertEquals(HTTP_NO_CONTENT, response.getStatus());
//                    break;
//            }
//        }
//        conn.rollback(sp);
//        conn.setAutoCommit(true);
//        conn.close();
//    }

    //@Path("/resource/{resourceId}")
    @Test
    public void getResource() throws NotFoundException, SQLException {
        long[] rids = addResources();
        for (Roles role : roles) {
            System.out.println("Test " + name.getMethodName() + " for role: " + role.toString());
            Cookie loginCookie = getLoginCookie(role.getLevel());
            HttpResponse<String> response = Unirest
                    .get(getURIString("resource/"+rids[0]))
                    .header("Cookie", loginCookie.toString())
                    .asString();
            //statement
            switch (role) {
                case VISITOR:
                case EDITOR:
                case ADMIN:
                    assertEquals(HTTP_OK, response.getStatus());
                    assertTrue(response.getBody().contains("TestMaterial"));
                    break;
            }
        }
        deleteResources(rids);
    }

    @Test
    public void deleteResource() throws NotFoundException, SQLException {
        for (Roles role : roles) {
            long[] rids = addResources();
            System.out.println("Test " + name.getMethodName() + " for role: " + role.toString());
            Cookie loginCookie = getLoginCookie(role.getLevel());
            HttpResponse<String> response = Unirest
                    .delete(getURIString("resource/"+rids[0]))
                    .header("Cookie", loginCookie.toString())
                    .asString();
            //statement
            switch (role) {
                case VISITOR:
                case EDITOR:
                    assertEquals(HTTP_FORBIDDEN, response.getStatus());
                    deleteResources(rids);
                    break;
                case ADMIN:
                    assertEquals(HTTP_NO_CONTENT, response.getStatus());
                    break;
            }
        }
    }

    //@Path("/resources/drawing")
    @Test
    public void getDrawing() throws NotFoundException, SQLException {
        long[] rids = addResources();
        for (Roles role : roles) {
            System.out.println("Test " + name.getMethodName() + " for role: " + role.toString());
            Cookie loginCookie = getLoginCookie(role.getLevel());
            HttpResponse<String> response = Unirest
                    .get(getURIString("resources/drawing"))
                    .header("Cookie", loginCookie.toString())
                    .asString();
            //statement
            switch (role) {
                case VISITOR:
                case EDITOR:
                case ADMIN:
                    assertEquals(HTTP_OK, response.getStatus());
                    assertTrue(response.getBody().contains("TestDrawing"));
                    break;
            }
        }
        deleteResources(rids);
    }

    @Test
    public void postDrawing() throws NotFoundException, SQLException {
        for (Roles role : roles) {
            System.out.println("Test " + name.getMethodName() + " for role: " + role.toString());
            Cookie loginCookie = getLoginCookie(role.getLevel());
            HttpResponse<String> response = Unirest
                    .post(getURIString("resources/drawing"))
                    .header("Content-Type", "application/json")
                    .header("Cookie", loginCookie.toString())
                    .body(new Drawing("TestDrawing2", "TestDrawing2", "SomeImage"))
                    .asString();
            //statement
            switch (role) {
                case VISITOR:
                case EDITOR:
                    assertEquals(HTTP_FORBIDDEN, response.getStatus());
                    break;
                case ADMIN:
                    assertEquals(HTTP_CREATED, response.getStatus());
                    long rid = Long.parseLong(response.getBody());
                    deleteResources(new long[]{rid});
            }
        }
    }

    @Test
    public void putDrawing() throws NotFoundException, SQLException {
        for (Roles role : roles) {
            long[] rids = addResources();
            System.out.println("Test " + name.getMethodName() + " for role: " + role.toString());
            Cookie loginCookie = getLoginCookie(role.getLevel());
            HttpResponse<String> response = Unirest
                    .put(getURIString("resources/drawing/"+rids[1]))
                    .header("Content-Type", "application/json")
                    .header("Cookie", loginCookie.toString())
                    .body(new Drawing(rids[1], "TestDrawing2", "TestDrawing2", "SomeImage"))
                    .asString();
            //statement
            switch (role) {
                case VISITOR:
                case EDITOR:
                    assertEquals(HTTP_FORBIDDEN, response.getStatus());
                    break;
                case ADMIN:
                    assertEquals(HTTP_NO_CONTENT, response.getStatus());
                    break;
            }
            deleteResources(rids);
        }
    }

    //@Path("/resources/material")
    @Test
    public void getMaterial() throws NotFoundException, SQLException {
        long[] rids = addResources();
        for (Roles role : roles) {
            System.out.println("Test " + name.getMethodName() + " for role: " + role.toString());
            Cookie loginCookie = getLoginCookie(role.getLevel());
            HttpResponse<String> response = Unirest
                    .get(getURIString("resources/material"))
                    .header("Cookie", loginCookie.toString())
                    .asString();
            //statement
            switch (role) {
                case VISITOR:
                case EDITOR:
                case ADMIN:
                    assertEquals(HTTP_OK, response.getStatus());
                    assertTrue(response.getBody().contains("TestMaterial"));
                    break;
            }
        }
        deleteResources(rids);
    }

    @Test
    public void putMaterial() throws NotFoundException, SQLException {
        for (Roles role : roles) {
            long[] rids = addResources();
            System.out.println("Test " + name.getMethodName() + " for role: " + role.toString());
            Cookie loginCookie = getLoginCookie(role.getLevel());
            HttpResponse<String> response = Unirest
                    .put(getURIString("resources/material/"+rids[0]))
                    .header("Content-Type", "application/json")
                    .header("Cookie", loginCookie.toString())
                    .body(new Material(rids[0], "TestMaterial2", "TestMaterial2", "SomeImage2"))
                    .asString();
            //statement
            switch (role) {
                case VISITOR:
                case EDITOR:
                    assertEquals(HTTP_FORBIDDEN, response.getStatus());
                    break;
                case ADMIN:
                    System.out.println(response.getBody());
                    assertEquals(HTTP_NO_CONTENT, response.getStatus());
            }
            deleteResources(rids);
        }
    }

    @Test
    public void postMaterial() throws NotFoundException, SQLException {
        for (Roles role : roles) {
            System.out.println("Test " + name.getMethodName() + " for role: " + role.toString());
            Cookie loginCookie = getLoginCookie(role.getLevel());
            HttpResponse<String> response = Unirest
                    .post(getURIString("resources/material"))
                    .header("Content-Type", "application/json")
                    .header("Cookie", loginCookie.toString())
                    .body(new Material("TestMaterial2", "TestMaterial2", "someImageInBase64"))
                    .asString();
            //statement
            switch (role) {
                case VISITOR:
                case EDITOR:
                    assertEquals(HTTP_FORBIDDEN, response.getStatus());
                    break;
                case ADMIN:
                    assertEquals(HTTP_CREATED, response.getStatus());
                    long rid = Long.parseLong(response.getBody());
                    deleteResources(new long[]{rid});
            }
        }
    }

    @After
    public void closeSession() {
        destroyUsers();
    }


}
