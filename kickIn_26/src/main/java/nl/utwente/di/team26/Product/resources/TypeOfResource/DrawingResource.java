package nl.utwente.di.team26.Product.resources.TypeOfResource;

import nl.utwente.di.team26.CONSTANTS;
import nl.utwente.di.team26.Exceptions.NotFoundException;
import nl.utwente.di.team26.Product.dao.TypeOfResources.DrawingDao;
import nl.utwente.di.team26.Product.dao.TypeOfResources.TypeOfResourceDao;
import nl.utwente.di.team26.Product.model.TypeOfResource.Drawing;
import nl.utwente.di.team26.Security.Authentication.Secured;
import nl.utwente.di.team26.Security.Authentication.User.User;
import nl.utwente.di.team26.Security.Authorization.Role;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.Connection;
import java.sql.SQLException;

@Path("/drawing/{drawingId}")
public class DrawingResource {



    DrawingDao drawingDao = new DrawingDao();

    @GET
    @Secured(Role.VISITOR)
    @Produces(MediaType.APPLICATION_JSON)
    public Drawing getDrawingObject(@PathParam("drawingId") int drawingId) {
        try (Connection conn = CONSTANTS.getConnection()) {
            return drawingDao.getObject(conn, drawingId);
        } catch (SQLException | NotFoundException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    @PUT
    @Secured(Role.EDITOR)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String updateObject(Drawing drawingToUpdate) {
        try (Connection conn = CONSTANTS.getConnection()) {
            drawingDao.save(conn, drawingToUpdate);
            return CONSTANTS.SUCCESS;
        } catch (NotFoundException | SQLException e) {
            return CONSTANTS.FAILURE + " " + e.getMessage();
        }
    }

    @DELETE
    @Secured(Role.EDITOR)
    @Produces(MediaType.TEXT_PLAIN)
    public String deleteObject(@PathParam("drawingId") int drawingId) {
        try (Connection conn = CONSTANTS.getConnection()) {
            (new TypeOfResourceDao()).delete(conn, new Drawing(drawingId));
            return CONSTANTS.SUCCESS;
        } catch (NotFoundException | SQLException e) {
            return CONSTANTS.FAILURE + " " + e.getMessage();
        }
    }
}
