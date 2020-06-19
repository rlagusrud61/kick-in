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

public class UsersTest extends Tests{

    @Before
    public void configureEnvironment() {
        addThreeUsers();
    }

    @Test
    public void getAllUsersWithoutLogin() {
        //this is the most basic request, and if this returns 400,
        // all other will as well due to implementation of filters being deployed on server not class specific.
        HttpResponse<String> response = Unirest
                .get(getURIString("/users"))
                .header("Cookie", "")
                .asString();
        assertEquals("Login without cookie: should result in 400-", response.getStatus(), HTTP_BAD_REQUEST);
    }

    @Test
    public void getAllUsersAfterLogin() throws SQLException, NotFoundException {
        long uid = addTestUser();
        for (int i = 0; i< 3; i++) {
            Cookie loginCookie = getLoginCookie(i);
            HttpResponse<JsonNode> getAllUsers = Unirest
                    .get(getURIString("/users"))
                    .header("Cookie", loginCookie.toString())
                    .asJson();
            if (i != 0) {
                assertEquals("After Login Should get all Users if Editor or Admin:", HTTP_OK, getAllUsers.getStatus());
                assertTrue("the resultant ok response should have an userId field for any object: ", getAllUsers.getBody().toString().contains("userId"));
            } else {
                assertEquals("Visitor should not get any User info: ", HTTP_FORBIDDEN, getAllUsers.getStatus());
            }
        }
        deleteTestUser(uid);
    }

    @Test
    public void addDeleteUserAsVisitor() throws SQLException, NotFoundException {
        Cookie getVisitorCookie = getLoginCookie(Roles.VISITOR.getLevel());
        HttpResponse<String> adduser = Unirest
                .post(getURIString("users"))
                .header("Content-Type", "application/json")
                .header("Cookie", getVisitorCookie.toString())
                .body(testUser)
                .asString();
        assertEquals("You can not add user as a VISITOR", HTTP_FORBIDDEN, adduser.getStatus());

        long uid = addTestUser();

        HttpResponse<String> deleteuser = Unirest
                .delete(getURIString("user/" + uid))
                .header("Cookie", getVisitorCookie.toString())
                .asString();
        assertEquals("You can not delete user as VISITOR: ", HTTP_FORBIDDEN, deleteuser.getStatus());
        deleteTestUser(uid);
    }
    @Test
    public void addDeleteUserAsEditor() throws NotFoundException, SQLException {
        Cookie getVisitorCookie = getLoginCookie(Roles.EDITOR.getLevel());
        HttpResponse<String> adduser = Unirest
                .post(getURIString("users"))
                .header("Content-Type", "application/json")
                .header("Cookie", getVisitorCookie.toString())
                .body(testUser)
                .asString();
        assertEquals("You can not add user as Editor: ", HTTP_FORBIDDEN, adduser.getStatus());

        long uid = addTestUser();
        HttpResponse<String> deleteuser = Unirest
                .delete(getURIString("user/" + uid))
                .header("Cookie", getVisitorCookie.toString())
                .asString();
        assertEquals("You can not delete user as Editor: ", HTTP_FORBIDDEN, deleteuser.getStatus());
        deleteTestUser(uid);
    }
    @Test
    public void addDeleteUserAsAdmin() {
        Cookie loginCookie = getLoginCookie(Roles.ADMIN.getLevel());
        HttpResponse<String> addUser = Unirest
                .post(getURIString("users"))
                .header("Content-Type", "application/json")
                .header("Cookie", loginCookie.toString())
                .body(testUser)
                .asString();
        assertEquals("You can add user as Admin: ", HTTP_CREATED, addUser.getStatus());

        long uid = Long.parseLong(addUser.getBody());
        HttpResponse<String> deleteUser = Unirest
                .delete(getURIString("user/" + uid))
                .header("Cookie", loginCookie.toString())
                .asString();
        assertEquals("You can delete user as Admin: ", HTTP_NO_CONTENT, deleteUser.getStatus());
    }

    @Test
    public void saveUserVisitorEditorAdmin() throws SQLException, NotFoundException {
        long eid = addTestUser();
        String newUserPassword = "password-2";
        String newUserEmail = "testusernewemail@email.com";
        String newUserNickname = "TestUser-Edited";
        int newClearanceLevel = -1;
        for (int i = 0; i < 3; i++) {
            Cookie loginCookie = getLoginCookie(i);
            HttpResponse<String> saveuser = Unirest
                    .put(getURIString("user/"+eid))
                    .header("Content-Type", "application/json")
                    .header("Cookie", loginCookie.toString())
                    .body("{" +
                            "\"userId\":" + eid + ","+
                            "\"email\": \"" + newUserEmail +"\"," +
                            "\"password\": \" "+newUserPassword+"\"," +
                            "\"nickname\": \"" + newUserNickname + "\"," +
                            "\"clearanceLevel\":" + newClearanceLevel +
                            "}")
                    .asString();
            assertEquals("Save as " +userNames[i]+":", (i == 0 || i == 1) ? HTTP_FORBIDDEN : HTTP_NO_CONTENT, saveuser.getStatus());
            if (i == 2) {
                assertTrue("user should now be uptoDate: ", getTestUserById(eid).contains(newUserPassword));
                assertTrue("user should now be uptoDate: ", getTestUserById(eid).contains(newUserEmail));
                assertTrue("user should now be uptoDate: ", getTestUserById(eid).contains(newUserNickname));
            }
        }
    }

    @After
    public void tearDownSession() {
        destroyUsers();
    }

}
