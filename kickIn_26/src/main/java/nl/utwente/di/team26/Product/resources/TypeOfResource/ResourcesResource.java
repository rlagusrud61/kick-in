package nl.utwente.di.team26.Product.resources.TypeOfResource;

import nl.utwente.di.team26.CONSTANTS;
import nl.utwente.di.team26.Exceptions.NotFoundException;
import nl.utwente.di.team26.Product.dao.Resources.ResourceDao;
import nl.utwente.di.team26.Security.Filters.Secured;
import nl.utwente.di.team26.Security.User.Roles;
import nl.utwente.di.team26.Utils;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.SQLException;

@Path("/resources")
public class ResourcesResource {

    ResourceDao resourceDao = new ResourceDao();

    @GET
    @Secured(Roles.VISITOR)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllResources() throws NotFoundException, SQLException {
        try (Connection conn = CONSTANTS.getConnection()) {
            String allMaps = resourceDao.getAllResources(conn);
            return Utils.returnOkResponse(allMaps);
        }
    }

    @DELETE
    @Secured(Roles.ADMIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteAllResources() throws SQLException {
        try (Connection conn = CONSTANTS.getConnection()) {
            resourceDao.deleteAll(conn);
            return Utils.returnNoContent();
        }
    }

}
