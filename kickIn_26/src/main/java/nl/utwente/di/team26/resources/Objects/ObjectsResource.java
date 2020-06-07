package nl.utwente.di.team26.resources.Objects;

import nl.utwente.di.team26.CONSTANTS;
import nl.utwente.di.team26.Exceptions.DriverNotInstalledException;
import nl.utwente.di.team26.Exceptions.NotFoundException;
import nl.utwente.di.team26.dao.MapObjectsDao;
import nl.utwente.di.team26.model.MapObjects;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Path("/objects")
public class ObjectsResource {

    MapObjectsDao mapObjectsDao = new MapObjectsDao();

    @GET
    @Path("{mapId}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<MapObjects> getAllObjectsForMapById(@PathParam("mapId") int mapId) {
        try (Connection conn = CONSTANTS.getConnection()) {
            return mapObjectsDao.searchMatching(conn, new MapObjects(0, mapId, 0, null));
        } catch (SQLException | DriverNotInstalledException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String addObjectToMap(MapObjects newObjectToAdd) {
        try (Connection conn = CONSTANTS.getConnection()) {
            mapObjectsDao.create(conn, newObjectToAdd);
            return CONSTANTS.SUCCESS;
        } catch (SQLException | DriverNotInstalledException throwables) {
            throwables.printStackTrace();
            return CONSTANTS.FAILURE + " " + throwables.getMessage();
        }
    }

    @DELETE
    @Path("{mapId}")
    @Produces(MediaType.TEXT_PLAIN)
    public String clearMap(@PathParam("mapId") int mapId) {
        try (Connection conn = CONSTANTS.getConnection()) {
            mapObjectsDao.deleteAllForMap(conn, new MapObjects(0, mapId, 0, null));
            return CONSTANTS.SUCCESS;
        } catch (SQLException | NotFoundException | DriverNotInstalledException throwables) {
            throwables.printStackTrace();
            return CONSTANTS.FAILURE + " " + throwables.getMessage();
        }
    }

    @DELETE
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
