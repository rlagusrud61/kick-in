package nl.utwente.di.team26.resources;

import nl.utwente.di.team26.dao.ResourcesDao;
import nl.utwente.di.team26.model.Resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Path("/{mapId}/resources")
public class ResourcesResource {

    @Path("all")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Resources> getAllResources() {
        return new ArrayList<>(ResourcesDao.instance.getAllResources().values());
    }

    @Path("allForMap")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Resources> getAllResourcesForMap(@PathParam("mapId") String mapId) {
        return new ArrayList<>(ResourcesDao.instance.getAllResourcesForMap(mapId).values());
    }

    @Path("count")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getCount(@PathParam("mapId") String mapId) {
        return String.valueOf(ResourcesDao.instance.getAllResourcesForMap(mapId).size());
    }

    @GET
    @Path("{objectId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Resources getResource(@PathParam("objectId") String objectId) {
        return ResourcesDao.instance.getResource(objectId);
    }

}
