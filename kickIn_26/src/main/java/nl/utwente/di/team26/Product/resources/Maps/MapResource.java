package nl.utwente.di.team26.Product.resources.Maps;

import nl.utwente.di.team26.CONSTANTS;
import nl.utwente.di.team26.Exceptions.NotFoundException;
import nl.utwente.di.team26.Product.dao.Maps.MapsDao;
import nl.utwente.di.team26.Product.model.Map.Map;
import nl.utwente.di.team26.Security.Filters.Secured;
import nl.utwente.di.team26.Security.User.Roles;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.Connection;
import java.sql.SQLException;

@Path("/map/{mapId}")
public class MapResource {



    @GET
    @Secured(Roles.VISITOR)
    @Produces(MediaType.APPLICATION_JSON)
    public Map getMapById(@PathParam("mapId") int mapId) {
        try (Connection conn = CONSTANTS.getConnection()) {
            return (new MapsDao()).getObject(conn, mapId);
        } catch (NotFoundException | SQLException e) {
            return null;
        }
    }

    @PUT
    @Secured(Roles.EDITOR)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String updateMap(Map mapToUpdate) {
        try (Connection conn = CONSTANTS.getConnection()) {
            (new MapsDao()).save(conn, mapToUpdate);
            return CONSTANTS.SUCCESS;
        } catch (NotFoundException | SQLException e) {
            return CONSTANTS.FAILURE + " " + e.getMessage();
        }
    }

    @DELETE
    @Secured(Roles.EDITOR)
    @Produces(MediaType.TEXT_PLAIN)
    public String deleteMap(@PathParam("mapId") int mapToDelete) {
        try (Connection conn = CONSTANTS.getConnection()) {
            (new MapsDao()).delete(conn, new Map(mapToDelete));
            return CONSTANTS.SUCCESS;
        } catch (NotFoundException | SQLException e) {
            return CONSTANTS.FAILURE + " " + e.getMessage();
        }
    }

}
