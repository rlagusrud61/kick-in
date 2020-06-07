package nl.utwente.di.team26;

import nl.utwente.di.team26.Exceptions.DriverNotInstalledException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class CONSTANTS {

    public static final String loginUrl = "jdbc:postgresql://bronto.ewi.utwente.nl:5432/dab_di19202b_27";
    public static final String loginUser = "dab_di19202b_27";
    public static final String loginPasswd = "e0z19ZX/Hq9Ip5qp";

    public static final String SUCCESS = "Successful";
    public static final String FAILURE = "Failure";
    public static final String SCHEMA = "idb_kick_in_team_26";

    public static Connection getConnection() throws SQLException, DriverNotInstalledException {

        try {
            Class.forName("org.postgresql.Driver");
            Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
            dbcon.setSchema(SCHEMA);
            return dbcon;
        } catch (ClassNotFoundException e) {
            throw new DriverNotInstalledException("postgresql driver not installed properly.");
        }
    }
}
