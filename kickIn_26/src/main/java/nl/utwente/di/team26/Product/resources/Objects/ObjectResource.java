package nl.utwente.di.team26.Product.resources.Objects;

import nl.utwente.di.team26.CONSTANTS;
import nl.utwente.di.team26.Exceptions.NotFoundException;
import nl.utwente.di.team26.Product.dao.Maps.MapObjectsDao;
import nl.utwente.di.team26.Product.model.Map.MapObject;
import nl.utwente.di.team26.Security.Authentication.Secured;
import nl.utwente.di.team26.Security.Authentication.User.User;
import nl.utwente.di.team26.Security.Authorization.Role;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.Connection;
import java.sql.SQLException;

@Path("/object/{objectId}")
public class ObjectResource {



    MapObjectsDao mapObjectsDao = new MapObjectsDao();

    @GET
    @Secured(Role.VISITOR)
    @Produces(MediaType.APPLICATION_JSON)
    public MapObject getObjectForMap(@PathParam("objectId") int objectId) {
        try (Connection conn = CONSTANTS.getConnection()) {
            return mapObjectsDao.getObject(conn, objectId);
        } catch (SQLException | NotFoundException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    @PUT
    @Secured(Role.EDITOR)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String updateObject(MapObject objectToUpdate) {
        try (Connection conn = CONSTANTS.getConnection()) {
            mapObjectsDao.save(conn, objectToUpdate);
            return CONSTANTS.SUCCESS;
        } catch (NotFoundException | SQLException e) {
            return CONSTANTS.FAILURE + " " + e.getMessage();
        }
    }

    @DELETE
    @Secured(Role.EDITOR)
    @Produces(MediaType.TEXT_PLAIN)
    public String deleteObject(@PathParam("objectId") int objectToDelete) {
        try (Connection conn = CONSTANTS.getConnection()) {
            mapObjectsDao.delete(conn, new MapObject(objectToDelete));
            return CONSTANTS.SUCCESS;
        } catch (NotFoundException | SQLException e) {
            return CONSTANTS.FAILURE + " " + e.getMessage();
        }
    }
}
