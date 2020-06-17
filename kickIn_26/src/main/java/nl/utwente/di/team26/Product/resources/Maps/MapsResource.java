package nl.utwente.di.team26.Product.resources.Maps;

import nl.utwente.di.team26.CONSTANTS;
import nl.utwente.di.team26.Exceptions.NotFoundException;
import nl.utwente.di.team26.Product.dao.Maps.MapsDao;
import nl.utwente.di.team26.Product.model.Map.Map;
import nl.utwente.di.team26.Security.Filters.Secured;
import nl.utwente.di.team26.Security.User.Roles;
import nl.utwente.di.team26.Utils;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.sql.Connection;
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
        try (Connection conn = CONSTANTS.getConnection()) {
            String allMaps = mapsDao.getAllMaps(conn);
            return Utils.returnOkResponse(allMaps);
        }
    }

    @POST
    @Secured(Roles.EDITOR)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addNewMap(Map mapToAdd) throws SQLException {

        long userId = Utils.getUserFromContext(securityContext);

        try (Connection conn = CONSTANTS.getConnection()) {

            mapToAdd.setCreatedBy(userId);
            mapToAdd.setLastEditedBy(userId);

            long mapId = mapsDao.create(conn, mapToAdd);
            return Utils.returnCreated(mapId);
        }
    }

    @DELETE
    @Secured(Roles.ADMIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteAllMaps() throws SQLException {
        try (Connection conn = CONSTANTS.getConnection()) {
            mapsDao.deleteAll(conn);
            return Utils.returnNoContent();
        }
    }

}