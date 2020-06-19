package nl.utwente.di.team26.Product.resources.Objects;

import nl.utwente.di.team26.CONSTANTS;
import nl.utwente.di.team26.Exception.Exceptions.NotFoundException;
import nl.utwente.di.team26.Product.dao.Maps.MapObjectsDao;
import nl.utwente.di.team26.Product.dao.Maps.MapsDao;
import nl.utwente.di.team26.Product.model.Map.MapObject;
import nl.utwente.di.team26.Security.Filters.Secured;
import nl.utwente.di.team26.Security.User.Roles;
import nl.utwente.di.team26.Utils;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.sql.Connection;
import java.sql.SQLException;

@Path("/objects")
public class ObjectsResource {

    @Context
    SecurityContext securityContext;

    MapObjectsDao mapObjectsDao = new MapObjectsDao();
    MapsDao mapsDao = new MapsDao();

    @GET
    @Secured(Roles.VISITOR)
    @Path("{mapId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllObjectsForMap(@PathParam("mapId") long mapId) throws NotFoundException, SQLException {
        String allObjectsOnMap = mapObjectsDao.getAllObjectsOnMap(mapId);
        return Utils.returnOkResponse(allObjectsOnMap);
    }

    @GET
    @Secured(Roles.VISITOR)
    @Path("{mapId}/report")
    @Produces(MediaType.APPLICATION_JSON)
    public Response generateReportForMap(@PathParam("mapId") long mapId) throws NotFoundException, SQLException {
        String allObjectsOnMap = mapObjectsDao.generateReport(mapId);
        return Utils.returnOkResponse(allObjectsOnMap);
    }

    @POST
    @Secured(Roles.EDITOR)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addObjectToMap(MapObject newObjectToAdd) throws NotFoundException, SQLException {
        long userId = Utils.getUserFromContext(securityContext);
        long objectId = mapObjectsDao.create(newObjectToAdd);
        mapsDao.saveLastEditedBy(newObjectToAdd.getMapId(), userId);
        return Utils.returnCreated(objectId);
    }

    @DELETE
    @Secured(Roles.EDITOR)
    @Path("{mapId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response clearMap(@PathParam("mapId") long mapId) throws NotFoundException, SQLException {
        long userId = Utils.getUserFromContext(securityContext);
        mapsDao.saveLastEditedBy(mapId, userId);
        mapObjectsDao.deleteAllForMap(new MapObject(0, mapId, 0, null));
        return Utils.returnNoContent();
    }

    @DELETE
    @Secured(Roles.ADMIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response clearAllMaps() throws SQLException, NotFoundException {
        mapObjectsDao.deleteAll();
        return Utils.returnNoContent();
    }

}
