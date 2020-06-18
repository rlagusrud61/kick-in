package nl.utwente.di.team26.Exception.Util;

import nl.utwente.di.team26.Exception.Exceptions.AuthenticationDeniedException;
import nl.utwente.di.team26.Exception.Exceptions.NotFoundException;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.sql.SQLException;

@Provider
public class CustomExceptionMapper implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable e) {
        String errorString;
        Status errorCode;
        if (e instanceof AuthenticationDeniedException) {
            errorCode = Response.Status.FORBIDDEN;
            errorString = e.getMessage();
        } else if (e instanceof NotFoundException) {
            errorCode = Response.Status.NOT_FOUND;
            errorString = e.getMessage();
        } else if (e instanceof SQLException) {
            errorCode = Status.BAD_REQUEST;
            errorString = e.getMessage();
        } else {
            errorCode = Response.Status.INTERNAL_SERVER_ERROR;
            errorString = e.getMessage();
        }

        ErrorMessage errorMessage = new ErrorMessage(errorCode, errorString);
        return Response.status(errorCode).entity(errorMessage).build();
    }

}
