package nl.utwente.di.team26.Product.resources.TypeOfResource;

import nl.utwente.di.team26.CONSTANTS;
import nl.utwente.di.team26.Exceptions.DriverNotInstalledException;
import nl.utwente.di.team26.Product.dao.TypeOfResources.DrawingDao;
import nl.utwente.di.team26.Product.dao.TypeOfResources.TypeOfResourceDao;
import nl.utwente.di.team26.Product.model.TypeOfResource.Drawing;
import nl.utwente.di.team26.Security.Authentication.Secured;
import nl.utwente.di.team26.Security.Authentication.User.AuthenticatedUser;
import nl.utwente.di.team26.Security.Authentication.User.User;
import nl.utwente.di.team26.Security.Authorization.Role;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Path("/drawings")
public class DrawingsResource {

    @Inject
    @AuthenticatedUser
    User authenticatedUser;

    DrawingDao drawingDao = new DrawingDao();

    @GET
    @Secured(Role.VISITOR)
    @Produces(MediaType.APPLICATION_JSON)
    public List<Drawing> getAllDrawings() {
        try (Connection conn = CONSTANTS.getConnection()) {
            return drawingDao.loadAll(conn);
        } catch (NotFoundException | SQLException | DriverNotInstalledException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    @POST
    @Secured(Role.EDITOR)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String addNewDrawing(Drawing drawingToAdd) {
        try (Connection conn = CONSTANTS.getConnection()) {
            (new TypeOfResourceDao()).create(conn, drawingToAdd);
            return CONSTANTS.SUCCESS;
        } catch (SQLException | DriverNotInstalledException throwables) {
            throwables.printStackTrace();
            return CONSTANTS.FAILURE + " " + throwables.getMessage();
        }
    }
}
