package nl.utwente.di.team26.Product.resources.Maps;

import nl.utwente.di.team26.CONSTANTS;
import nl.utwente.di.team26.Exceptions.NotFoundException;
import nl.utwente.di.team26.Product.dao.Maps.MapsDao;
import nl.utwente.di.team26.Product.model.Map.Map;
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

@Path("/map/{mapId}")
public class MapResource {

    @Context
    SecurityContext securityContext;

    MapsDao mapsDao = new MapsDao();

    @GET
    @Secured(Roles.VISITOR)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMapById(@PathParam("mapId") int mapId) {
        try (Connection conn = CONSTANTS.getConnection()) {
            String mapData = mapsDao.getMap(conn, mapId);
            return Response.ok(mapData).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @PUT
    @Secured(Roles.EDITOR)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response updateMap(Map mapToUpdate) {
        long userId = Utils.getUserFromContext(securityContext);
        mapToUpdate.setLastEditedBy(userId);
        try (Connection conn = CONSTANTS.getConnection()) {
            mapsDao.save(conn, mapToUpdate);
            return Response.noContent().build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Secured(Roles.EDITOR)
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteMap(@PathParam("mapId") int mapToDelete) {
        try (Connection conn = CONSTANTS.getConnection()) {
            mapsDao.delete(conn, new Map(mapToDelete));
            return Response.noContent().build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

}
