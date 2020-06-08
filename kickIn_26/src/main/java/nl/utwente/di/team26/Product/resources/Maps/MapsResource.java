package nl.utwente.di.team26.Product.resources.Maps;

import nl.utwente.di.team26.CONSTANTS;
import nl.utwente.di.team26.Exceptions.DriverNotInstalledException;
import nl.utwente.di.team26.Product.dao.Maps.MapsDao;
import nl.utwente.di.team26.Product.model.Map.Map;
import nl.utwente.di.team26.Security.Authentication.Secured;
import nl.utwente.di.team26.Security.Authentication.User.AuthenticatedUser;
import nl.utwente.di.team26.Security.Authentication.User.User;
import nl.utwente.di.team26.Security.Authorization.Role;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Path("/maps")
public class MapsResource {

    @Inject
    @AuthenticatedUser
    User authenticatedUser;

    MapsDao mapsDao = new MapsDao();

    @GET
    @Secured(Role.VISITOR)
    @Produces(MediaType.APPLICATION_JSON)
    public List<Map> getAllMaps() {
        try (Connection conn = CONSTANTS.getConnection()) {
            return mapsDao.loadAll(conn);
        } catch (NotFoundException | SQLException | DriverNotInstalledException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    @POST
    @Secured(Role.EDITOR)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String addNewMap(Map mapToAdd) {
        try (Connection conn = CONSTANTS.getConnection()) {
            mapsDao.create(conn, mapToAdd);
            return CONSTANTS.SUCCESS;
        } catch (SQLException | DriverNotInstalledException throwables) {
            throwables.printStackTrace();
            return CONSTANTS.FAILURE + ": " + throwables.getMessage();
        }
    }

    @DELETE
    @Secured(Role.ADMIN)
    @Produces(MediaType.TEXT_PLAIN)
    public String deleteAllMaps() {
        try (Connection conn = CONSTANTS.getConnection()) {
            mapsDao.deleteAll(conn);
            return CONSTANTS.SUCCESS;
        } catch (SQLException | DriverNotInstalledException e) {
            e.printStackTrace();
            return CONSTANTS.FAILURE + ": " + e.getMessage();
        }
    }

}