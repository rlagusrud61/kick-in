package nl.utwente.di.team26.resources;

import nl.utwente.di.team26.dao.StaticResourceDao;
import nl.utwente.di.team26.model.Static;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Path("/staticResource")
public class StaticResource {

    @Path("all")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Static> getAllResources() {
        return new ArrayList<>(StaticResourceDao.instance.getAllStaticResources().values());
    }

    @Path("count")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getCount() {
        return String.valueOf(StaticResourceDao.instance.getAllStaticResources().size());
    }

    @GET
    @Path("{staticId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Static getStaticResource(@PathParam("staticId") String staticId) {
        return StaticResourceDao.instance.getStaticResource(staticId);
    }

}
