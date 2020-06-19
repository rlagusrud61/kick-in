package nl.utwente.di.team26.Product.resources.TypeOfResource;

import nl.utwente.di.team26.CONSTANTS;
import nl.utwente.di.team26.Exception.Exceptions.NotFoundException;
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
    public Response getAllMaterials() throws NotFoundException, SQLException {

            String allMaterials = resourceDao.getAllMaterials();
            return Utils.returnOkResponse(allMaterials);
    }

    @PUT
    @Path("{materialId}")
    @Secured(Roles.EDITOR)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateMap(Material materialToSave) throws NotFoundException, SQLException {

            resourceDao.save(materialToSave);
            return Utils.returnNoContent();
    }

    @POST
    @Secured(Roles.ADMIN)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addNewMaterial(Material materialToAdd) throws SQLException {

            long drawingId = resourceDao.create(materialToAdd);
            return Utils.returnCreated(drawingId);
    }

}

