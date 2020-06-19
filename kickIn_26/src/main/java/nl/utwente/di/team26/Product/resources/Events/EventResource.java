package nl.utwente.di.team26.Product.resources.Events;

import nl.utwente.di.team26.CONSTANTS;
import nl.utwente.di.team26.Exception.Exceptions.NotFoundException;
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
    public Response getEventById(@PathParam("eventId") long eventId) throws SQLException, NotFoundException {

            String eventData = eventsDao.get(eventId);
            return Utils.returnOkResponse(eventData);
    }

    @PUT
    @Secured({Roles.EDITOR})
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateEvent(Event eventToUpdate) throws NotFoundException, SQLException {

        long userId = Utils.getUserFromContext(securityContext);
        eventToUpdate.setLastEditedBy(userId);


            eventsDao.save(eventToUpdate);
            return Utils.returnNoContent();
    }

    @DELETE
    @Secured({Roles.EDITOR})
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteEvent(@PathParam("eventId") int eventToDelete) throws NotFoundException, SQLException {

            eventsDao.delete(new Event(eventToDelete));
            return Utils.returnNoContent();
    }
}
