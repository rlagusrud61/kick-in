package nl.utwente.di.team26.resources.Maps;

import nl.utwente.di.team26.CONSTANTS;
import nl.utwente.di.team26.dao.MapsDao;
import nl.utwente.di.team26.model.Maps;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

@Path("/maps")
public class MapsResource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Maps> getAllMaps() {
        try (Connection conn = DriverManager.getConnection(
                CONSTANTS.URL,
                CONSTANTS.USER,
                CONSTANTS.PASSWORD)) {
            return (new MapsDao()).loadAll(conn);
        } catch (NotFoundException | SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String addNewMap(Maps mapToAdd) {
        try (Connection conn = DriverManager.getConnection(
                CONSTANTS.URL,
                CONSTANTS.USER,
                CONSTANTS.PASSWORD)) {
            (new MapsDao()).create(conn, mapToAdd);
            return CONSTANTS.SUCCESS;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return CONSTANTS.FAILURE;
        }
    }
}