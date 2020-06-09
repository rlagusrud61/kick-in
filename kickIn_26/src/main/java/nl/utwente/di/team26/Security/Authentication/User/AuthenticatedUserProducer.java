package nl.utwente.di.team26.Security.Authentication.User;

import nl.utwente.di.team26.CONSTANTS;
import nl.utwente.di.team26.Exceptions.DriverNotInstalledException;
import nl.utwente.di.team26.Exceptions.NotFoundException;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import java.sql.SQLException;

@RequestScoped
public class AuthenticatedUserProducer {

    @Produces
    @RequestScoped
    @AuthenticatedUser
    private User authenticatedUser;

    UserDao userDao = new UserDao();

    public void handleAuthenticationEvent(@Observes @AuthenticatedUser String userId) {
        int user = Integer.parseInt(userId);
        this.authenticatedUser = findUser(user);
    }

    private User findUser(int userId) {
        // Hit the the database or a service to find a user by its username and return it
        // Return the User instance
        User user = null;
        try {
            user = userDao.getObject(CONSTANTS.getConnection(), userId);
        } catch (NotFoundException | SQLException | DriverNotInstalledException e) {
            e.printStackTrace();
        }
        return user;
    }
}
