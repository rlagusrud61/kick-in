package nl.utwente.di.team26.resources.TypeOfResource;

import nl.utwente.di.team26.CONSTANTS;
import nl.utwente.di.team26.Exceptions.DriverNotInstalledException;
import nl.utwente.di.team26.dao.DrawingDao;
import nl.utwente.di.team26.dao.TypeOfResourceDao;
import nl.utwente.di.team26.model.Drawing;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Path("/drawings")
public class DrawingsResource {

    DrawingDao drawingDao = new DrawingDao();

    @GET
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
