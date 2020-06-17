package nl.utwente.di.team26.Product.resources.Maps;

import nl.utwente.di.team26.CONSTANTS;
import nl.utwente.di.team26.Exceptions.NotFoundException;
import nl.utwente.di.team26.Product.dao.Events.EventMapDao;
import nl.utwente.di.team26.Product.dao.Events.EventsDao;
import nl.utwente.di.team26.Product.dao.Maps.MapsDao;
import nl.utwente.di.team26.Product.model.Event.Event;
import nl.utwente.di.team26.Product.model.Event.EventMap;
import nl.utwente.di.team26.Product.model.Map.Map;
import nl.utwente.di.team26.Security.Filters.Secured;
import nl.utwente.di.team26.Security.User.Roles;
import nl.utwente.di.team26.Utils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Path("/eventMap")
public class EventMapResource {

    EventMapDao eventMapDao = new EventMapDao();

    @GET
    @Secured({Roles.VISITOR})
    @Path("event/{eventId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllMapsForEvent(@PathParam("eventId") long eventId) {
        try (Connection conn = CONSTANTS.getConnection()) {
            return Utils.returnOkResponse(eventMapDao.getAllMapsFor(conn, eventId));
        } catch (NotFoundException e) {
            return Utils.returnNotFoundError(e.getMessage());
        } catch (SQLException e) {
            return Utils.returnInternalServerError(e.getMessage());
        }
    }

    @GET
    @Secured({Roles.VISITOR})
    @Path("map/{mapId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllEventsForMap(@PathParam("mapId") int mapId) {
        try (Connection conn = CONSTANTS.getConnection()) {
            return Utils.returnOkResponse(eventMapDao.allEventsFor(conn, mapId));
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
    public Response addEventMap(EventMap eventMapToCreate) {
        try (Connection conn = CONSTANTS.getConnection()) {
            eventMapDao.create(conn, eventMapToCreate);
            return Utils.returnCreated();
        } catch (SQLException e) {
            return Utils.returnInternalServerError(e.getMessage());
        }
    }

    @DELETE
    @Secured(Roles.EDITOR)
    @Path("event/{eventId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response clearEvent(@PathParam("eventId") long eventId) {
        try (Connection conn = CONSTANTS.getConnection()) {
            eventMapDao.deleteAllForEvent(conn, eventId);
            return Utils.returnNoContent();
        } catch (NotFoundException e) {
            return Utils.returnNotFoundError(e.getMessage());
        } catch (SQLException e) {
            return Utils.returnInternalServerError(e.getMessage());
        }
    }

    @DELETE
    @Secured(Roles.EDITOR)
    @Path("{eventId}/{mapId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteEventMap(@PathParam("eventId") long eventId, @PathParam("mapId") long mapId) {
        try (Connection conn = CONSTANTS.getConnection()) {
            eventMapDao.delete(conn, new EventMap(eventId, mapId));
            return Utils.returnNoContent();
        } catch (NotFoundException e) {
            return Utils.returnNotFoundError(e.getMessage());
        } catch (SQLException e) {
            return Utils.returnInternalServerError(e.getMessage());
        }
    }

    @DELETE
    @Secured(Roles.ADMIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteAllRelations() {
        try (Connection conn = CONSTANTS.getConnection()) {
            eventMapDao.deleteAll(conn);
            return Utils.returnNoContent();
        } catch (SQLException e) {
            return Utils.returnInternalServerError(e.getMessage());
        }
    }

}
