package nl.utwente.di.team26.Product.resources.Objects;

import nl.utwente.di.team26.CONSTANTS;
import nl.utwente.di.team26.Exceptions.DriverNotInstalledException;
import nl.utwente.di.team26.Exceptions.NotFoundException;
import nl.utwente.di.team26.Product.dao.Maps.MapObjectsDao;
import nl.utwente.di.team26.Product.model.Map.MapObject;
import nl.utwente.di.team26.Security.Authentication.Secured;
import nl.utwente.di.team26.Security.Authentication.User.AuthenticatedUser;
import nl.utwente.di.team26.Security.Authentication.User.User;
import nl.utwente.di.team26.Security.Authorization.Role;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Path("/objects")
public class ObjectsResource {

    @Inject
    @AuthenticatedUser
    User authenticatedUser;

    MapObjectsDao mapObjectsDao = new MapObjectsDao();

    @GET
    @Secured(Role.VISITOR)
    @Path("{mapId}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<MapObject> getAllObjectsForMap(@PathParam("mapId") int mapId) {
        try (Connection conn = CONSTANTS.getConnection()) {
            return mapObjectsDao.searchMatching(conn, new MapObject(0, mapId, 0, null));
        } catch (SQLException | DriverNotInstalledException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    @POST
    @Secured(Role.EDITOR)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String addObjectToMap(MapObject newObjectToAdd) {
        try (Connection conn = CONSTANTS.getConnection()) {
            mapObjectsDao.create(conn, newObjectToAdd);
            return CONSTANTS.SUCCESS;
        } catch (SQLException | DriverNotInstalledException throwables) {
            throwables.printStackTrace();
            return CONSTANTS.FAILURE + " " + throwables.getMessage();
        }
    }

    @DELETE
    @Secured(Role.EDITOR)
    @Path("{mapId}")
    @Produces(MediaType.TEXT_PLAIN)
    public String clearMap(@PathParam("mapId") int mapId) {
        try (Connection conn = CONSTANTS.getConnection()) {
            mapObjectsDao.deleteAllForMap(conn, new MapObject(0, mapId, 0, null));
            return CONSTANTS.SUCCESS;
        } catch (SQLException | NotFoundException | DriverNotInstalledException throwables) {
            throwables.printStackTrace();
            return CONSTANTS.FAILURE + " " + throwables.getMessage();
        }
    }

    @DELETE
    @Secured(Role.ADMIN)
    @Produces(MediaType.TEXT_PLAIN)
    public String clearAllMaps() {
        try (Connection conn = CONSTANTS.getConnection()) {
            mapObjectsDao.deleteAll(conn);
            return CONSTANTS.SUCCESS;
        } catch (SQLException | DriverNotInstalledException e) {
            e.printStackTrace();
            return CONSTANTS.FAILURE + ": " + e.getMessage();
        }
    }

}
