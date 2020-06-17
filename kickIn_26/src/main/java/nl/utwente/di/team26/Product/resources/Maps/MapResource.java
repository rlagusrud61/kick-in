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
    public Response getMapById(@PathParam("mapId") long mapId) {
        try (Connection conn = CONSTANTS.getConnection()) {
            String mapData = mapsDao.getMap(conn, mapId);
            return Utils.returnOkResponse(mapData);
        } catch (NotFoundException e) {
            return Utils.returnNotFoundError(e.getMessage());
        } catch (SQLException e) {
            return Utils.returnInternalServerError(e.getMessage());
        }
    }

    @PUT
    @Secured(Roles.EDITOR)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateMap(Map mapToUpdate) {
        long userId = Utils.getUserFromContext(securityContext);
        mapToUpdate.setLastEditedBy(userId);
        try (Connection conn = CONSTANTS.getConnection()) {
            mapsDao.save(conn, mapToUpdate);
            return Utils.returnNoContent();
        } catch (NotFoundException e) {
            return Utils.returnNotFoundError(e.getMessage());
        } catch (SQLException e) {
            return Utils.returnInternalServerError(e.getMessage());
        }
    }

    @DELETE
    @Secured(Roles.EDITOR)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteMap(@PathParam("mapId") int mapToDelete) {
        try (Connection conn = CONSTANTS.getConnection()) {
            mapsDao.delete(conn, new Map(mapToDelete));
            return Utils.returnNoContent();
        } catch (NotFoundException e) {
            return Utils.returnNotFoundError(e.getMessage());
        } catch (SQLException e) {
            return Utils.returnInternalServerError(e.getMessage());
        }
    }

}
