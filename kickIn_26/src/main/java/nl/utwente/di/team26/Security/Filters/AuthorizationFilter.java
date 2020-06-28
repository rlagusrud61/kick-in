package nl.utwente.di.team26.Security.Filters;

import nl.utwente.di.team26.Exception.Util.ErrorMessage;
import nl.utwente.di.team26.Product.dao.Authentication.UserDao;
import nl.utwente.di.team26.Security.User.Roles;

import javax.annotation.Priority;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Secured
@Provider
@Priority(Priorities.AUTHORIZATION)
public class AuthorizationFilter implements ContainerRequestFilter {

    @Context
    private ResourceInfo resourceInfo;

    @Context
    private SecurityContext securityContext;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        // Get the resource class which matches with the requested URL
        // Extract the roles declared by it
        Class<?> resourceClass = resourceInfo.getResourceClass();
        List<Roles> classRoles = extractRoles(resourceClass);

        // Get the resource method which matches with the requested URL
        // Extract the roles declared by it
        Method resourceMethod = resourceInfo.getResourceMethod();
        List<Roles> methodRoles = extractRoles(resourceMethod);

        try {

            // Check if the user is allowed to execute the method
            // The method annotations override the class annotations
            if (methodRoles.isEmpty()) {
                checkPermissions(classRoles);
            } else {
                checkPermissions(methodRoles);
            }

        } catch (NotAuthorizedException e) {
            requestContext.abortWith(Response
                            .status(Response.Status.FORBIDDEN)
                            .entity(new ErrorMessage(Response.Status.FORBIDDEN, e.getMessage()))
                            .build()
            );
        }
    }

    // Extract the roles from the annotated element
    private List<Roles> extractRoles(AnnotatedElement annotatedElement) {
        if (annotatedElement == null) {
            return new ArrayList<>();
        } else {
            Secured secured = annotatedElement.getAnnotation(Secured.class);
            if (secured == null) {
                return new ArrayList<>();
            } else {
                Roles[] allowedRoles = secured.value();
                return Arrays.asList(allowedRoles);
            }
        }
    }

    private void checkPermissions(List<Roles> allowedRoles) throws NotAuthorizedException {
        // Check if the user contains one of the allowed roles
        // Throw an Exception if the user has not permission to execute the method

        if (allowedRoles.isEmpty()) {
            return;
        }

        if (!securityContext.isUserInRole(allowedRoles.get(0).getString())) {
            throw new NotAuthorizedException("You shall not pass!");
        }

    }

}