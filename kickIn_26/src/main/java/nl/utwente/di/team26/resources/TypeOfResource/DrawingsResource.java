package nl.utwente.di.team26.resources.TypeOfResource;

import nl.utwente.di.team26.CONSTANTS;
import nl.utwente.di.team26.dao.DrawingDao;
import nl.utwente.di.team26.model.Drawing;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

@Path("/drawings")
public class DrawingsResource {

    DrawingDao drawingDao = new DrawingDao();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Drawing> getAllDrawings() {
        try (Connection conn = DriverManager.getConnection(
                CONSTANTS.URL,
                CONSTANTS.USER,
                CONSTANTS.PASSWORD)) {
            return drawingDao.loadAll(conn);
        } catch (NotFoundException | SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String addNewDrawing(Drawing drawingToAdd) {
        try (Connection conn = DriverManager.getConnection(
                CONSTANTS.URL,
                CONSTANTS.USER,
                CONSTANTS.PASSWORD)) {
            drawingDao.create(conn, drawingToAdd);
            return CONSTANTS.SUCCESS;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return CONSTANTS.FAILURE;
        }
    }
}
