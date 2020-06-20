package nl.utwente.di.team26.Product.resources.Maps;

import nl.utwente.di.team26.Exception.Exceptions.NotFoundException;
import nl.utwente.di.team26.Product.dao.Maps.MapsDao;
import nl.utwente.di.team26.Product.model.Map.Map;
import nl.utwente.di.team26.Security.Filters.Secured;
import nl.utwente.di.team26.Security.User.Roles;
import nl.utwente.di.team26.Utils;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.sql.SQLException;

@Path("/maps")
public class MapsResource {

    MapsDao mapsDao = new MapsDao();

    @Context
    HttpHeaders httpHeaders;

    @Context
    SecurityContext securityContext;

    @GET
    @Secured(Roles.VISITOR)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllMaps() throws NotFoundException, SQLException {
        String allMaps = mapsDao.getAllMaps();
        return Utils.returnOkResponse(allMaps);
    }

    @POST
    @Secured(Roles.EDITOR)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addNewMap(Map mapToAdd) throws SQLException {

        long userId = Utils.getUserFromContext(securityContext);

        mapToAdd.setCreatedBy(userId);
        mapToAdd.setLastEditedBy(userId);

        long mapId = mapsDao.create(mapToAdd);
        return Utils.returnCreated(mapId);
    }

    @DELETE
    @Secured(Roles.ADMIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteAllMaps() throws SQLException, NotFoundException {
        mapsDao.deleteAll();
        return Utils.returnNoContent();
    }

}