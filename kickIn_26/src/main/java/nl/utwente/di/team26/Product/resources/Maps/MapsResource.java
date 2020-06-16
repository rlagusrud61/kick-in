package nl.utwente.di.team26.Product.resources.Maps;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import nl.utwente.di.team26.CONSTANTS;
import nl.utwente.di.team26.Product.dao.Maps.MapsDao;
import nl.utwente.di.team26.Product.model.Map.Map;
import nl.utwente.di.team26.Security.Filters.Secured;
import nl.utwente.di.team26.Security.User.Roles;
import nl.utwente.di.team26.Security.User.User;
import nl.utwente.di.team26.Security.User.UserDao;
import nl.utwente.di.team26.Utils;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import java.security.Key;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Path("/maps")
public class MapsResource {

    MapsDao mapsDao = new MapsDao();

    @Context
    HttpHeaders httpHeaders;

    @Context
    SecurityContext securityContext;

    @GET
    @Secured(Roles.VISITOR)
    @Produces(MediaType.APPLICATION_JSON)
    public List<Map> getAllMaps() {
        try (Connection conn = CONSTANTS.getConnection()) {
            return mapsDao.loadAll(conn);
        } catch (NotFoundException | SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    @POST
    @Secured(Roles.EDITOR)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String addNewMap(Map mapToAdd) {

        long userId = Utils.getUserFromContext(securityContext);

        try (Connection conn = CONSTANTS.getConnection()) {

            mapToAdd.setCreatedBy(userId);
            mapToAdd.setLastEditedBy(userId);

            return String.valueOf(mapsDao.create(conn, mapToAdd));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return CONSTANTS.FAILURE + ": " + throwables.getMessage();
        }
    }

    @DELETE
    @Secured(Roles.ADMIN)
    @Produces(MediaType.TEXT_PLAIN)
    public String deleteAllMaps() {
        try (Connection conn = CONSTANTS.getConnection()) {
            mapsDao.deleteAll(conn);
            return CONSTANTS.SUCCESS;
        } catch (SQLException e) {
            e.printStackTrace();
            return CONSTANTS.FAILURE + ": " + e.getMessage();
        }
    }

}