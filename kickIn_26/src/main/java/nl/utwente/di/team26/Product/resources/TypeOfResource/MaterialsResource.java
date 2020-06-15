package nl.utwente.di.team26.Product.resources.TypeOfResource;

import nl.utwente.di.team26.CONSTANTS;
import nl.utwente.di.team26.Product.dao.TypeOfResources.MaterialsDao;
import nl.utwente.di.team26.Product.dao.TypeOfResources.TypeOfResourceDao;
import nl.utwente.di.team26.Product.model.TypeOfResource.Material;
import nl.utwente.di.team26.Security.Filters.Secured;
import nl.utwente.di.team26.Security.User.Roles;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Path("/materials")
public class MaterialsResource {

    MaterialsDao materialsDao = new MaterialsDao();

    @GET
//    @Secured(Roles.VISITOR)
    @Produces(MediaType.APPLICATION_JSON)
    public List<Material> getAllMaterials() {
        try (Connection conn = CONSTANTS.getConnection()) {
            return materialsDao.loadAll(conn);
        } catch (NotFoundException | SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    @POST
    @Secured(Roles.ADMIN)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String addNewMaterial(Material materialToAdd) {
        try (Connection conn = CONSTANTS.getConnection()) {
            (new TypeOfResourceDao()).create(conn, materialToAdd);
            return CONSTANTS.SUCCESS;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return CONSTANTS.FAILURE + " " + throwables.getMessage();
        }
    }

}
