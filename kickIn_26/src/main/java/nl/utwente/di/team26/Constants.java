package nl.utwente.di.team26;

import javax.ws.rs.InternalServerErrorException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Constants {

    public static final String loginUrl = "jdbc:postgresql://bronto.ewi.utwente.nl:5432/dab_di19202b_27";
    public static final String loginUser = "dab_di19202b_27";
    public static final String loginPasswd = "e0z19ZX/Hq9Ip5qp";
    public static final String SCHEMA = "kickin26";

    public static final String COOKIENAME = "securityTeam26";
    public static final String SECRET = "securityTeam26IWILLBESECUREENOUGH!!@@";

    public static final int ITERATION = 10000;
    public static final int KEY_LENGTH = 512;

    public static final String USER = "kickInTeam26";
    public static final String EMAIL = "kickInTeam26@gmail.com";
    public static final String PASSWORD = "ihqvjwzhtpnlhcfy";
    public static final String ISSUER = "http://env-di-team26.paas.hosted-by-previder.com/kickInTeam26";
    public static final long TTK = 7200000;
    public static final String AUTH_SCHEME = "JWT-TOKEN";
    public static final String CHROME_DRIVER = "D:\\TCS\\Module 4\\Selenium\\chromedriver.exe";

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
