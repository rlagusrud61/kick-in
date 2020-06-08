package nl.utwente.di.team26.Product.resources.TypeOfResource;

import nl.utwente.di.team26.CONSTANTS;
import nl.utwente.di.team26.Exceptions.DriverNotInstalledException;
import nl.utwente.di.team26.Exceptions.NotFoundException;
import nl.utwente.di.team26.Product.dao.TypeOfResources.MaterialsDao;
import nl.utwente.di.team26.Product.dao.TypeOfResources.TypeOfResourceDao;
import nl.utwente.di.team26.Product.model.TypeOfResource.Material;
import nl.utwente.di.team26.Security.Authentication.Secured;
import nl.utwente.di.team26.Security.Authentication.User.AuthenticatedUser;
import nl.utwente.di.team26.Security.Authentication.User.User;
import nl.utwente.di.team26.Security.Authorization.Role;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.Connection;
import java.sql.SQLException;

@Path("/materials/{materialId}")
public class MaterialResource {

    @Inject
    @AuthenticatedUser
    User authenticatedUser;

    MaterialsDao materialsDao = new MaterialsDao();

    @GET
    @Secured(Role.VISITOR)
    @Produces(MediaType.APPLICATION_JSON)
    public Material getDrawingObject(@PathParam("materialId") int materialId) {
        try (Connection conn = CONSTANTS.getConnection()) {
            return materialsDao.getObject(conn, materialId);
        } catch (SQLException | NotFoundException | DriverNotInstalledException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    @PUT
    @Secured(Role.ADMIN)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String updateObject(Material materialToUpdate) {
        try (Connection conn = CONSTANTS.getConnection()) {
            materialsDao.save(conn, materialToUpdate);
            return CONSTANTS.SUCCESS;
        } catch (NotFoundException | SQLException | DriverNotInstalledException e) {
            return CONSTANTS.FAILURE + " " + e.getMessage();
        }
    }

    @DELETE
    @Secured(Role.ADMIN)
    @Produces(MediaType.TEXT_PLAIN)
    public String deleteObject(@PathParam("materialId") int materialId) {
        try (Connection conn = CONSTANTS.getConnection()) {
            (new TypeOfResourceDao()).delete(conn, new Material(materialId));
            return CONSTANTS.SUCCESS;
        } catch (NotFoundException | SQLException | DriverNotInstalledException e) {
            return CONSTANTS.FAILURE + " " + e.getMessage();
        }
    }

}
