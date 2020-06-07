package nl.utwente.di.team26.resources.Events;

import nl.utwente.di.team26.CONSTANTS;
import nl.utwente.di.team26.Exceptions.DriverNotInstalledException;
import nl.utwente.di.team26.Exceptions.NotFoundException;
import nl.utwente.di.team26.dao.EventsDao;
import nl.utwente.di.team26.model.Events;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.Connection;
import java.sql.SQLException;

@Path("/event/{eventId}")
public class EventResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Events getEventById(@PathParam("eventId") int eventId) {
        try (Connection conn = CONSTANTS.getConnection()) {
            return (new EventsDao()).getObject(conn, eventId);
        } catch (NotFoundException | SQLException | DriverNotInstalledException e) {
            return null;
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String updateEvent(Events eventToUpdate) {
        try (Connection conn = CONSTANTS.getConnection()) {
            (new EventsDao()).save(conn, eventToUpdate);
            return CONSTANTS.SUCCESS;
        } catch (NotFoundException | SQLException | DriverNotInstalledException e) {
            return CONSTANTS.FAILURE + " " + e.getMessage();
        }
    }

    @DELETE
    @Produces(MediaType.TEXT_PLAIN)
    public String deleteEvent(@PathParam("eventId") int eventToDelete) {
        try (Connection conn = CONSTANTS.getConnection()) {
            (new EventsDao()).delete(conn, new Events(eventToDelete));
            return CONSTANTS.SUCCESS;
        } catch (NotFoundException | SQLException | DriverNotInstalledException e) {
            return CONSTANTS.FAILURE + " " + e.getMessage();
        }
    }
}
