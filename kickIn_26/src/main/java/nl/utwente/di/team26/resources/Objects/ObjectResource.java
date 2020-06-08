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

@Path("/object/{objectId}")
public class ObjectResource {

    MapObjectsDao mapObjectsDao = new MapObjectsDao();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public MapObjects getObjectForMap(@PathParam("objectId") int objectId) {
        try (Connection conn = CONSTANTS.getConnection()) {
            return mapObjectsDao.getObject(conn, objectId);
        } catch (SQLException | NotFoundException | DriverNotInstalledException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String updateObject(MapObjects objectToUpdate) {
        try (Connection conn = CONSTANTS.getConnection()) {
            mapObjectsDao.save(conn, objectToUpdate);
            return CONSTANTS.SUCCESS;
        } catch (NotFoundException | SQLException | DriverNotInstalledException e) {
            return CONSTANTS.FAILURE + " " + e.getMessage();
        }
    }

    @DELETE
    @Produces(MediaType.TEXT_PLAIN)
    public String deleteObject(@PathParam("objectId") int objectToDelete) {
        try (Connection conn = CONSTANTS.getConnection()) {
            mapObjectsDao.delete(conn, new MapObjects(objectToDelete));
            return CONSTANTS.SUCCESS;
        } catch (NotFoundException | SQLException | DriverNotInstalledException e) {
            return CONSTANTS.FAILURE + " " + e.getMessage();
        }
    }
}
