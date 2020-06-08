package nl.utwente.di.team26.Product.resources.TypeOfResource;

import nl.utwente.di.team26.CONSTANTS;
import nl.utwente.di.team26.Exceptions.DriverNotInstalledException;
import nl.utwente.di.team26.Exceptions.NotFoundException;
import nl.utwente.di.team26.Product.dao.TypeOfResources.TypeOfResourceDao;
import nl.utwente.di.team26.Product.model.TypeOfResource.TypeOfResource;

import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.sql.Connection;
import java.sql.SQLException;

@Path("/resources")
public class ResourcesResource {

    TypeOfResourceDao typeOfResourceDao = new TypeOfResourceDao();

    @DELETE
    @Produces(MediaType.TEXT_PLAIN)
    public String deleteObject() {
        try (Connection conn = CONSTANTS.getConnection()) {
            typeOfResourceDao.deleteAll(conn);
            return CONSTANTS.SUCCESS;
        } catch (SQLException | DriverNotInstalledException e) {
            return CONSTANTS.FAILURE + " " + e.getMessage();
        }
    }

    @DELETE
    @Path("{resourceId}")
    @Produces(MediaType.TEXT_PLAIN)
    public String deleteResource(@PathParam("resourceId") int resourceId) {
        try (Connection conn = CONSTANTS.getConnection()) {
            typeOfResourceDao.delete(conn, new TypeOfResource(resourceId));
            return CONSTANTS.SUCCESS;
        } catch (SQLException | NotFoundException | DriverNotInstalledException e) {
            e.printStackTrace();
            return CONSTANTS.FAILURE + ": " + e.getMessage();
        }
    }

}
