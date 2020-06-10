package nl.utwente.di.team26.Product.resources.Objects;

import nl.utwente.di.team26.CONSTANTS;
import nl.utwente.di.team26.Exceptions.NotFoundException;
import nl.utwente.di.team26.Product.dao.Maps.MapObjectsDao;
import nl.utwente.di.team26.Product.model.Map.MapObject;
import nl.utwente.di.team26.Security.Filters.Secured;
import nl.utwente.di.team26.Security.User.Roles;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Path("/objects")
public class ObjectsResource {



    MapObjectsDao mapObjectsDao = new MapObjectsDao();

    @GET
    @Secured(Roles.VISITOR)
    @Path("{mapId}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<MapObject> getAllObjectsForMap(@PathParam("mapId") int mapId) {
        try (Connection conn = CONSTANTS.getConnection()) {
            return mapObjectsDao.searchMatching(conn, new MapObject(0, mapId, 0, null));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    @POST
    @Secured(Roles.EDITOR)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String addObjectToMap(MapObject newObjectToAdd) {
        try (Connection conn = CONSTANTS.getConnection()) {
            mapObjectsDao.create(conn, newObjectToAdd);
            return CONSTANTS.SUCCESS;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return CONSTANTS.FAILURE + " " + throwables.getMessage();
        }
    }

    @DELETE
    @Secured(Roles.EDITOR)
    @Path("{mapId}")
    @Produces(MediaType.TEXT_PLAIN)
    public String clearMap(@PathParam("mapId") int mapId) {
        try (Connection conn = CONSTANTS.getConnection()) {
            mapObjectsDao.deleteAllForMap(conn, new MapObject(0, mapId, 0, null));
            return CONSTANTS.SUCCESS;
        } catch (SQLException | NotFoundException throwables) {
            throwables.printStackTrace();
            return CONSTANTS.FAILURE + " " + throwables.getMessage();
        }
    }

    @DELETE
    @Secured(Roles.ADMIN)
    @Produces(MediaType.TEXT_PLAIN)
    public String clearAllMaps() {
        try (Connection conn = CONSTANTS.getConnection()) {
            mapObjectsDao.deleteAll(conn);
            return CONSTANTS.SUCCESS;
        } catch (SQLException e) {
            e.printStackTrace();
            return CONSTANTS.FAILURE + ": " + e.getMessage();
        }
    }

}
