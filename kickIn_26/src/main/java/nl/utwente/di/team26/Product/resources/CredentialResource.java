package nl.utwente.di.team26.Product.resources;

import nl.utwente.di.team26.Exception.Exceptions.NotFoundException;
import nl.utwente.di.team26.Product.dao.Authentication.UserDao;
import nl.utwente.di.team26.Product.model.Authentication.User;
import nl.utwente.di.team26.Security.EmailSender.EmailSender;
import nl.utwente.di.team26.Security.Filters.Secured;
import nl.utwente.di.team26.Security.User.Roles;
import nl.utwente.di.team26.Utils;

import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import java.sql.SQLException;

@Path("/credential/reset/{userId}")
public class CredentialResource {

    private final UserDao userDao = new UserDao();
    private final EmailSender emailSender = new EmailSender();

    @PUT
    @Secured(Roles.ADMIN)
    public Response generateNewCredentials(@PathParam("userId") long userId) throws NotFoundException, SQLException {
        String newPassword = Utils.generatePassayPassword();
        User userToEdit = Utils.findUser(userId);
        userDao.resetPassword(userId, Utils.hashPassword(newPassword));
        emailSender.sendMail(newPassword, userToEdit);
        return Utils.returnNoContent();
    }

}
