package nl.utwente.di.team26.Security.Authorization;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import nl.utwente.di.team26.CONSTANTS;
import nl.utwente.di.team26.Exceptions.NotFoundException;
import nl.utwente.di.team26.Security.Authentication.Secured;
import nl.utwente.di.team26.Security.Authentication.User.User;
import nl.utwente.di.team26.Security.Authentication.User.UserDao;

import javax.annotation.Priority;
import javax.crypto.spec.SecretKeySpec;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.security.Key;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Secured
@Provider
@Priority(Priorities.AUTHORIZATION)
public class AuthorizationFilter implements ContainerRequestFilter {

    @Context
    private ResourceInfo resourceInfo;

    UserDao userDao = new UserDao();

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        System.out.println("Checking Token");
        String jwtToken = requestContext.getCookies().get(CONSTANTS.COOKIENAME).getValue();
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        Key signingKey = new SecretKeySpec(CONSTANTS.SECRET.getBytes(), signatureAlgorithm.getJcaName());

        int userId = Integer.parseInt(
                Jwts.parserBuilder()
                        .setSigningKey(signingKey)
                        .build()
                        .parseClaimsJws(jwtToken)
                        .getBody()
                        .getSubject());

        User authenticatedUser = findUser(userId);

        // Get the resource class which matches with the requested URL
        // Extract the roles declared by it
        Class<?> resourceClass = resourceInfo.getResourceClass();
        List<Role> classRoles = extractRoles(resourceClass);

        // Get the resource method which matches with the requested URL
        // Extract the roles declared by it
        Method resourceMethod = resourceInfo.getResourceMethod();
        List<Role> methodRoles = extractRoles(resourceMethod);

        try {

            // Check if the user is allowed to execute the method
            // The method annotations override the class annotations
            if (methodRoles.isEmpty()) {
                checkPermissions(classRoles, authenticatedUser);
            } else {
                checkPermissions(methodRoles, authenticatedUser);
            }

        } catch (NotAuthorizedException e) {
            requestContext.abortWith(
                    Response.status(Response.Status.FORBIDDEN).build());
        }
    }

    // Extract the roles from the annotated element
    private List<Role> extractRoles(AnnotatedElement annotatedElement) {
        if (annotatedElement == null) {
            return new ArrayList<>();
        } else {
            Secured secured = annotatedElement.getAnnotation(Secured.class);
            if (secured == null) {
                return new ArrayList<>();
            } else {
                Role[] allowedRoles = secured.value();
                return Arrays.asList(allowedRoles);
            }
        }
    }

    private void checkPermissions(List<Role> allowedRoles, User authenticatedUser) throws NotAuthorizedException {
        // Check if the user contains one of the allowed roles
        // Throw an Exception if the user has not permission to execute the method

        if (authenticatedUser.getClarificationLevel() < allowedRoles.get(0).ordinal()) {
            throw new NotAuthorizedException("You shall not pass!");
        }
    }

    private User findUser(int userId) {
        // Hit the the database or a service to find a user by its username and return it
        // Return the User instance
        User user = null;
        try {
            user = userDao.getObject(CONSTANTS.getConnection(), userId);
        } catch (NotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

}