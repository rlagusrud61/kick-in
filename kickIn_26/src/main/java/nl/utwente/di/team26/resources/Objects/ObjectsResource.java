package nl.utwente.di.team26.resources.Objects;

import nl.utwente.di.team26.CONSTANTS;
import nl.utwente.di.team26.Exceptions.NotFoundException;
import nl.utwente.di.team26.dao.MapObjectsDao;
import nl.utwente.di.team26.model.MapObjects;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

@Path("/objects/{mapId}")
public class ObjectsResource {

    MapObjectsDao mapObjectsDao = new MapObjectsDao();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<MapObjects> getAllObjectsForMapById(@PathParam("mapId") int mapId) {
        try (Connection conn = DriverManager.getConnection(
                CONSTANTS.URL,
                CONSTANTS.USER,
                CONSTANTS.PASSWORD)) {
            return mapObjectsDao.searchMatching(conn, new MapObjects(0, mapId, 0, null));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String addObjectToMap(MapObjects newObjectToAdd) {
        try (Connection conn = DriverManager.getConnection(
                CONSTANTS.URL,
                CONSTANTS.USER,
                CONSTANTS.PASSWORD)) {
            mapObjectsDao.create(conn, newObjectToAdd);
            return CONSTANTS.SUCCESS;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return CONSTANTS.FAILURE;
        }
    }

    @DELETE
    @Produces(MediaType.TEXT_PLAIN)
    public String clearMap(@PathParam("mapId") int mapId) {
        try (Connection conn = DriverManager.getConnection(
                CONSTANTS.URL,
                CONSTANTS.USER,
                CONSTANTS.PASSWORD)) {
            mapObjectsDao.deleteAllForMap(conn, new MapObjects(0, mapId, 0, null));
            return CONSTANTS.SUCCESS;
        } catch (SQLException | NotFoundException throwables) {
            throwables.printStackTrace();
            return CONSTANTS.FAILURE;
        }
    }

}
