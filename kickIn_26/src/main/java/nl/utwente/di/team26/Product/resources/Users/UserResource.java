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

@Path("/user/{userId}")
public class UserResource {

    UserDao usersDao = new UserDao();

    @GET
    @Secured(Roles.ADMIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserById(@PathParam("userId") long userId) throws NotFoundException, SQLException {
        String mapData = usersDao.get(userId);
        return Utils.returnOkResponse(mapData);
    }

    @PUT
    @Secured(Roles.ADMIN)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUser(User userToUpdate) throws NotFoundException, SQLException {
        userToUpdate.setPassword(Utils.hashPassword(userToUpdate.getPassword()));
        usersDao.save(userToUpdate);
        return Utils.returnNoContent();
    }

    @DELETE
    @Secured(Roles.ADMIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUser(@PathParam("userId") long userId) throws NotFoundException, SQLException {
        usersDao.delete(new User(userId));
        return Utils.returnNoContent();
    }

}
