package nl.utwente.di.team26.Product.resources.Objects;

import nl.utwente.di.team26.CONSTANTS;
import nl.utwente.di.team26.Exception.Exceptions.NotFoundException;
import nl.utwente.di.team26.Product.dao.Maps.MapObjectsDao;
import nl.utwente.di.team26.Product.model.Map.MapObject;
import nl.utwente.di.team26.Security.Filters.Secured;
import nl.utwente.di.team26.Security.User.Roles;
import nl.utwente.di.team26.Utils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.SQLException;

@Path("/object/{objectId}")
public class ObjectResource {

    MapObjectsDao mapObjectsDao = new MapObjectsDao();

    @PUT
    @Secured(Roles.EDITOR)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateObject(MapObject objectToUpdate) throws NotFoundException, SQLException {

            mapObjectsDao.save(objectToUpdate);
            return Utils.returnNoContent();
    }

    @DELETE
    @Secured(Roles.EDITOR)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteObject(@PathParam("objectId") long objectToDelete) throws NotFoundException, SQLException {

            mapObjectsDao.delete(new MapObject(objectToDelete));
            return Utils.returnNoContent();
    }
}
