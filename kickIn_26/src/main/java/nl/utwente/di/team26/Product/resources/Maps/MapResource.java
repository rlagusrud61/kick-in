package nl.utwente.di.team26.Product.resources.Maps;

import nl.utwente.di.team26.Exception.Exceptions.NotFoundException;
import nl.utwente.di.team26.Product.dao.Maps.MapsDao;
import nl.utwente.di.team26.Product.model.Map.Map;
import nl.utwente.di.team26.Security.Filters.Secured;
import nl.utwente.di.team26.Security.User.Roles;
import nl.utwente.di.team26.Utils;
import org.checkerframework.common.reflection.qual.GetClass;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.sql.SQLException;

@Path("/map")
public class MapResource {

    @Context
    SecurityContext securityContext;

    MapsDao mapsDao = new MapsDao();

    @GET
    @Path("{mapId}")
    @Secured(Roles.VISITOR)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMapById(@PathParam("mapId") long mapId) throws NotFoundException, SQLException {
        String mapData = mapsDao.getMap(mapId);
        return Utils.returnOkResponse(mapData);
    }

    @GET
    @Path("image/{mapId}")
    @Secured(Roles.VISITOR)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMapImage(@PathParam("mapId") long mapId) throws NotFoundException, SQLException {
        String mapImage = mapsDao.getMapImage(mapId);
        return Utils.returnOkResponse(mapImage);
    }

    @PUT
    @Path("{mapId}")
    @Secured(Roles.EDITOR)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateMap(Map mapToUpdate) throws NotFoundException, SQLException {
        long userId = Utils.getUserFromContext(securityContext);
        mapToUpdate.setLastEditedBy(userId);
        mapsDao.save(mapToUpdate);
        return Utils.returnNoContent();
    }

    @PUT
    @Path("image/{mapId}")
    @Secured(Roles.EDITOR)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response setNewMapImage(Map mapToUpdate) throws NotFoundException, SQLException {
        mapsDao.setNewImage(mapToUpdate);
        return Utils.returnNoContent();
    }

    @DELETE
    @Path("{mapId}")
    @Secured(Roles.EDITOR)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteMap(@PathParam("mapId") long mapToDelete) throws NotFoundException, SQLException {
        mapsDao.delete(new Map(mapToDelete));
        return Utils.returnNoContent();
    }

}
