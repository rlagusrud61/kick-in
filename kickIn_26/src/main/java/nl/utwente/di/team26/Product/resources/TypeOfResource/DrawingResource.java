package nl.utwente.di.team26.Product.resources.TypeOfResource;

import nl.utwente.di.team26.CONSTANTS;
import nl.utwente.di.team26.Exceptions.NotFoundException;
import nl.utwente.di.team26.Product.dao.Resources.ResourceDao;
import nl.utwente.di.team26.Product.model.TypeOfResource.Drawing;
import nl.utwente.di.team26.Security.Filters.Secured;
import nl.utwente.di.team26.Security.User.Roles;
import nl.utwente.di.team26.Utils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.SQLException;

@Path("/resources/drawing")
public class DrawingResource {

    ResourceDao resourceDao = new ResourceDao();

    @GET
    @Secured(Roles.VISITOR)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllDrawings() {
        try (Connection conn = CONSTANTS.getConnection()) {
            String allDrawings = resourceDao.getAllDrawings(conn);
            return Utils.returnOkResponse(allDrawings);
        } catch (NotFoundException e) {
            return Utils.returnNotFoundError(e.getMessage());
        } catch (SQLException e) {
            return Utils.returnInternalServerError(e.getMessage());
        }
    }

    @PUT
    @Path("{drawingId}")
    @Secured(Roles.EDITOR)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateMap(Drawing drawingToSave) {
        try (Connection conn = CONSTANTS.getConnection()) {
            resourceDao.save(conn, drawingToSave);
            return Utils.returnNoContent();
        } catch (NotFoundException e) {
            e.printStackTrace();
            return Utils.returnNotFoundError(e.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
            return Utils.returnInternalServerError(e.getMessage());
        }
    }

    @POST
    @Secured(Roles.ADMIN)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addNewDrawing(Drawing drawingToAdd) {
        try (Connection conn = CONSTANTS.getConnection()) {
            long drawingId = resourceDao.create(conn, drawingToAdd);
            return Utils.returnCreated(drawingId);
        } catch (SQLException e) {
            e.printStackTrace();
            return Utils.returnInternalServerError(e.getMessage());
        }
    }

}
