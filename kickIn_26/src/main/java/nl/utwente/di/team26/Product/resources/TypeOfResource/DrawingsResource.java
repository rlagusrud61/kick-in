package nl.utwente.di.team26.Product.resources.TypeOfResource;

import nl.utwente.di.team26.CONSTANTS;
import nl.utwente.di.team26.Product.dao.TypeOfResources.DrawingDao;
import nl.utwente.di.team26.Product.dao.TypeOfResources.TypeOfResourceDao;
import nl.utwente.di.team26.Product.model.TypeOfResource.Drawing;
import nl.utwente.di.team26.Security.Filters.Secured;
import nl.utwente.di.team26.Security.User.Roles;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Path("/drawings")
public class DrawingsResource {



    DrawingDao drawingDao = new DrawingDao();

    @GET
    @Secured(Roles.VISITOR)
    @Produces(MediaType.APPLICATION_JSON)
    public List<Drawing> getAllDrawings() {
        try (Connection conn = CONSTANTS.getConnection()) {
            return drawingDao.loadAll(conn);
        } catch (NotFoundException | SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    @POST
    @Secured(Roles.EDITOR)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String addNewDrawing(Drawing drawingToAdd) {
        try (Connection conn = CONSTANTS.getConnection()) {
            (new TypeOfResourceDao()).create(conn, drawingToAdd);
            return CONSTANTS.SUCCESS;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return CONSTANTS.FAILURE + " " + throwables.getMessage();
        }
    }
}
