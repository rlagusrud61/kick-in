package JUnitTesting;

import kong.unirest.Cookie;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import nl.utwente.di.team26.Constants;
import nl.utwente.di.team26.Exception.Exceptions.NotFoundException;
import nl.utwente.di.team26.Product.dao.Authentication.UserDao;
import nl.utwente.di.team26.Product.dao.Events.EventMapDao;
import nl.utwente.di.team26.Product.dao.Events.EventsDao;
import nl.utwente.di.team26.Product.dao.Maps.MapObjectsDao;
import nl.utwente.di.team26.Product.dao.Maps.MapsDao;
import nl.utwente.di.team26.Product.dao.Resources.ResourceDao;
import nl.utwente.di.team26.Product.model.Authentication.Credentials;
import nl.utwente.di.team26.Product.model.Authentication.User;
import nl.utwente.di.team26.Product.model.Event.Event;
import nl.utwente.di.team26.Product.model.Event.EventMap;
import nl.utwente.di.team26.Product.model.Map.LatLang;
import nl.utwente.di.team26.Product.model.Map.Map;
import nl.utwente.di.team26.Product.model.Map.MapObject;
import nl.utwente.di.team26.Product.model.TypeOfResource.Drawing;
import nl.utwente.di.team26.Product.model.TypeOfResource.Material;
import nl.utwente.di.team26.Product.model.TypeOfResource.TypeOfResource;
import nl.utwente.di.team26.Security.User.Roles;
import nl.utwente.di.team26.Utils;

import javax.ws.rs.core.UriBuilder;
import java.sql.Connection;
import java.sql.SQLException;

public class Tests {

    EventsDao eventsDao = new EventsDao();
    MapsDao mapsDao = new MapsDao();
    UserDao usersDao = new UserDao();
    EventMapDao eventMapDao = new EventMapDao();
    MapObjectsDao mapObjectsDao = new MapObjectsDao();
    ResourceDao resourceDao = new ResourceDao();

    Roles[] roles = {Roles.ADMIN, Roles.EDITOR, Roles.VISITOR};

    protected final Event testEventInstance = new Event("TestEvent", "TestEvent", "On Campus", "2020-02-01");

    protected final String[] testMaps = {"{\"name\":\"TestMap1\",\"description\":\"TestMap1\"}","{\"name\":\"TestMap2\",\"description\":\"TestMap2\"}"};
    protected Map[] testMapsInstance = {new Map("TestMap1", "TestMap1"), new Map("TestMap2", "TestMap2")};

    protected final String testUser = "{\"email\":\"testuser@email.com\",\"password\":\"password\",\"nickname\":\"TestUser\",\"clearanceLevel\":2}";
    protected final User testUserInstance = new User("testuser@email.com", "password", "TestUser", 2);

    String defaultPassword = "password";
    String[] userNames = {"visitorJUINT", "editorJUINT", "adminJUNIT"};
    long[] userIds;

    protected Cookie getLoginCookie(int level) {
        String email = userNames[level] + "@email.com";
        HttpResponse<String> login = Unirest.post(getURIString("/authentication"))
                .header("Content-Type", "application/json")
                .body(new Credentials(email, defaultPassword))
                .asString();
        return login.getCookies().getNamed(Constants.COOKIENAME);
    }

    protected String getURIString(String path) {
        return UriBuilder.fromUri("http://env-di-team26.paas.hosted-by-previder.com/rest").path(path).build().toString();
    }

    protected void addThreeUsers() {
        long[] userArrays = new long[3];

        UserDao userDao = new UserDao();
        for (int i = 0; i < 3; i++) {
            try {
                userArrays[i] = userDao.create(new User(String.format("%s@email.com", userNames[i]), Utils.hashPassword(defaultPassword), userNames[i], i));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        userIds = userArrays;
    }
    protected void destroyUsers() {
        UserDao userDao = new UserDao();
        try(Connection conn = Constants.getConnection()) {
            for (int i = 0; i < 3; i++) {
                userDao.delete(new User(userIds[i]));
            }
        } catch (SQLException | NotFoundException e) {
            e.printStackTrace();
        }
    }

    protected long addTestEvent() throws SQLException {
        testEventInstance.setLastEditedBy(userIds[2]);
        testEventInstance.setCreatedBy(userIds[2]);
        return eventsDao.create(testEventInstance);
    }
    protected void deleteTestEvent(long eid) throws SQLException, NotFoundException {
        eventsDao.delete(new Event(eid));
    }
    protected String getTestEventById(long eid) throws SQLException, NotFoundException {
        return eventsDao.get(eid);
    }

    protected long addTestUser() throws SQLException {
        return usersDao.create(testUserInstance);
    }
    protected void deleteTestUser(long uid) throws SQLException, NotFoundException {
        usersDao.delete(new User(uid));
    }
    protected String getTestUserById(long uid) throws SQLException, NotFoundException {
        return usersDao.get(uid);
    }

    protected long[] addTestMaps() throws SQLException {
        long[] mapIds = new long[2];
        for(int i = 0; i < 2; i++) {
            testMapsInstance[i].setLastEditedBy(userIds[2]);
            testMapsInstance[i].setCreatedBy(userIds[2]);
            mapIds[i] = mapsDao.create(testMapsInstance[i]);
        }
        return mapIds;
    }
    protected void deleteTestMap(long[] mids) throws SQLException, NotFoundException {
        for (long mid : mids) {
            mapsDao.delete(new Map(mid));
        }
    }
    protected String getTestMapById(long mid) throws SQLException, NotFoundException {
        return mapsDao.getMap(mid);
    }

    protected void createRelations(long eid, long[] mids) throws SQLException {
        for (long mid : mids) {
            eventMapDao.create(new EventMap(eid, mid));
        }
    }
    protected void deleteRelations(long eid, long[] mids) throws NotFoundException, SQLException {
        for (long mid : mids) {
            eventMapDao.delete(new EventMap(eid, mid));
        }
    }

    protected void clearMap(long mid) throws NotFoundException, SQLException {
        mapObjectsDao.deleteAllForMap(mid);
    }
    protected long addObjectsToMap(long mid) throws SQLException {
        return mapObjectsDao.create(new MapObject(mid, 23, new LatLang[]{new LatLang(1,2)}));
    }

    protected long[] addResources() throws SQLException {
        long[] rids = new long[2];
        rids[0] = resourceDao.create(new Material("TestMaterial", "TestMaterial", "someBase64image"));
        rids[1] = resourceDao.create(new Drawing("TestDrawing", "TestDrawing", "someBase64image"));
        return rids;
    }
    protected void deleteResources(long[] rids) throws NotFoundException, SQLException {
        for (long rid : rids) {
            resourceDao.delete(new TypeOfResource(rid));
        }
    }
}
