package nl.utwente.di.team26.resources.Events;

import nl.utwente.di.team26.CONSTANTS;
import nl.utwente.di.team26.Exceptions.DataSourceNotFoundException;
import nl.utwente.di.team26.dao.EventsDao;
import nl.utwente.di.team26.model.Events;

import javax.naming.NamingException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Path("/events")
public class EventsResource {

    public EventsDao eventsDao = new EventsDao();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Events> getAllEvents() {
        try (Connection conn = CONSTANTS.getConnection()) {
            return eventsDao.loadAll(conn);
        } catch (NotFoundException | SQLException | NamingException | DataSourceNotFoundException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String addNewEvent(Events eventToAdd) {
        try (Connection conn = CONSTANTS.getConnection()) {
            conn.setSchema(CONSTANTS.SCHEMA);
            eventsDao.create(conn, eventToAdd);
            return CONSTANTS.SUCCESS;
        } catch (SQLException | NamingException | DataSourceNotFoundException throwables) {
            throwables.printStackTrace();
            return CONSTANTS.FAILURE;
        }
    }
}
