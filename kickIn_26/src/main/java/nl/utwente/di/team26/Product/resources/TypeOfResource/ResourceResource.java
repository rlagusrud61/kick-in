package nl.utwente.di.team26.Product.resources.TypeOfResource;

import nl.utwente.di.team26.Exception.Exceptions.NotFoundException;
import nl.utwente.di.team26.Product.dao.Resources.ResourceDao;
import nl.utwente.di.team26.Product.model.TypeOfResource.TypeOfResource;
import nl.utwente.di.team26.Security.Filters.Secured;
import nl.utwente.di.team26.Security.User.Roles;
import nl.utwente.di.team26.Utils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;

@Path("/resource/{resourceId}")
public class ResourceResource {

    ResourceDao resourceDao = new ResourceDao();

    @GET
    @Secured(Roles.VISITOR)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getResourceById(@PathParam("resourceId") long resourceId) throws NotFoundException, SQLException {

            String allMaps = resourceDao.getResource(resourceId);
            return Utils.returnOkResponse(allMaps);
    }

    @DELETE
    @Secured(Roles.ADMIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteResource(@PathParam("resourceId") long resourceId) throws NotFoundException, SQLException {

            resourceDao.delete(new TypeOfResource(resourceId));
            return Utils.returnNoContent();
    }

}
