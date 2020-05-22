package nl.utwente.di.team26.resources;

import nl.utwente.di.team26.dao.EventDao;
import nl.utwente.di.team26.model.Event;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Path("/events")
public class EventsResource {

    @Path("all")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Event> getAllEvents() {
        return new ArrayList<>(EventDao.instance.getAllEvents().values());
    }

    @Path("count")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getCount() {
        return String.valueOf(EventDao.instance.getAllEvents().size());
    }

    @GET
    @Path("{mapId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Event getEvent(@PathParam("mapId") String eventId) {
        return EventDao.instance.getEvent(eventId);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void createEvent(Event event) {
        EventDao.instance.addEvent(event);
    }

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    public void deleteEvent(Event event) {
        EventDao.instance.getAllEvents().remove(String.valueOf(event.getEvent_id()));
    }

}
