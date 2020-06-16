package nl.utwente.di.team26;

import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.core.SecurityContext;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class CONSTANTS {

    public static final String loginUrl = "jdbc:postgresql://bronto.ewi.utwente.nl:5432/dab_di19202b_27";
    public static final String loginUser = "dab_di19202b_27";
    public static final String loginPasswd = "e0z19ZX/Hq9Ip5qp";
    public static final String SCHEMA = "idb_kick_in_team_26";

    public static final String COOKIENAME = "securityTeam26";
    public static final String SECRET = "securityTeam26IWILLBESECUREENOUGH!!@@";

    public static final String SUCCESS = "Successful";
    public static final String FAILURE = "Failure";
    public static final String ISSUER = "http://localhost:8080/kickInTeam26/";
    public static final long TTK = 7200000;
    public static final String AUTH_SCHEME = "JWT-TOKEN";

    public static Connection getConnection() throws InternalServerErrorException {

        try {
            Class.forName("org.postgresql.Driver");
            Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
            dbcon.setSchema(SCHEMA);
            return dbcon;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            throw new InternalServerErrorException("SQL failed");
        }
    }

}
