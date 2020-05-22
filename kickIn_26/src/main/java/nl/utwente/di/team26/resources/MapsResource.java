package nl.utwente.di.team26.resources;

import nl.utwente.di.team26.dao.MapsDao;
import nl.utwente.di.team26.model.Maps;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Path("/{eventId}/maps")
public class MapsResource {

    @Path("all")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Maps> getAllMaps() {
        return new ArrayList<>(MapsDao.instance.getAllMaps().values());
    }

    @Path("allForEvent")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Maps> getAllMapsForEvent(@PathParam("eventId") String eventId) {
        return new ArrayList<>(MapsDao.instance.getAllMapsForEvent(eventId).values());
    }

    @Path("count")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getCount() {
        return String.valueOf(MapsDao.instance.getAllMaps().size());
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void createEvent(Maps map) {
        MapsDao.instance.addMap(map);
    }

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    public void deleteMap(Maps map) {
        MapsDao.instance.getAllMaps().remove(String.valueOf(map.getMap_id()));
    }

}
