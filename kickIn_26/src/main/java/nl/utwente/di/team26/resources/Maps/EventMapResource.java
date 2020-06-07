package nl.utwente.di.team26.resources.Maps;

import nl.utwente.di.team26.CONSTANTS;
import nl.utwente.di.team26.Exceptions.DriverNotInstalledException;
import nl.utwente.di.team26.Exceptions.NotFoundException;
import nl.utwente.di.team26.dao.Events.EventMapDao;
import nl.utwente.di.team26.model.Event.EventMap;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Path("/eventMap")
public class EventMapResource {

    EventMapDao eventMapDao = new EventMapDao();

    @GET
    @Path("event/{eventId}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<EventMap> getAllMapsForEvent(@PathParam("eventId") int eventId) {
        try (Connection conn = CONSTANTS.getConnection()) {
            return eventMapDao.searchMatching(conn, new EventMap(eventId, 0));
        } catch (SQLException | DriverNotInstalledException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    @GET
    @Path("map/{mapId}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<EventMap> getAllEventsForMap(@PathParam("mapId") int mapId) {
        try (Connection conn = CONSTANTS.getConnection()) {
            return eventMapDao.searchMatching(conn, new EventMap(0, mapId));
        } catch (SQLException | DriverNotInstalledException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String addEventMap(EventMap eventMapToCreate) {
        try (Connection conn = CONSTANTS.getConnection()) {
            eventMapDao.create(conn, eventMapToCreate);
            return CONSTANTS.SUCCESS;
        } catch (SQLException | DriverNotInstalledException throwables) {
            return CONSTANTS.FAILURE + ": " + throwables.getMessage();
        }
    }

    @DELETE
    @Path("event/{eventId}")
    @Produces(MediaType.TEXT_PLAIN)
    public String clearEvent(@PathParam("eventId") int eventId) {
        try (Connection conn = CONSTANTS.getConnection()) {
            eventMapDao.deleteAllForEvent(conn, new EventMap(eventId, 0));
            return CONSTANTS.SUCCESS;
        } catch (SQLException | NotFoundException | DriverNotInstalledException throwables) {
            throwables.printStackTrace();
            return CONSTANTS.FAILURE + " " + throwables.getMessage();
        }
    }

    @DELETE
    @Path("{eventId}/{mapId}")
    @Produces(MediaType.TEXT_PLAIN)
    public String deleteEventMap(@PathParam("eventId") int eventId, @PathParam("mapId") int mapId) {
        try (Connection conn = CONSTANTS.getConnection()) {
            eventMapDao.delete(conn, new EventMap(eventId, mapId));
            return CONSTANTS.SUCCESS;
        } catch (SQLException | NotFoundException | DriverNotInstalledException throwables) {
            throwables.printStackTrace();
            return CONSTANTS.FAILURE + " " + throwables.getMessage();
        }
    }

    @DELETE
    @Produces(MediaType.TEXT_PLAIN)
    public String deleteAll() {
        try (Connection conn = CONSTANTS.getConnection()) {
            eventMapDao.deleteAll(conn);
            return CONSTANTS.SUCCESS;
        } catch (SQLException | DriverNotInstalledException throwables) {
            throwables.printStackTrace();
            return CONSTANTS.FAILURE + " " + throwables.getMessage();
        }
    }

}
