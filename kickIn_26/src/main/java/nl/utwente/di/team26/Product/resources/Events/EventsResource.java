package nl.utwente.di.team26.Product.resources.Events;

import nl.utwente.di.team26.Exception.Exceptions.NotFoundException;
import nl.utwente.di.team26.Product.dao.Events.EventsDao;
import nl.utwente.di.team26.Product.model.Event.Event;
import nl.utwente.di.team26.Security.Filters.Secured;
import nl.utwente.di.team26.Security.User.Roles;
import nl.utwente.di.team26.Utils;
import org.springframework.web.util.HtmlUtils;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.sql.SQLException;

@Path("/events")
public class EventsResource {

    @Context
    SecurityContext securityContext;

    public EventsDao eventsDao = new EventsDao();

    @GET
    @Secured(Roles.VISITOR)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllEvents() throws NotFoundException, SQLException {
        String allEvents = eventsDao.getAll();
        return Utils.returnOkResponse(allEvents);
    }

    @POST
    @Secured({Roles.EDITOR})
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addNewEvent(Event eventToAdd) throws SQLException {
        //get the userId stored into the security Context during authentication.
        long userId = Utils.getUserFromContext(securityContext);
        eventToAdd.setName(HtmlUtils.htmlEscape(eventToAdd.getName()));
        eventToAdd.setCreatedBy(userId);
        eventToAdd.setLastEditedBy(userId);
        System.out.println(HtmlUtils.htmlEscape(eventToAdd.toString()));
        long eventId = eventsDao.create(eventToAdd);
        return Utils.returnCreated(eventId);
    }

    @DELETE
    @Secured({Roles.ADMIN})
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteAllEvents() throws SQLException, NotFoundException {
        eventsDao.deleteAll();
        return Utils.returnNoContent();
    }

}
