package nl.utwente.di.team26.Security.User;

import nl.utwente.di.team26.Exceptions.AuthenticationDeniedException;
import nl.utwente.di.team26.Exceptions.NotFoundException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


    /**
     * User Data Access Object (DAO).
     * This class contains all database handling that is needed to
     * permanently store and retrieve User object instances.
     */

    public class UserDao {



        /**
         * createValueObject-method. This method is used when the Dao class needs
         * to create new value object instance. The reason why this method exists
         * is that sometimes the programmer may want to extend also the valueObject
         * and then this method can be overrided to return extended valueObject.
         * NOTE: If you extend the valueObject class, make sure to override the
         * clone() method in it!
         */
        public User createValueObject() {
            return new User();
        }


        /**
         * getObject-method. This will create and load valueObject contents from database
         * using given Primary-Key as identifier. This method is just a convenience method
         * for the real load-method which accepts the valueObject as a parameter. Returned
         * valueObject will be created using the createValueObject() method.
         */
        public User getObject(Connection conn, long userId) throws SQLException, AuthenticationDeniedException {

            User valueObject = createValueObject();
            valueObject.setUserId(userId);
            load(conn, valueObject);
            return valueObject;
        }

        public User authenticateUser(Connection conn, Credentials credentials) throws AuthenticationDeniedException, SQLException {
            String sql =
                    "SELECT * FROM users WHERE email = ? AND password = ?";
            User valueObject = new User();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, credentials.getEmail());
                stmt.setString(2, credentials.getPassword());
                singleQuery(conn, stmt, valueObject);
                return valueObject;
            } catch (SQLException e) {
                e.printStackTrace();
                throw new AuthenticationDeniedException("SQL Error");
            } catch (NotFoundException e) {
                throw new AuthenticationDeniedException("No!");
            }

        }


        /**
         * load-method. This will load valueObject contents from database using
         * Primary-Key as identifier. Upper layer should use this so that valueObject
         * instance is created and only primary-key should be specified. Then call
         * this method to complete other persistent information. This method will
         * overwrite all other fields except primary-key and possible runtime variables.
         * If load can not find matching row, NotFoundException will be thrown.
         *
         * @param conn         This method requires working database connection.
         * @param valueObject  This parameter contains the class instance to be loaded.
         *                     Primary-key field must be set for this to work properly.
         */
        public void load(Connection conn, User valueObject) throws SQLException, AuthenticationDeniedException {

            String sql = "SELECT * FROM users WHERE (userId = ? ) ";
            PreparedStatement stmt = null;
            try {
                stmt = conn.prepareStatement(sql);
                stmt.setLong(1, valueObject.getUserId());
                singleQuery(conn, stmt, valueObject);
            } catch (NotFoundException e) {
                throw new AuthenticationDeniedException("user not existent");
            } finally {
                if (stmt != null) {
                    stmt.close();
                }
            }
        }


        /**
         * LoadAll-method. This will read all contents from database table and
         * build a List containing valueObjects. Please note, that this method
         * will consume huge amounts of resources if table has lot's of rows.
         * This should only be used when target tables have only small amounts
         * of data.
         *
         * @param conn         This method requires working database connection.
         */
        public List<User> loadAll(Connection conn) throws SQLException {

            String sql = "SELECT * FROM Users ORDER BY userId ASC ";

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
         * @param conn         This method requires working database connection.
         * @param valueObject  This parameter contains the class instance to be created.
         *                     If automatic surrogate-keys are not used the Primary-key
         *                     field must be set for this to work properly.
         */
        public synchronized void create(Connection conn, User valueObject) throws SQLException {

            String sql = "";
            PreparedStatement stmt = null;
            ResultSet result = null;

            try {
                sql = "INSERT INTO users (email, password, "
                        + "clearanceLevel) VALUES (?, ?, ?) ";
                stmt = conn.prepareStatement(sql);

                stmt.setString(1, valueObject.getEmail());
                stmt.setString(2, valueObject.getPassword());
                stmt.setInt(3, valueObject.getclearanceLevel());

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
         * @param conn         This method requires working database connection.
         * @param valueObject  This parameter contains the class instance to be saved.
         *                     Primary-key field must be set for this to work properly.
         */
        public void save(Connection conn, User valueObject)
                throws NotFoundException, SQLException {

            String sql = "UPDATE users SET email = ?, password = ?, clearanceLevel = ? WHERE (userId = ? ) ";

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, valueObject.getEmail());
                stmt.setString(2, valueObject.getPassword());
                stmt.setInt(3, valueObject.getclearanceLevel());

                stmt.setLong(4, valueObject.getUserId());

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
         * @param conn         This method requires working database connection.
         * @param valueObject  This parameter contains the class instance to be deleted.
         *                     Primary-key field must be set for this to work properly.
         */
        public void delete(Connection conn, User valueObject)
                throws NotFoundException, SQLException {

            String sql = "DELETE FROM users WHERE (userId = ? ) ";

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, valueObject.getUserId());

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
         * @param conn         This method requires working database connection.
         */
        public void deleteAll(Connection conn) throws SQLException {

            String sql = "DELETE FROM users";

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
         * @param conn         This method requires working database connection.
         */
        public int countAll(Connection conn) throws SQLException {

            String sql = "SELECT count(*) FROM users";
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
         * databaseUpdate-method. This method is a helper method for internal use. It will execute
         * all database handling that will change the information in tables. SELECT queries will
         * not be executed here however. The return value indicates how many rows were affected.
         * This method will also make sure that if cache is used, it will reset when data changes.
         *
         * @param conn         This method requires working database connection.
         * @param stmt         This parameter contains the SQL statement to be excuted.
         */
        protected int databaseUpdate(Connection conn, PreparedStatement stmt) throws SQLException {

            return stmt.executeUpdate();
        }



        /**
         * databaseQuery-method. This method is a helper method for internal use. It will execute
         * all database queries that will return only one row. The resultset will be converted
         * to valueObject. If no rows were found, NotFoundException will be thrown.
         *
         * @param conn         This method requires working database connection.
         * @param stmt         This parameter contains the SQL statement to be excuted.
         * @param valueObject  Class-instance where resulting data will be stored.
         */
        protected void singleQuery(Connection conn, PreparedStatement stmt, User valueObject)
                throws NotFoundException, SQLException {

            try (ResultSet result = stmt.executeQuery()) {

                if (result.next()) {

                    valueObject.setUserId(result.getInt("userId"));
                    valueObject.setEmail(result.getString("email"));
                    valueObject.setPassword(result.getString("password"));
                    valueObject.setclearanceLevel(result.getInt("clearanceLevel"));

                } else {
                    //System.out.println("User Object Not Found!");
                    throw new NotFoundException("User Object Not Found!");
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
         * @param conn         This method requires working database connection.
         * @param stmt         This parameter contains the SQL statement to be excuted.
         */
        protected List<User> listQuery(Connection conn, PreparedStatement stmt) throws SQLException {

            ArrayList<User> searchResults = new ArrayList<>();

            try (ResultSet result = stmt.executeQuery()) {

                while (result.next()) {
                    User temp = createValueObject();

                    temp.setUserId(result.getInt("userId"));
                    temp.setEmail(result.getString("email"));
                    temp.setPassword(result.getString("password"));
                    temp.setclearanceLevel(result.getInt("clearanceLevel"));

                    searchResults.add(temp);
                }

            } finally {
                if (stmt != null)
                    stmt.close();
            }

            return searchResults;
        }
    }
