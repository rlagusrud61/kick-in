package nl.utwente.di.team26.Product.dao;

import nl.utwente.di.team26.Exceptions.NotFoundException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class Dao {

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
    protected String getResultOfQuery(PreparedStatement stmt) throws SQLException, NotFoundException {
        try (ResultSet resultSet = stmt.executeQuery()) {
            if (resultSet.next()) {
                String result = resultSet.getString(1);
                if (result == null || result.equals("")) {
                    throw new NotFoundException("No Result Returned, no Maps in the Database");
                } else {
                    return result;
                }
            } else {
                throw new NotFoundException("No Result returned, no Maps");
            }
        }
    }

}
