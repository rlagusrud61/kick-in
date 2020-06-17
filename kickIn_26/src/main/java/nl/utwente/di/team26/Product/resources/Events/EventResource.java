package nl.utwente.di.team26.Product.resources.Events;

import nl.utwente.di.team26.CONSTANTS;
import nl.utwente.di.team26.Exceptions.NotFoundException;
import nl.utwente.di.team26.Product.dao.Events.EventsDao;
import nl.utwente.di.team26.Product.model.Event.Event;
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

@Path("/event/{eventId}")
public class EventResource {

    @Context
    SecurityContext securityContext;

    EventsDao eventsDao = new EventsDao();

    @GET
    @Secured({Roles.VISITOR})
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEventById(@PathParam("eventId") long eventId) {
        try (Connection conn = CONSTANTS.getConnection()) {
            String eventData = eventsDao.getEvent(conn, eventId);
            return Response.ok(eventData).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @PUT
    @Secured({Roles.EDITOR})
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response updateEvent(Event eventToUpdate) {

        long userId = Utils.getUserFromContext(securityContext);
        eventToUpdate.setLastEditedBy(userId);

        try (Connection conn = CONSTANTS.getConnection()) {
            eventsDao.save(conn, eventToUpdate);
            return Response.noContent().build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Secured({Roles.EDITOR})
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteEvent(@PathParam("eventId") int eventToDelete) {
        try (Connection conn = CONSTANTS.getConnection()) {
            eventsDao.delete(conn, new Event(eventToDelete));
            return Response.noContent().build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }
}
