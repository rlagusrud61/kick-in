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
import java.util.List;

@Path("/events")
public class EventsResource {

    @Context
    SecurityContext securityContext;

    public EventsDao eventsDao = new EventsDao();

    @GET
    @Secured(Roles.VISITOR)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllEvents() {
        try (Connection conn = CONSTANTS.getConnection()) {
            String allEvents = eventsDao.getAllEvents(conn);
            return Response.ok(allEvents).build();
        } catch (NotFoundException throwables) {
            return Response.status(Response.Status.NOT_FOUND).entity(throwables.getMessage()).build();
        } catch (SQLException throwables) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(throwables.getMessage()).build();
        }
    }

    @POST
    @Secured({Roles.EDITOR})
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response addNewEvent(Event eventToAdd) {
        //get the userId stored into the security Context during authentication.
        long userId = Utils.getUserFromContext(securityContext);

        try (Connection conn = CONSTANTS.getConnection()) {
            eventToAdd.setCreatedBy(userId);
            eventToAdd.setLastEditedBy(userId);

            long eventId = eventsDao.create(conn, eventToAdd);
            return Response.status(Response.Status.CREATED).entity(eventId).build();
        } catch (SQLException throwables) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(throwables.getMessage()).build();
        }
    }

    @DELETE
    @Secured({Roles.ADMIN})
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteAllEvents() {
        try (Connection conn = CONSTANTS.getConnection()) {
            eventsDao.deleteAll(conn);
            return Response.noContent().build();
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

}
