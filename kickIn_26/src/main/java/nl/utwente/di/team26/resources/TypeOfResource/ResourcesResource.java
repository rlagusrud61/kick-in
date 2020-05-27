package nl.utwente.di.team26.resources.TypeOfResource;

import nl.utwente.di.team26.CONSTANTS;
import nl.utwente.di.team26.Exceptions.DataSourceNotFoundException;
import nl.utwente.di.team26.Exceptions.NotFoundException;
import nl.utwente.di.team26.dao.TypeOfResourceDao;
import nl.utwente.di.team26.model.TypeOfResource;

import javax.naming.NamingException;
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
        } catch (SQLException | DataSourceNotFoundException | NamingException e) {
            return CONSTANTS.FAILURE;
        }
    }

    @DELETE
    @Path("{resourceId}")
    @Produces(MediaType.TEXT_PLAIN)
    public String deleteResource(@PathParam("resourceId") int resourceId) {
        try (Connection conn = CONSTANTS.getConnection()) {
            typeOfResourceDao.delete(conn, new TypeOfResource(resourceId));
            return CONSTANTS.SUCCESS;
        } catch (DataSourceNotFoundException | SQLException | NamingException | NotFoundException e) {
            e.printStackTrace();
            return CONSTANTS.FAILURE + ": " + e.getMessage();
        }
    }

}
