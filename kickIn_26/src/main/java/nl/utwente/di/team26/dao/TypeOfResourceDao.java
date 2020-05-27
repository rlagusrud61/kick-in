package nl.utwente.di.team26.dao;

import nl.utwente.di.team26.Exceptions.NotFoundException;
import nl.utwente.di.team26.model.Drawing;
import nl.utwente.di.team26.model.Materials;
import nl.utwente.di.team26.model.TypeOfResource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * TypeOfResource Data Access Object (DAO).
 * This class contains all database handling that is needed to
 * permanently store and retrieve TypeOfResource object instances.
 */

public class TypeOfResourceDao {


    /**
     * createValueObject-method. This method is used when the Dao class needs
     * to create new value object instance. The reason why this method exists
     * is that sometimes the programmer may want to extend also the valueObject
     * and then this method can be overrided to return extended valueObject.
     * NOTE: If you extend the valueObject class, make sure to override the
     * clone() method in it!
     */
    public TypeOfResource createValueObject() {
        return new TypeOfResource();
    }


    /**
     * getObject-method. This will create and load valueObject contents from database
     * using given Primary-Key as identifier. This method is just a convenience method
     * for the real load-method which accepts the valueObject as a parameter. Returned
     * valueObject will be created using the createValueObject() method.
     */
    public TypeOfResource getObject(Connection conn, int resourceId) throws NotFoundException, SQLException {

        TypeOfResource valueObject = createValueObject();
        valueObject.setResourceId(resourceId);
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
    public void load(Connection conn, TypeOfResource valueObject) throws NotFoundException, SQLException {

        String sql = "SELECT * FROM TypeOfResource WHERE (resourceId = ? ) ";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, valueObject.getResourceId());

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
    public List<TypeOfResource> loadAll(Connection conn) throws SQLException {

        String sql = "SELECT * FROM TypeOfResource ORDER BY resourceId ASC ";

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
    public synchronized void create(Connection conn, TypeOfResource valueObject) throws SQLException {

        String sql = "";
        PreparedStatement stmt = null;
        ResultSet result = null;
        String subClass = "";
        String image = "";

        if (valueObject instanceof Drawing) {
            subClass = "Drawing";
            image = ((Drawing) valueObject).getImage();
        } else if (valueObject instanceof Materials) {
            subClass = "Materials";
            image = ((Materials) valueObject).getImage();
        }
        try {
            sql = "WITH super AS (" +
                    "INSERT INTO TypeOfResource (name, description) " +
                    "VALUES (?, ?) " +
                    "RETURNING resourceId" +
                    ") " +
                    "INSERT INTO ? (resourceId, image) " +
                    "SELECT super.resourceId, ? " +
                    "FROM super ";
            stmt = conn.prepareStatement(sql);

            stmt.setString(1, valueObject.getName());
            stmt.setString(2, valueObject.getDescription());
            stmt.setString(3, subClass);
            stmt.setString(4, image);

            int rowcount = databaseUpdate(conn, stmt);
            if (rowcount != 1) {
                //System.out.println("PrimaryKey Error when updating DB!");
                throw new SQLException("PrimaryKey Error when updating DB!");
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
    public void save(Connection conn, TypeOfResource valueObject)
            throws NotFoundException, SQLException {

        String sql = "UPDATE TypeOfResource SET name = ?, description = ? WHERE (resourceId = ? ) ";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, valueObject.getName());
            stmt.setString(2, valueObject.getDescription());

            stmt.setInt(3, valueObject.getResourceId());

            int rowcount = databaseUpdate(conn, stmt);
            if (rowcount == 0) {
                //System.out.println("Object could not be saved! (PrimaryKey not found)");
                throw new NotFoundException("Object could not be saved! (PrimaryKey not found)");
            }
            if (rowcount > 1) {
                //System.out.println("PrimaryKey Error when updating DB! (Many objects were affected!)");
                throw new SQLException("PrimaryKey Error when updating DB! (Many objects were affected!)");
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
     * @param valueObject This parameter contains the class instance to be deleted.
     *                    Primary-key field must be set for this to work properly.
     */
    public void delete(Connection conn, TypeOfResource valueObject)
            throws NotFoundException, SQLException {

        String sql = "DELETE FROM TypeOfResource WHERE (resourceId = ? ) ";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, valueObject.getResourceId());

            int rowcount = databaseUpdate(conn, stmt);
            if (rowcount == 0) {
                //System.out.println("Object could not be deleted (PrimaryKey not found)");
                throw new NotFoundException("Object could not be deleted! (PrimaryKey not found)");
            }
            if (rowcount > 1) {
                //System.out.println("PrimaryKey Error when updating DB! (Many objects were deleted!)");
                throw new SQLException("PrimaryKey Error when updating DB! (Many objects were deleted!)");
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

        String sql = "DELETE FROM TypeOfResource";

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

        String sql = "SELECT count(*) FROM TypeOfResource";
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


    /**
     * searchMatching-Method. This method provides searching capability to
     * get matching valueObjects from database. It works by searching all
     * objects that match permanent instance variables of given object.
     * Upper layer should use this by setting some parameters in valueObject
     * and then  call searchMatching. The result will be 0-N objects in a List,
     * all matching those criteria you specified. Those instance-variables that
     * have NULL values are excluded in search-criteria.
     *
     * @param conn        This method requires working database connection.
     * @param valueObject This parameter contains the class instance where search will be based.
     *                    Primary-key field should not be set.
     */
    public List<TypeOfResource> searchMatching(Connection conn, TypeOfResource valueObject) throws SQLException {

        List<TypeOfResource> searchResults;

        boolean first = true;
        StringBuilder sql = new StringBuilder("SELECT * FROM TypeOfResource WHERE 1=1 ");

        if (valueObject.getResourceId() != 0) {
            first = false;
            sql.append("AND resourceId = ").append(valueObject.getResourceId()).append(" ");
        }

        if (valueObject.getName() != null) {
            if (first) {
                first = false;
            }
            sql.append("AND name LIKE '").append(valueObject.getName()).append("%' ");
        }

        if (valueObject.getDescription() != null) {
            if (first) {
                first = false;
            }
            sql.append("AND description LIKE '").append(valueObject.getDescription()).append("%' ");
        }


        sql.append("ORDER BY resourceId ASC ");

        // Prevent accidential full table results.
        // Use loadAll if all rows must be returned.
        if (first)
            searchResults = new ArrayList<TypeOfResource>();
        else
            searchResults = listQuery(conn, conn.prepareStatement(sql.toString()));

        return searchResults;
    }


    /**
     * getDaogenVersion will return information about
     * generator which created these sources.
     */
    public String getDaogenVersion() {
        return "DaoGen version 2.4.1";
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
    protected void singleQuery(Connection conn, PreparedStatement stmt, TypeOfResource valueObject)
            throws NotFoundException, SQLException {

        try (ResultSet result = stmt.executeQuery()) {

            if (result.next()) {

                valueObject.setResourceId(result.getInt("resourceId"));
                valueObject.setName(result.getString("name"));
                valueObject.setDescription(result.getString("description"));

            } else {
                //System.out.println("TypeOfResource Object Not Found!");
                throw new NotFoundException("TypeOfResource Object Not Found!");
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
    protected List<TypeOfResource> listQuery(Connection conn, PreparedStatement stmt) throws SQLException {

        ArrayList<TypeOfResource> searchResults = new ArrayList<TypeOfResource>();

        try (ResultSet result = stmt.executeQuery()) {

            while (result.next()) {
                TypeOfResource temp = createValueObject();

                temp.setResourceId(result.getInt("resourceId"));
                temp.setName(result.getString("name"));
                temp.setDescription(result.getString("description"));

                searchResults.add(temp);
            }

        } finally {
            if (stmt != null)
                stmt.close();
        }

        return searchResults;
    }
}
