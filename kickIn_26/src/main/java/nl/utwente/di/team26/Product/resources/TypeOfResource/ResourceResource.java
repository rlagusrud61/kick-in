package nl.utwente.di.team26.Product.resources.TypeOfResource;

import nl.utwente.di.team26.CONSTANTS;
import nl.utwente.di.team26.Exceptions.NotFoundException;
import nl.utwente.di.team26.Product.dao.Resources.ResourceDao;
import nl.utwente.di.team26.Product.model.TypeOfResource.TypeOfResource;
import nl.utwente.di.team26.Security.Filters.Secured;
import nl.utwente.di.team26.Security.User.Roles;
import nl.utwente.di.team26.Utils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.SQLException;

@Path("/resource/{resourceId}")
public class ResourceResource {

    ResourceDao resourceDao = new ResourceDao();

    @GET
    @Secured(Roles.VISITOR)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllResources(@PathParam("resourceId") long resourceId) {
        try (Connection conn = CONSTANTS.getConnection()) {
            String allMaps = resourceDao.getResource(conn, resourceId);
            return Utils.returnOkResponse(allMaps);
        } catch (NotFoundException e) {
            return Utils.returnNotFoundError(e.getMessage());
        } catch (SQLException e) {
            return Utils.returnInternalServerError(e.getMessage());
        }
    }

    @DELETE
    @Secured(Roles.ADMIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteResource(@PathParam("resourceId") long resourceId) {
        try (Connection conn = CONSTANTS.getConnection()) {
            resourceDao.delete(conn, new TypeOfResource(resourceId));
            return Utils.returnNoContent();
        } catch (NotFoundException e) {
            return Utils.returnNotFoundError(e.getMessage());
        } catch (SQLException e) {
            return Utils.returnInternalServerError(e.getMessage());
        }
    }

}
