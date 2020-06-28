package nl.utwente.di.team26.Product.resources.Users;

import nl.utwente.di.team26.Exception.Exceptions.NotFoundException;
import nl.utwente.di.team26.Product.dao.Authentication.UserDao;
import nl.utwente.di.team26.Product.model.Authentication.User;
import nl.utwente.di.team26.Security.Filters.Secured;
import nl.utwente.di.team26.Security.User.Roles;
import nl.utwente.di.team26.Utils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;

@Path("/users")
public class UsersResource {

    UserDao usersDao = new UserDao();

    @GET
    @Secured(Roles.EDITOR)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUsers() throws NotFoundException, SQLException {
        String allUsers = usersDao.getAll();
        return Utils.returnOkResponse(allUsers);
    }

    @POST
    @Secured(Roles.ADMIN)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addNewUser(User userToAdd) throws SQLException {
        String password = Utils.generatePassayPassword();
        userToAdd.setPassword(Utils.hashPassword(password));
        EmailSender.send(password);
        long userId = usersDao.create(userToAdd);
        return Utils.returnCreated(userId);
    }

    @DELETE
    @Secured(Roles.ADMIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteAllUsers() throws SQLException, NotFoundException {
        usersDao.deleteAll();
        return Utils.returnNoContent();
    }

}
