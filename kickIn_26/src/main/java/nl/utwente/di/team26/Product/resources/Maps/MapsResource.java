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
    public Response getAllMaps() {
        try (Connection conn = CONSTANTS.getConnection()) {
            String allMaps = mapsDao.getAllMaps(conn);
            return Utils.returnOkResponse(allMaps);
        } catch (NotFoundException e) {
            return Utils.returnNotFoundError(e.getMessage());
        } catch (SQLException e) {
            return Utils.returnInternalServerError(e.getMessage());
        }
    }

    @POST
    @Secured(Roles.EDITOR)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addNewMap(Map mapToAdd) {

        long userId = Utils.getUserFromContext(securityContext);

        try (Connection conn = CONSTANTS.getConnection()) {

            mapToAdd.setCreatedBy(userId);
            mapToAdd.setLastEditedBy(userId);

            long mapId = mapsDao.create(conn, mapToAdd);
            return Utils.returnCreated(mapId);
        } catch (SQLException e) {
            return Utils.returnInternalServerError(e.getMessage());
        }
    }

    @DELETE
    @Secured(Roles.ADMIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteAllMaps() {
        try (Connection conn = CONSTANTS.getConnection()) {
            mapsDao.deleteAll(conn);
            return Utils.returnNoContent();
        } catch (SQLException e) {
            return Utils.returnInternalServerError(e.getMessage());
        }
    }

}