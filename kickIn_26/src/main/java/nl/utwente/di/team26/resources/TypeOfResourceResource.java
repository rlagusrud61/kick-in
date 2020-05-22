package nl.utwente.di.team26.resources;

import nl.utwente.di.team26.dao.ResourcesDao;
import nl.utwente.di.team26.dao.TypeOfResourceDao;
import nl.utwente.di.team26.model.Resources;
import nl.utwente.di.team26.model.TypeOfResource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Path("/typeOfResource")
public class TypeOfResourceResource {

    @Path("all")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<TypeOfResource> getAllResources() {
        return new ArrayList<>(TypeOfResourceDao.instance.getAllTypeOfResources().values());
    }

    @Path("count")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getCount() {
        return String.valueOf(TypeOfResourceDao.instance.getAllTypeOfResources().size());
    }

    @GET
    @Path("{typeOfResourceId}")
    @Produces(MediaType.APPLICATION_JSON)
    public TypeOfResource getTypeOfResource(@PathParam("typeOfResourceId") String typeOfResourceId) {
        return TypeOfResourceDao.instance.getTypeOfResource(typeOfResourceId);
    }

}
