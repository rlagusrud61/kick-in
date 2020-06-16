package nl.utwente.di.team26.Product.resources.Events;

import nl.utwente.di.team26.CONSTANTS;
import nl.utwente.di.team26.Product.dao.Events.EventsDao;
import nl.utwente.di.team26.Product.model.Event.Event;
import nl.utwente.di.team26.Security.Filters.Secured;
import nl.utwente.di.team26.Security.User.Roles;
import nl.utwente.di.team26.Utils;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Path("/events")
public class EventsResource {

    @Context
    SecurityContext securityContext;

    public EventsDao eventsDao = new EventsDao();

    @GET
    @Secured(Roles.VISITOR)
    @Produces(MediaType.APPLICATION_JSON)
    public List<Event> getAllEvents() {
        try (Connection conn = CONSTANTS.getConnection()) {
            return eventsDao.loadAll(conn);
        } catch (NotFoundException | SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    @POST
    @Secured({Roles.EDITOR})
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String addNewEvent(Event eventToAdd) {
        long userId = Utils.getUserFromContext(securityContext);
        try (Connection conn = CONSTANTS.getConnection()) {
            eventToAdd.setCreatedBy(userId);
            eventToAdd.setLastEditedBy(userId);
            return String.valueOf(eventsDao.create(conn, eventToAdd));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return CONSTANTS.FAILURE + ": " + throwables.getMessage();
        }
    }

    @DELETE
    @Secured({Roles.ADMIN})
    @Produces(MediaType.TEXT_PLAIN)
    public String deleteAllEvents() {
        try (Connection conn = CONSTANTS.getConnection()) {
            eventsDao.deleteAll(conn);
            return CONSTANTS.SUCCESS;
        } catch (SQLException e) {
            e.printStackTrace();
            return CONSTANTS.FAILURE + ": " + e.getMessage();
        }
    }

}
