package nl.utwente.di.team26.Product.dao.Authentication;

import nl.utwente.di.team26.Constants;
import nl.utwente.di.team26.Exception.Exceptions.AuthenticationDeniedException;
import nl.utwente.di.team26.Exception.Exceptions.DatabaseException;
import nl.utwente.di.team26.Exception.Exceptions.NotFoundException;
import nl.utwente.di.team26.Product.dao.Dao;
import nl.utwente.di.team26.Product.dao.DaoInterface;
import nl.utwente.di.team26.Product.model.Authentication.Credentials;
import nl.utwente.di.team26.Product.model.Authentication.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao extends Dao implements DaoInterface<User> {

    @Override
    public long create(User valueObject) throws SQLException {
        try(Connection conn = Constants.getConnection()) {
            String sql = "INSERT INTO users (email, password, nickname, "
                    + "clearanceLevel) VALUES (?, ?, ?, ?) RETURNING userId";
            beginTransaction(conn, Connection.TRANSACTION_READ_COMMITTED);

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, valueObject.getEmail());
                stmt.setString(2, valueObject.getPassword());
                stmt.setString(3, valueObject.getNickname());
                stmt.setInt(4, valueObject.getClearanceLevel());
                return executeCreate(conn, stmt);
            }
        }
    }

    @Override
    public void save(User user) throws NotFoundException, SQLException {
        try(Connection conn = Constants.getConnection()) {
            String sql = "UPDATE Users SET email = ?, password = ?, nickname = ?, clearanceLevel = ? WHERE (userId = ?) ";
            beginTransaction(conn, Connection.TRANSACTION_READ_COMMITTED);
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, user.getEmail());
                stmt.setString(2, user.getPassword());
                stmt.setString(3, user.getNickname());
                stmt.setInt(4, user.getClearanceLevel());

                stmt.setLong(5, user.getUserId());
                databaseUpdate(conn, stmt);
            }
        }
    }

    @Override
    public void delete(User user) throws NotFoundException, SQLException {
        try(Connection conn = Constants.getConnection()) {
            String sql = "DELETE FROM Users WHERE (userId = ? )";
            beginTransaction(conn, Connection.TRANSACTION_READ_COMMITTED);
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, user.getUserId());
                databaseUpdate(conn, stmt);
            }
        }
    }

    @Override
    public void deleteAll() throws SQLException, NotFoundException {
        try(Connection conn = Constants.getConnection()) {
            String sql = "DELETE FROM Users";
            beginTransaction(conn, Connection.TRANSACTION_READ_COMMITTED);
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                databaseUpdate(conn, stmt);
            }
        }
    }

    public String getAll() throws NotFoundException, SQLException {
        try(Connection conn = Constants.getConnection()) {
            String sql = "SELECT getAllUsers();";
            beginTransaction(conn, Connection.TRANSACTION_SERIALIZABLE);
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                return getResultOfQuery(conn, stmt);
            }
        }
    }

    public String get(long userId) throws SQLException, NotFoundException {
        try(Connection conn = Constants.getConnection()) {
            String sql = "SELECT getUser(?)";
            beginTransaction(conn, Connection.TRANSACTION_SERIALIZABLE);
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, userId);
                return getResultOfQuery(conn, stmt);
            }
        }
    }

    public User getUserByEmail(Credentials credentials) throws AuthenticationDeniedException, SQLException {
        try(Connection conn = Constants.getConnection()) {
            String sql = "SELECT * FROM users WHERE email = ?;";
            beginTransaction(conn, Connection.TRANSACTION_SERIALIZABLE);
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, credentials.getEmail());
                ResultSet rs = stmt.executeQuery();
                endTransaction(conn);
                if (rs.next()) {
                    return createUserInstance(rs);
                } else {
                    throw new NotFoundException("This user does not exist");
                }
            } catch (SQLException e) {
                conn.rollback();
                throw new DatabaseException(e);
            } catch (NotFoundException e) {
                throw new AuthenticationDeniedException("Sorry, your credentials do not Match, please contact your administrator if you have lost your Credentials.");
            }
        }
    }

    public User getUserInstance(long userId) throws SQLException, NotFoundException {
        try(Connection conn = Constants.getConnection()) {
            String sql = "SELECT * FROM users WHERE userId = ?";
            beginTransaction(conn, Connection.TRANSACTION_SERIALIZABLE);
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, userId);

                ResultSet rs = stmt.executeQuery();
                endTransaction(conn);
                if (rs.next()) {
                    return createUserInstance(rs);
                } else {
                    throw new NotFoundException("This user does not exist");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                conn.rollback();
                throw new DatabaseException(e);
            }
        }
    }

    private User createUserInstance(ResultSet rs) throws SQLException {
        return new User(
                rs.getLong("userId"),
                rs.getString("email"),
                rs.getString("password"),
                rs.getString("nickname"),
                rs.getInt("clearanceLevel")
        );
    }
}

