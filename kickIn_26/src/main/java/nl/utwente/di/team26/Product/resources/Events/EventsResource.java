package nl.utwente.di.team26.Product.resources.Events;

import nl.utwente.di.team26.CONSTANTS;
import nl.utwente.di.team26.Product.dao.Events.EventsDao;
import nl.utwente.di.team26.Product.model.Event.Event;
import nl.utwente.di.team26.Security.Authentication.Secured;
import nl.utwente.di.team26.Security.Authentication.User.User;
import nl.utwente.di.team26.Security.Authorization.Role;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Path("/events")
public class EventsResource {



    public EventsDao eventsDao = new EventsDao();

    @GET
    @Secured({Role.VISITOR})
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
    @Secured({Role.EDITOR})
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String addNewEvent(Event eventToAdd) {
        try (Connection conn = CONSTANTS.getConnection()) {
            eventsDao.create(conn, eventToAdd);
            return CONSTANTS.SUCCESS;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return CONSTANTS.FAILURE + ": " + throwables.getMessage();
        }
    }

    @DELETE
    @Secured({Role.ADMIN})
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
