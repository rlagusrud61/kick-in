package nl.utwente.di.team26.resources;

import nl.utwente.di.team26.dao.MapsDao;
import nl.utwente.di.team26.model.Maps;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Path("/maps")
public class MapsResource {

    @Path("all")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Maps> getAllMaps() {
        return new ArrayList<>(MapsDao.instance.getAllMaps().values());
    }

    @Path("count")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getCount() {
        return String.valueOf(MapsDao.instance.getAllMaps().size());
    }

    @GET
    @Path("{eventId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Maps getMap(@PathParam("eventId") String eventId) {
        return MapsDao.instance.getMap(eventId);
    }


}