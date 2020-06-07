package nl.utwente.di.team26.resources.TypeOfResource;

import nl.utwente.di.team26.CONSTANTS;
import nl.utwente.di.team26.Exceptions.DriverNotInstalledException;
import nl.utwente.di.team26.Exceptions.NotFoundException;
import nl.utwente.di.team26.dao.MaterialsDao;
import nl.utwente.di.team26.dao.TypeOfResourceDao;
import nl.utwente.di.team26.model.Materials;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.Connection;
import java.sql.SQLException;

@Path("/materials/{materialId}")
public class MaterialResource {

    MaterialsDao materialsDao = new MaterialsDao();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Materials getDrawingObject(@PathParam("materialId") int materialId) {
        try (Connection conn = CONSTANTS.getConnection()) {
            return materialsDao.getObject(conn, materialId);
        } catch (SQLException | NotFoundException | DriverNotInstalledException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String updateObject(Materials materialToUpdate) {
        try (Connection conn = CONSTANTS.getConnection()) {
            materialsDao.save(conn, materialToUpdate);
            return CONSTANTS.SUCCESS;
        } catch (NotFoundException | SQLException | DriverNotInstalledException e) {
            return CONSTANTS.FAILURE + " " + e.getMessage();
        }
    }

    @DELETE
    @Produces(MediaType.TEXT_PLAIN)
    public String deleteObject(@PathParam("materialId") int materialId) {
        try (Connection conn = CONSTANTS.getConnection()) {
            (new TypeOfResourceDao()).delete(conn, new Materials(materialId));
            return CONSTANTS.SUCCESS;
        } catch (NotFoundException | SQLException | DriverNotInstalledException e) {
            return CONSTANTS.FAILURE + " " + e.getMessage();
        }
    }

}
