package nl.utwente.di.team26.Product.resources.TypeOfResource;

import nl.utwente.di.team26.CONSTANTS;
import nl.utwente.di.team26.Exceptions.NotFoundException;
import nl.utwente.di.team26.Product.dao.Resources.ResourceDao;
import nl.utwente.di.team26.Product.model.TypeOfResource.Material;
import nl.utwente.di.team26.Security.Filters.Secured;
import nl.utwente.di.team26.Security.User.Roles;
import nl.utwente.di.team26.Utils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.SQLException;

@Path("/resources/material")
public class MaterialResource {

    ResourceDao resourceDao = new ResourceDao();

    @GET
    @Secured(Roles.VISITOR)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllMaterials() {
        try (Connection conn = CONSTANTS.getConnection()) {
            String allMaterials = resourceDao.getAllMaterials(conn);
            return Utils.returnOkResponse(allMaterials);
        } catch (NotFoundException e) {
            return Utils.returnNotFoundError(e.getMessage());
        } catch (SQLException e) {
            return Utils.returnInternalServerError(e.getMessage());
        }
    }

    @PUT
    @Path("{materialId}")
    @Secured(Roles.EDITOR)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateMap(Material materialToSave) {
        try (Connection conn = CONSTANTS.getConnection()) {
            resourceDao.save(conn, materialToSave);
            return Utils.returnNoContent();
        } catch (NotFoundException e) {
            return Utils.returnNotFoundError(e.getMessage());
        } catch (SQLException e) {
            return Utils.returnInternalServerError(e.getMessage());
        }
    }

    @POST
    @Secured(Roles.ADMIN)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addNewMaterial(Material materialToAdd) {
        try (Connection conn = CONSTANTS.getConnection()) {
            long drawingId = resourceDao.create(conn, materialToAdd);
            return Utils.returnCreated(drawingId);
        } catch (SQLException e) {
            return Utils.returnInternalServerError(e.getMessage());
        }
    }

}

