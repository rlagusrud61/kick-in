package nl.utwente.di.team26.resources.Maps;

import nl.utwente.di.team26.CONSTANTS;
import nl.utwente.di.team26.Exceptions.DriverNotInstalledException;
import nl.utwente.di.team26.Exceptions.NotFoundException;
import nl.utwente.di.team26.dao.MapsDao;
import nl.utwente.di.team26.model.Maps;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.Connection;
import java.sql.SQLException;

@Path("/map/{mapId}")
public class MapResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Maps getMapById(@PathParam("mapId") int mapId) {
        try (Connection conn = CONSTANTS.getConnection()) {
            return (new MapsDao()).getObject(conn, mapId);
        } catch (NotFoundException | SQLException | DriverNotInstalledException e) {
            return null;
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String updateMap(Maps mapToUpdate) {
        try (Connection conn = CONSTANTS.getConnection()) {
            (new MapsDao()).save(conn, mapToUpdate);
            return CONSTANTS.SUCCESS;
        } catch (NotFoundException | SQLException | DriverNotInstalledException e) {
            return CONSTANTS.FAILURE + " " + e.getMessage();
        }
    }

    @DELETE
    @Produces(MediaType.TEXT_PLAIN)
    public String deleteEvent(@PathParam("mapId") int mapToDelete) {
        try (Connection conn = CONSTANTS.getConnection()) {
            (new MapsDao()).delete(conn, new Maps(mapToDelete));
            return CONSTANTS.SUCCESS;
        } catch (NotFoundException | SQLException | DriverNotInstalledException e) {
            return CONSTANTS.FAILURE + " " + e.getMessage();
        }
    }

}
