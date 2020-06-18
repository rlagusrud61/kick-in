package nl.utwente.di.team26.Product.dao;

import nl.utwente.di.team26.Exception.Exceptions.DatabaseException;
import nl.utwente.di.team26.Exception.Exceptions.NotFoundException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class Dao {

    /**
     * Begins a transaction
     * @param connection The connection to modify
     * @param isolationLevel the isolation for the connection
     * @throws SQLException if an sql error occurs.
     */
    public static void beginTransaction(Connection connection, int isolationLevel) throws SQLException {
        connection.setAutoCommit(false);
        connection.setTransactionIsolation(isolationLevel);
    }

    /**
     * Ends a transaction
     * @param connection the connection to which the transaction belongs.
     * @throws SQLException if an sql error occurs.
     */
    public static void endTransaction(Connection connection) throws SQLException {
        connection.commit();
        connection.setAutoCommit(true);
    }

    /**
     * databaseUpdate-method. This method is a helper method for internal use. It will execute
     * all database handling that will change the information in tables. SELECT queries will
     * not be executed here however. The return value indicates how many rows were affected.
     * This method will also make sure that if cache is used, it will reset when data changes.
     *
     * @param stmt This parameter contains the SQL statement to be excuted.
     */
    protected int databaseUpdate(PreparedStatement stmt) throws SQLException {
        return stmt.executeUpdate();
    }

    /**
     * @param stmt the Prepared Statement that needs to be executed for a result.
     * @return The result of a Select query. This is usually a JSON encoded into a string.
     * @throws SQLException When error occurs due to SQL execution
     * @throws NotFoundException When the Select query can not find the needed object.
     */
    protected String getResultOfQuery(Connection conn, PreparedStatement stmt) throws SQLException, NotFoundException {
        try (ResultSet resultSet = stmt.executeQuery()) {
            if (resultSet.next()) {
                String result = resultSet.getString(1);
                endTransaction(conn);
                if (result == null || result.equals("")) {
                    throw new NotFoundException("No Result Returned, the object either does not exist, or the database is empty in the Database");
                } else {
                    return result;
                }
            } else {
                throw new NotFoundException("No Result returned, no Maps");
            }
        } catch (SQLException e) {
            conn.rollback();
            throw new DatabaseException(e);
        }
    }

}
