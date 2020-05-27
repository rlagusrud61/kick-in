package nl.utwente.di.team26.resources.Maps;

import nl.utwente.di.team26.CONSTANTS;
import nl.utwente.di.team26.Exceptions.DataSourceNotFoundException;
import nl.utwente.di.team26.dao.MapsDao;
import nl.utwente.di.team26.model.Maps;

import javax.naming.NamingException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Path("/maps")
public class MapsResource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Maps> getAllMaps() {
        try (Connection conn = CONSTANTS.getConnection()) {
            return (new MapsDao()).loadAll(conn);
        } catch (NotFoundException | SQLException | DataSourceNotFoundException | NamingException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String addNewMap(Maps mapToAdd) {
        try (Connection conn = CONSTANTS.getConnection()) {
            (new MapsDao()).create(conn, mapToAdd);
            return CONSTANTS.SUCCESS;
        } catch (SQLException | DataSourceNotFoundException | NamingException throwables) {
            throwables.printStackTrace();
            return CONSTANTS.FAILURE;
        }
    }
}