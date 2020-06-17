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
            return Response.ok(allMaps).build();
        } catch (NotFoundException throwables) {
            return Response.status(Response.Status.NOT_FOUND).entity(throwables.getMessage()).build();
        } catch (SQLException throwables) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(throwables.getMessage()).build();
        }
    }

    @POST
    @Secured(Roles.EDITOR)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response addNewMap(Map mapToAdd) {

        long userId = Utils.getUserFromContext(securityContext);

        try (Connection conn = CONSTANTS.getConnection()) {

            mapToAdd.setCreatedBy(userId);
            mapToAdd.setLastEditedBy(userId);

            long mapId = mapsDao.create(conn, mapToAdd);
            return Response.status(Response.Status.CREATED).entity(mapId).build();
        } catch (SQLException throwables) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(throwables.getMessage()).build();
        }
    }

    @DELETE
    @Secured(Roles.ADMIN)
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteAllMaps() {
        try (Connection conn = CONSTANTS.getConnection()) {
            mapsDao.deleteAll(conn);
            return Response.noContent().build();
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

}