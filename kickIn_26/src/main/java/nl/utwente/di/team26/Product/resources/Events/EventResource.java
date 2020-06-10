package nl.utwente.di.team26.Product.resources.Events;

import nl.utwente.di.team26.CONSTANTS;
import nl.utwente.di.team26.Exceptions.NotFoundException;
import nl.utwente.di.team26.Product.dao.Events.EventsDao;
import nl.utwente.di.team26.Product.model.Event.Event;
import nl.utwente.di.team26.Security.Filters.Secured;
import nl.utwente.di.team26.Security.User.Roles;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.Connection;
import java.sql.SQLException;

@Path("/event/{eventId}")
public class EventResource {

    @GET
    @Secured({Roles.VISITOR})
    @Produces(MediaType.APPLICATION_JSON)
    public Event getEventById(@PathParam("eventId") int eventId) {
        try (Connection conn = CONSTANTS.getConnection()) {
            return (new EventsDao()).getObject(conn, eventId);
        } catch (NotFoundException | SQLException e) {
            return null;
        }
    }

    @PUT
    @Secured({Roles.EDITOR})
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String updateEvent(Event eventToUpdate) {
        try (Connection conn = CONSTANTS.getConnection()) {
            (new EventsDao()).save(conn, eventToUpdate);
            return CONSTANTS.SUCCESS;
        } catch (NotFoundException | SQLException e) {
            return CONSTANTS.FAILURE + " " + e.getMessage();
        }
    }

    @DELETE
    @Secured({Roles.EDITOR})
    @Produces(MediaType.TEXT_PLAIN)
    public String deleteEvent(@PathParam("eventId") int eventToDelete) {
        try (Connection conn = CONSTANTS.getConnection()) {
            (new EventsDao()).delete(conn, new Event(eventToDelete));
            return CONSTANTS.SUCCESS;
        } catch (NotFoundException | SQLException e) {
            return CONSTANTS.FAILURE + " " + e.getMessage();
        }
    }
}
