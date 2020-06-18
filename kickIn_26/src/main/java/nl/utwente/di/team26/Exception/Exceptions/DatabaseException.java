package nl.utwente.di.team26.Exception.Exceptions;

import java.sql.SQLException;

public class DatabaseException extends SQLException {

    public DatabaseException(SQLException e) {
        super(e);
    }

    public DatabaseException(String msg) {
        super(msg);
    }

}
