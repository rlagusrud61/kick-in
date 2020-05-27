package nl.utwente.di.team26;

import nl.utwente.di.team26.Exceptions.DataSourceNotFoundException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class CONSTANTS {

    public static final String SUCCESS = "Successful";
    public static final String FAILURE = "Failure";
    public static final String SCHEMA = "idb_kick_in_team_26";

    public static Connection getConnection() throws DataSourceNotFoundException, NamingException, SQLException {

        Context ctx = null;
        Connection con = null;
        ctx = new InitialContext();

        DataSource ds = (DataSource) ctx.lookup("java:/comp/env/jdbc/kickInTeam26");

        if (ds == null) {
            throw new DataSourceNotFoundException("No data source found");
        }

        con = ds.getConnection();
        con.setSchema(CONSTANTS.SCHEMA);
        return con;
    }
}
