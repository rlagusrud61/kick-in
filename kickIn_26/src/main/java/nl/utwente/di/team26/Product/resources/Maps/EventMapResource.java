package nl.utwente.di.team26.Product.resources.Maps;

import nl.utwente.di.team26.CONSTANTS;
import nl.utwente.di.team26.Exceptions.NotFoundException;
import nl.utwente.di.team26.Product.dao.Events.EventMapDao;
import nl.utwente.di.team26.Product.dao.Events.EventsDao;
import nl.utwente.di.team26.Product.dao.Maps.MapsDao;
import nl.utwente.di.team26.Product.model.Event.Event;
import nl.utwente.di.team26.Product.model.Event.EventMap;
import nl.utwente.di.team26.Product.model.Map.Map;
import nl.utwente.di.team26.Security.Authentication.Secured;
import nl.utwente.di.team26.Security.Authentication.User.User;
import nl.utwente.di.team26.Security.Authorization.Role;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Path("/eventMap")
public class EventMapResource {



    EventMapDao eventMapDao = new EventMapDao();
    MapsDao mapsDao = new MapsDao();
    EventsDao eventsDao = new EventsDao();

    @GET
    @Secured({Role.VISITOR})
    @Path("event/{eventId}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Map> getAllMapsForEvent(@PathParam("eventId") int eventId) {
        try (Connection conn = CONSTANTS.getConnection()) {
            return mapsDao.getAllMapsFor(conn, eventId);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    @GET
    @Secured({Role.VISITOR})
    @Path("map/{mapId}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Event> getAllEventsForMap(@PathParam("mapId") int mapId) {
        try (Connection conn = CONSTANTS.getConnection()) {
            return eventsDao.allEventsFor(conn, mapId);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    @POST
    @Secured(Role.EDITOR)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String addEventMap(EventMap eventMapToCreate) {
        try (Connection conn = CONSTANTS.getConnection()) {
            eventMapDao.create(conn, eventMapToCreate);
            return CONSTANTS.SUCCESS;
        } catch (SQLException throwables) {
            return CONSTANTS.FAILURE + ": " + throwables.getMessage();
        }
    }

    @DELETE
    @Secured(Role.EDITOR)
    @Path("event/{eventId}")
    @Produces(MediaType.TEXT_PLAIN)
    public String clearEvent(@PathParam("eventId") int eventId) {
        try (Connection conn = CONSTANTS.getConnection()) {
            eventMapDao.deleteAllForEvent(conn, new EventMap(eventId, 0));
            return CONSTANTS.SUCCESS;
        } catch (SQLException | NotFoundException throwables) {
            throwables.printStackTrace();
            return CONSTANTS.FAILURE + " " + throwables.getMessage();
        }
    }

    @DELETE
    @Secured(Role.EDITOR)
    @Path("{eventId}/{mapId}")
    @Produces(MediaType.TEXT_PLAIN)
    public String deleteEventMap(@PathParam("eventId") int eventId, @PathParam("mapId") int mapId) {
        try (Connection conn = CONSTANTS.getConnection()) {
            eventMapDao.delete(conn, new EventMap(eventId, mapId));
            return CONSTANTS.SUCCESS;
        } catch (SQLException | NotFoundException throwables) {
            throwables.printStackTrace();
            return CONSTANTS.FAILURE + " " + throwables.getMessage();
        }
    }

    @DELETE
    @Secured(Role.ADMIN)
    @Produces(MediaType.TEXT_PLAIN)
    public String deleteAllRelations() {
        try (Connection conn = CONSTANTS.getConnection()) {
            eventMapDao.deleteAll(conn);
            return CONSTANTS.SUCCESS;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return CONSTANTS.FAILURE + " " + throwables.getMessage();
        }
    }

}
