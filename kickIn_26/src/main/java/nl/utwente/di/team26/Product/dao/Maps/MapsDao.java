package nl.utwente.di.team26.Product.dao.Maps;

import nl.utwente.di.team26.Exceptions.NotFoundException;
import nl.utwente.di.team26.Product.model.Map.Map;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MapsDao {


    /**
     * createValueObject-method. This method is used when the Dao class needs
     * to create new value object instance. The reason why this method exists
     * is that sometimes the programmer may want to extend also the valueObject
     * and then this method can be overrided to return extended valueObject.
     * NOTE: If you extend the valueObject class, make sure to override the
     * clone() method in it!
     */
    public Map createValueObject() {
        return new Map();
    }


    /**
     * getObject-method. This will create and load valueObject contents from database
     * using given Primary-Key as identifier. This method is just a convenience method
     * for the real load-method which accepts the valueObject as a parameter. Returned
     * valueObject will be created using the createValueObject() method.
     */
    public Map getObject(Connection conn, int mapId) throws NotFoundException, SQLException {

        Map valueObject = createValueObject();
        valueObject.setMapId(mapId);
        load(conn, valueObject);
        return valueObject;
    }


    /**
     * load-method. This will load valueObject contents from database using
     * Primary-Key as identifier. Upper layer should use this so that valueObject
     * instance is created and only primary-key should be specified. Then call
     * this method to complete other persistent information. This method will
     * overwrite all other fields except primary-key and possible runtime variables.
     * If load can not find matching row, NotFoundException will be thrown.
     *
     * @param conn        This method requires working database connection.
     * @param valueObject This parameter contains the class instance to be loaded.
     *                    Primary-key field must be set for this to work properly.
     */
    public void load(Connection conn, Map valueObject) throws NotFoundException, SQLException {

        String sql = "SELECT * FROM Maps WHERE (mapId = ? ) ";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, valueObject.getMapId());

            singleQuery(conn, stmt, valueObject);

        }
    }


    /**
     * LoadAll-method. This will read all contents from database table and
     * build a List containing valueObjects. Please note, that this method
     * will consume huge amounts of resources if table has lot's of rows.
     * This should only be used when target tables have only small amounts
     * of data.
     *
     * @param conn This method requires working database connection.
     */
    public List<Map> loadAll(Connection conn) throws SQLException {

        String sql = "SELECT * FROM Maps ORDER BY mapId ASC ";

        return listQuery(conn, conn.prepareStatement(sql));
    }


    /**
     * create-method. This will create new row in database according to supplied
     * valueObject contents. Make sure that values for all NOT NULL columns are
     * correctly specified. Also, if this table does not use automatic surrogate-keys
     * the primary-key must be specified. After INSERT command this method will
     * read the generated primary-key back to valueObject if automatic surrogate-keys
     * were used.
     *
     * @param conn        This method requires working database connection.
     * @param valueObject This parameter contains the class instance to be created.
     *                    If automatic surrogate-keys are not used the Primary-key
     *                    field must be set for this to work properly.
     */
    public synchronized int create(Connection conn, Map valueObject) throws SQLException {

        String sql = "";
        PreparedStatement stmt = null;
        ResultSet result = null;

        try {
            sql = "INSERT INTO Maps (name, description, "
                    + "createdBy, lastEditedBy) VALUES (?, ?, ?, ?) returning mapId";
            stmt = conn.prepareStatement(sql);

            stmt.setString(1, valueObject.getName());
            stmt.setString(2, valueObject.getDescription());
            stmt.setLong(3, valueObject.getCreatedBy());
            stmt.setLong(4, valueObject.getLastEditedBy());
            stmt.execute();

            ResultSet resultSet = stmt.getResultSet();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            } else {
                throw new SQLException("Some error creating a new map.");
            }

        } finally {
            if (stmt != null)
                stmt.close();
        }


    }


    /**
     * save-method. This method will save the current state of valueObject to database.
     * Save can not be used to create new instances in database, so upper layer must
     * make sure that the primary-key is correctly specified. Primary-key will indicate
     * which instance is going to be updated in database. If save can not find matching
     * row, NotFoundException will be thrown.
     *
     * @param conn        This method requires working database connection.
     * @param valueObject This parameter contains the class instance to be saved.
     *                    Primary-key field must be set for this to work properly.
     */
    public void save(Connection conn, Map valueObject)
            throws NotFoundException, SQLException {

        String sql = "UPDATE Maps SET name = ?, description = ?, "
                + "lastEditedBy = ? WHERE (mapId = ? ) ";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, valueObject.getName());
            stmt.setString(2, valueObject.getDescription());
            stmt.setLong(3, valueObject.getLastEditedBy());

            stmt.setLong(4, valueObject.getMapId());

            int rowcount = databaseUpdate(conn, stmt);
            if (rowcount == 0) {
                //System.out.println("Object could not be saved! (PrimaryKey not found)");
                throw new NotFoundException("Object could not be saved! (PrimaryKey not found)");
            }
        }
    }


    /**
     * delete-method. This method will remove the information from database as identified by
     * by primary-key in supplied valueObject. Once valueObject has been deleted it can not
     * be restored by calling save. Restoring can only be done using create method but if
     * database is using automatic surrogate-keys, the resulting object will have different
     * primary-key than what it was in the deleted object. If delete can not find matching row,
     * NotFoundException will be thrown.
     *
     * @param conn        This method requires working database connection.
     * @param valueObject This parameter is the primary key of the resource to be deleted.
     *                    Primary-key field must be set for this to work properly.
     */
    public void delete(Connection conn, Map valueObject)
            throws NotFoundException, SQLException {

        String sql = "DELETE FROM Maps WHERE (mapId = ? ) ";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, valueObject.getMapId());

            int rowcount = databaseUpdate(conn, stmt);
            if (rowcount == 0) {
                //System.out.println("Object could not be deleted (PrimaryKey not found)");
                throw new NotFoundException("Object could not be deleted! (PrimaryKey not found)");
            }
        }
    }


    /**
     * deleteAll-method. This method will remove all information from the table that matches
     * this Dao and ValueObject couple. This should be the most efficient way to clear table.
     * Once deleteAll has been called, no valueObject that has been created before can be
     * restored by calling save. Restoring can only be done using create method but if database
     * is using automatic surrogate-keys, the resulting object will have different primary-key
     * than what it was in the deleted object. (Note, the implementation of this method should
     * be different with different DB backends.)
     *
     * @param conn This method requires working database connection.
     */
    public void deleteAll(Connection conn) throws SQLException {

        String sql = "DELETE FROM Maps";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            int rowcount = databaseUpdate(conn, stmt);
        }
    }


    /**
     * coutAll-method. This method will return the number of all rows from table that matches
     * this Dao. The implementation will simply execute "select count(primarykey) from table".
     * If table is empty, the return value is 0. This method should be used before calling
     * loadAll, to make sure table has not too many rows.
     *
     * @param conn This method requires working database connection.
     */
    public int countAll(Connection conn) throws SQLException {

        String sql = "SELECT count(*) FROM Maps";
        PreparedStatement stmt = null;
        ResultSet result = null;
        int allRows = 0;

        try {
            stmt = conn.prepareStatement(sql);
            result = stmt.executeQuery();

            if (result.next())
                allRows = result.getInt(1);
        } finally {
            if (result != null)
                result.close();
            if (stmt != null)
                stmt.close();
        }
        return allRows;
    }

    public List<Map> getAllMapsFor(Connection conn, int eventId) throws SQLException {

        List<Map> searchResults = null;
        String sql =
                "select m.* from maps m inner join eventmap e on m.mapid = e.mapid where (e.eventid = ?)";

        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setInt(1, eventId);

        return listQuery(conn, preparedStatement);
    }

    /**
     * databaseUpdate-method. This method is a helper method for internal use. It will execute
     * all database handling that will change the information in tables. SELECT queries will
     * not be executed here however. The return value indicates how many rows were affected.
     * This method will also make sure that if cache is used, it will reset when data changes.
     *
     * @param conn This method requires working database connection.
     * @param stmt This parameter contains the SQL statement to be excuted.
     */
    protected int databaseUpdate(Connection conn, PreparedStatement stmt) throws SQLException {
        return stmt.executeUpdate();
    }


    /**
     * databaseQuery-method. This method is a helper method for internal use. It will execute
     * all database queries that will return only one row. The resultset will be converted
     * to valueObject. If no rows were found, NotFoundException will be thrown.
     *
     * @param conn        This method requires working database connection.
     * @param stmt        This parameter contains the SQL statement to be excuted.
     * @param valueObject Class-instance where resulting data will be stored.
     */
    protected void singleQuery(Connection conn, PreparedStatement stmt, Map valueObject)
            throws NotFoundException, SQLException {

        try (ResultSet result = stmt.executeQuery()) {

            if (result.next()) {

                valueObject.setMapId(result.getInt("mapId"));
                valueObject.setName(result.getString("name"));
                valueObject.setDescription(result.getString("description"));
                valueObject.setCreatedBy(result.getLong("createdBy"));
                valueObject.setLastEditedBy(result.getLong("lastEditedBy"));

            } else {
                //System.out.println("Map Object Not Found!");
                throw new NotFoundException("Map Object Not Found!");
            }
        } finally {
            if (stmt != null)
                stmt.close();
        }
    }


    /**
     * databaseQuery-method. This method is a helper method for internal use. It will execute
     * all database queries that will return multiple rows. The resultset will be converted
     * to the List of valueObjects. If no rows were found, an empty List will be returned.
     *
     * @param conn This method requires working database connection.
     * @param stmt This parameter contains the SQL statement to be excuted.
     */
    protected List<Map> listQuery(Connection conn, PreparedStatement stmt) throws SQLException {

        ArrayList<Map> searchResults = new ArrayList<>();

        try (ResultSet result = stmt.executeQuery()) {

            while (result.next()) {
                Map temp = createValueObject();

                temp.setMapId(result.getInt("mapId"));
                temp.setName(result.getString("name"));
                temp.setDescription(result.getString("description"));
                temp.setCreatedBy(result.getLong("createdBy"));
                temp.setLastEditedBy(result.getLong("lastEditedBy"));

                searchResults.add(temp);
            }

        } finally {
            if (stmt != null)
                stmt.close();
        }

        return searchResults;
    }

    public String getAllMaps(Connection conn) throws NotFoundException, SQLException {
        String sql = "SELECT getAllMaps();";
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet resultSet = stmt.executeQuery();
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

    public String getMap(Connection conn, int mapId) throws SQLException, NotFoundException {
        String sql = "SELECT getMap(?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, mapId);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                String result = resultSet.getString(1);
                if (result == null || result.equals("")) {
                    throw new NotFoundException("No Result Returned, no Map of ID: " + mapId + " in the Database");
                } else {
                    return result;
                }
            } else {
                throw new NotFoundException("No Result Returned, no Map of ID: " + mapId + " in the Database");
            }
        }
    }
}
