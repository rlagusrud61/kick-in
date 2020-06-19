package nl.utwente.di.team26.Product.resources.Users;

import nl.utwente.di.team26.CONSTANTS;
import nl.utwente.di.team26.Exception.Exceptions.NotFoundException;
import nl.utwente.di.team26.Product.dao.Authentication.UserDao;
import nl.utwente.di.team26.Product.model.Authentication.User;
import nl.utwente.di.team26.Security.Filters.Secured;
import nl.utwente.di.team26.Security.User.Roles;
import nl.utwente.di.team26.Utils;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.sql.Connection;
import java.sql.SQLException;

@Path("/users")
public class UsersResource {

    UserDao usersDao = new UserDao();

    @GET
    @Secured(Roles.EDITOR)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUsers() throws NotFoundException, SQLException {
        try (Connection conn = CONSTANTS.getConnection()) {
            String allUsers = usersDao.getAllUsers(conn);
            return Utils.returnOkResponse(allUsers);
        }
    }

    @POST
    @Secured(Roles.ADMIN)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addNewUser(User userToAdd) throws SQLException {

        try (Connection conn = CONSTANTS.getConnection()) {
            long userId = usersDao.create(conn, userToAdd);
            return Utils.returnCreated(userId);
        }
    }

    @DELETE
    @Secured(Roles.ADMIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteAllUsers() throws SQLException {
        try (Connection conn = CONSTANTS.getConnection()) {
            usersDao.deleteAll(conn);
            return Utils.returnNoContent();
        }
    }

}
