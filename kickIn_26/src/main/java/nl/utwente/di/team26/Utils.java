package nl.utwente.di.team26;

import nl.utwente.di.team26.Exceptions.NotFoundException;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Utils {

    public static long getUserFromContext(SecurityContext securityContext) {
        return Long.parseLong(securityContext.getUserPrincipal().getName());
    }

    public static Response returnInternalServerError(String message) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(message).build();
    }

    public static Response returnNotFoundError(String message) {
        return Response.status(Response.Status.NOT_FOUND).entity(message).build();
    }

    public static Response returnOkResponse(String message) {
        return Response.ok(message).build();
    }

    public static Response returnNoContent() {
        return Response.noContent().build();
    }

    public static Response returnCreated(long oid) {
        return Response.status(Response.Status.CREATED).entity(oid).build();
    }

    public static Response returnCreated() {
        return Response.status(Response.Status.CREATED).build();
    }

}
