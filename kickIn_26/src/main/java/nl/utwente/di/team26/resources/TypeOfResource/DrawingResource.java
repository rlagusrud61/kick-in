package nl.utwente.di.team26.resources.TypeOfResource;

import nl.utwente.di.team26.CONSTANTS;
import nl.utwente.di.team26.Exceptions.NotFoundException;
import nl.utwente.di.team26.dao.DrawingDao;
import nl.utwente.di.team26.model.Drawing;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Path("/drawing/{drawingId}")
public class DrawingResource {

    DrawingDao drawingDao = new DrawingDao();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Drawing getDrawingObject(@PathParam("drawingId") int drawingId) {
        try (Connection conn = DriverManager.getConnection(
                CONSTANTS.URL,
                CONSTANTS.USER,
                CONSTANTS.PASSWORD)) {
            return drawingDao.getObject(conn, drawingId);
        } catch (SQLException | nl.utwente.di.team26.Exceptions.NotFoundException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String updateObject(Drawing drawingToUpdate) {
        try (Connection conn = DriverManager.getConnection(
                CONSTANTS.URL,
                CONSTANTS.USER,
                CONSTANTS.PASSWORD)) {
            drawingDao.save(conn, drawingToUpdate);
            return CONSTANTS.SUCCESS;
        } catch (nl.utwente.di.team26.Exceptions.NotFoundException | SQLException e) {
            return CONSTANTS.FAILURE;
        }
    }

    @DELETE
    @Produces(MediaType.TEXT_PLAIN)
    public String deleteObject(@PathParam("drawingId") int drawingId) {
        try (Connection conn = DriverManager.getConnection(
                CONSTANTS.URL,
                CONSTANTS.USER,
                CONSTANTS.PASSWORD)) {
            drawingDao.delete(conn, new Drawing(drawingId));
            return CONSTANTS.SUCCESS;
        } catch (NotFoundException | SQLException e) {
            return CONSTANTS.FAILURE;
        }
    }
}
