package nl.utwente.di.team26.Product.dao.Authentication;

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
    public long create(Connection conn, User valueObject) throws SQLException {
        String sql = "INSERT INTO users (email, password, "
                + "clearanceLevel) VALUES (?, ?, ?) RETURNING userId";
        beginTransaction(conn, Connection.TRANSACTION_READ_COMMITTED);

        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, valueObject.getEmail());
            stmt.setString(2, valueObject.getPassword());
            stmt.setInt(3, valueObject.getClearanceLevel());

            ResultSet resultSet = stmt.executeQuery();
            endTransaction(conn);
            if (resultSet.next()) {
                return resultSet.getLong(1);
            } else {
                throw new DatabaseException("User could not be created");
            }
        } catch (SQLException e) {
            conn.rollback();
            throw new DatabaseException(e);
        }
    }

    @Override
    public void save(Connection conn, User user) throws NotFoundException, SQLException {
        String sql = "UPDATE User SET email = ?, password = ?, nickname = ?, clearanceLevel = ? WHERE (userId = ?) ";
        beginTransaction(conn, Connection.TRANSACTION_READ_COMMITTED);
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getNickname());
            stmt.setInt(4, user.getClearanceLevel());

            stmt.setLong(5, user.getUserId());

            int rowcount = databaseUpdate(stmt);
            endTransaction(conn);
            if (rowcount == 0) {
                //System.out.println("Object could not be saved! (PrimaryKey not found)");
                throw new NotFoundException("Object could not be saved! (PrimaryKey not found)");
            }
        } catch (SQLException e) {
            conn.rollback();
            throw new DatabaseException(e);
        }
    }

    @Override
    public void delete(Connection conn, User user) throws NotFoundException, SQLException {
        String sql = "DELETE FROM Users WHERE (userId = ? ) ";
        beginTransaction(conn, Connection.TRANSACTION_READ_COMMITTED);
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, user.getUserId());

            int rowcount = databaseUpdate(stmt);
            endTransaction(conn);
            if (rowcount == 0) {
                //System.out.println("Object could not be deleted (PrimaryKey not found)");
                throw new NotFoundException("Object could not be deleted! (PrimaryKey not found)");
            }
        } catch (SQLException e) {
            conn.rollback();
            throw new DatabaseException(e);
        }
    }

    @Override
    public void deleteAll(Connection conn) throws SQLException {
        String sql = "DELETE FROM Users";
        beginTransaction(conn, Connection.TRANSACTION_READ_COMMITTED);
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            int rowcount = databaseUpdate(stmt);
        } catch (SQLException e) {
            conn.rollback();
            throw new DatabaseException(e);
        }
    }

    public String getAllUsers(Connection conn) throws NotFoundException, SQLException {
        String sql = "SELECT getAllUsers();";
        beginTransaction(conn, Connection.TRANSACTION_SERIALIZABLE);
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            return getResultOfQuery(conn, stmt);
        }
    }

    public String getUser(Connection conn, long userId) throws SQLException, NotFoundException {
        String sql = "SELECT getUser(?)";
        beginTransaction(conn, Connection.TRANSACTION_SERIALIZABLE);
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            return getResultOfQuery(conn, stmt);
        }
    }

    public User authenticateUser(Connection conn, Credentials credentials) throws AuthenticationDeniedException, SQLException {
        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
        beginTransaction(conn, Connection.TRANSACTION_SERIALIZABLE);
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, credentials.getEmail());
            stmt.setString(2, credentials.getPassword());
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

    public User getUserInstance(Connection conn, long userId) throws SQLException, NotFoundException {
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
            conn.rollback();
            throw new DatabaseException(e);
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

