package nl.utwente.di.team26.resources.TypeOfResource;

import nl.utwente.di.team26.CONSTANTS;
import nl.utwente.di.team26.Exceptions.DriverNotInstalledException;
import nl.utwente.di.team26.Exceptions.NotFoundException;
import nl.utwente.di.team26.dao.DrawingDao;
import nl.utwente.di.team26.dao.TypeOfResourceDao;
import nl.utwente.di.team26.model.Drawing;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.Connection;
import java.sql.SQLException;

@Path("/drawing/{drawingId}")
public class DrawingResource {

    DrawingDao drawingDao = new DrawingDao();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Drawing getDrawingObject(@PathParam("drawingId") int drawingId) {
        try (Connection conn = CONSTANTS.getConnection()) {
            return drawingDao.getObject(conn, drawingId);
        } catch (SQLException | NotFoundException | DriverNotInstalledException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String updateObject(Drawing drawingToUpdate) {
        try (Connection conn = CONSTANTS.getConnection()) {
            drawingDao.save(conn, drawingToUpdate);
            return CONSTANTS.SUCCESS;
        } catch (NotFoundException | SQLException | DriverNotInstalledException e) {
            return CONSTANTS.FAILURE + " " + e.getMessage();
        }
    }

    @DELETE
    @Produces(MediaType.TEXT_PLAIN)
    public String deleteObject(@PathParam("drawingId") int drawingId) {
        try (Connection conn = CONSTANTS.getConnection()) {
            (new TypeOfResourceDao()).delete(conn, new Drawing(drawingId));
            return CONSTANTS.SUCCESS;
        } catch (NotFoundException | SQLException | DriverNotInstalledException e) {
            return CONSTANTS.FAILURE + " " + e.getMessage();
        }
    }
}
