package nl.utwente.di.team26.Product.resources.Maps;

import nl.utwente.di.team26.CONSTANTS;
import nl.utwente.di.team26.Exception.Exceptions.NotFoundException;
import nl.utwente.di.team26.Product.dao.Events.EventMapDao;
import nl.utwente.di.team26.Product.model.Event.EventMap;
import nl.utwente.di.team26.Security.Filters.Secured;
import nl.utwente.di.team26.Security.User.Roles;
import nl.utwente.di.team26.Utils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.SQLException;

@Path("/eventMap")
public class EventMapResource {

    EventMapDao eventMapDao = new EventMapDao();

    @GET
    @Secured({Roles.VISITOR})
    @Path("event/{eventId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllMapsForEvent(@PathParam("eventId") long eventId) throws NotFoundException, SQLException {
        try (Connection conn = CONSTANTS.getConnection()) {
            return Utils.returnOkResponse(eventMapDao.getAllMapsFor(conn, eventId));
        }
    }

    @GET
    @Secured({Roles.VISITOR})
    @Path("map/{mapId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllEventsForMap(@PathParam("mapId") long mapId) throws NotFoundException, SQLException {
        try (Connection conn = CONSTANTS.getConnection()) {
            return Utils.returnOkResponse(eventMapDao.allEventsFor(conn, mapId));
        }
    }

    @POST
    @Secured(Roles.EDITOR)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addEventMap(EventMap eventMapToCreate) throws SQLException {
        try (Connection conn = CONSTANTS.getConnection()) {
            eventMapDao.create(conn, eventMapToCreate);
            return Utils.returnCreated();
        }
    }

    @DELETE
    @Secured(Roles.EDITOR)
    @Path("event/{eventId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response clearEvent(@PathParam("eventId") long eventId) throws NotFoundException, SQLException {
        try (Connection conn = CONSTANTS.getConnection()) {
            eventMapDao.deleteAllForEvent(conn, eventId);
            return Utils.returnNoContent();
        }
    }

    @DELETE
    @Secured(Roles.EDITOR)
    @Path("{eventId}/{mapId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteEventMap(@PathParam("eventId") long eventId, @PathParam("mapId") long mapId) throws NotFoundException, SQLException {
        try (Connection conn = CONSTANTS.getConnection()) {
            eventMapDao.delete(conn, new EventMap(eventId, mapId));
            return Utils.returnNoContent();
        }
    }

    @DELETE
    @Secured(Roles.ADMIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteAllRelations() throws SQLException {
        try (Connection conn = CONSTANTS.getConnection()) {
            eventMapDao.deleteAll(conn);
            return Utils.returnNoContent();
        }
    }

}
