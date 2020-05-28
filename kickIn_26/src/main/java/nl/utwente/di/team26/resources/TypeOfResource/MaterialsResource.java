package nl.utwente.di.team26.resources.TypeOfResource;

import nl.utwente.di.team26.CONSTANTS;
import nl.utwente.di.team26.Exceptions.DataSourceNotFoundException;
import nl.utwente.di.team26.dao.MaterialsDao;
import nl.utwente.di.team26.dao.TypeOfResourceDao;
import nl.utwente.di.team26.model.Materials;

import javax.naming.NamingException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Path("/materials")
public class MaterialsResource {

    MaterialsDao materialsDao = new MaterialsDao();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Materials> getAllDrawings() {
        try (Connection conn = CONSTANTS.getConnection()) {
            return materialsDao.loadAll(conn);
        } catch (NotFoundException | SQLException | DataSourceNotFoundException | NamingException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String addNewDrawing(Materials materialToAdd) {
        try (Connection conn = CONSTANTS.getConnection()) {
            (new TypeOfResourceDao()).create(conn, materialToAdd);
            return CONSTANTS.SUCCESS;
        } catch (SQLException | DataSourceNotFoundException | NamingException throwables) {
            throwables.printStackTrace();
            return CONSTANTS.FAILURE;
        }
    }

}
