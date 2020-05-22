package nl.utwente.di.team26.resources;

import nl.utwente.di.team26.dao.EventDao;
import nl.utwente.di.team26.model.Event;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
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

}
