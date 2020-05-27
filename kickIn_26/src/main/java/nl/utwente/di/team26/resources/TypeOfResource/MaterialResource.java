package nl.utwente.di.team26.resources.TypeOfResource;

import nl.utwente.di.team26.CONSTANTS;
import nl.utwente.di.team26.Exceptions.NotFoundException;
import nl.utwente.di.team26.dao.MaterialsDao;
import nl.utwente.di.team26.model.Materials;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Path("/materials/{materialId}")
public class MaterialResource {

    MaterialsDao materialsDao = new MaterialsDao();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Materials getDrawingObject(@PathParam("materialId") int materialId) {
        try (Connection conn = DriverManager.getConnection(
                CONSTANTS.URL,
                CONSTANTS.USER,
                CONSTANTS.PASSWORD)) {
            return materialsDao.getObject(conn, materialId);
        } catch (SQLException | nl.utwente.di.team26.Exceptions.NotFoundException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String updateObject(Materials materialToUpdate) {
        try (Connection conn = DriverManager.getConnection(
                CONSTANTS.URL,
                CONSTANTS.USER,
                CONSTANTS.PASSWORD)) {
            materialsDao.save(conn, materialToUpdate);
            return CONSTANTS.SUCCESS;
        } catch (nl.utwente.di.team26.Exceptions.NotFoundException | SQLException e) {
            return CONSTANTS.FAILURE;
        }
    }

    @DELETE
    @Produces(MediaType.TEXT_PLAIN)
    public String deleteObject(@PathParam("materialId") int materialId) {
        try (Connection conn = DriverManager.getConnection(
                CONSTANTS.URL,
                CONSTANTS.USER,
                CONSTANTS.PASSWORD)) {
            materialsDao.delete(conn, new Materials(materialId));
            return CONSTANTS.SUCCESS;
        } catch (NotFoundException | SQLException e) {
            return CONSTANTS.FAILURE;
        }
    }

}
