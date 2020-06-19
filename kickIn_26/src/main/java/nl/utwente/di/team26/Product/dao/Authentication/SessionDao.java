package nl.utwente.di.team26.Product.dao.Authentication;

import nl.utwente.di.team26.CONSTANTS;
import nl.utwente.di.team26.Exception.Exceptions.DatabaseException;
import nl.utwente.di.team26.Exception.Exceptions.NotFoundException;
import nl.utwente.di.team26.Product.dao.Dao;
import nl.utwente.di.team26.Product.dao.DaoInterface;
import nl.utwente.di.team26.Product.model.Authentication.Session;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SessionDao extends Dao implements DaoInterface<Session> {

    public long create(Session session) throws SQLException {
        try(Connection conn = CONSTANTS.getConnection()) {
            String sql = "INSERT INTO session (token, userId) VALUES (?, ?) returning tokenId";
            beginTransaction(conn, Connection.TRANSACTION_READ_COMMITTED);
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                clearTokensForUser(conn, session.getUserId());
                stmt.setString(1, session.getToken());
                stmt.setLong(2, session.getUserId());
                return executeCreate(conn, stmt);
            }
        }
    }
    public void save(Session session) throws NotFoundException, SQLException {
        //no used.
    }
    public void delete(Session session) throws NotFoundException, SQLException {
        try(Connection conn = CONSTANTS.getConnection()) {
            String sql = "DELETE FROM session WHERE (tokenId = ? ) ";
            beginTransaction(conn, Connection.TRANSACTION_READ_COMMITTED);
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, session.getTokenId());
                databaseUpdate(conn, stmt);
            }
        }
    }
    public void deleteAll() throws SQLException, NotFoundException {
        try(Connection conn = CONSTANTS.getConnection()) {
            String sql = "DELETE FROM session";
            beginTransaction(conn, Connection.TRANSACTION_READ_COMMITTED);
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                databaseUpdate(conn, stmt);
            }
        }
    }
    public String get(long oid) throws NotFoundException, SQLException {
        try(Connection conn = CONSTANTS.getConnection()) {
            String sql = "SELECT getSession(?);";
            beginTransaction(conn, Connection.TRANSACTION_SERIALIZABLE);
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, oid);
                return getResultOfQuery(conn, stmt);
            }
        }
    }
    public String getAll() throws NotFoundException, SQLException {
        try(Connection conn = CONSTANTS.getConnection()) {
            String sql = "SELECT getAllSessions();";
            beginTransaction(conn, Connection.TRANSACTION_SERIALIZABLE);
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                return getResultOfQuery(conn, stmt);
            }
        }
    }

    public void clearTokensForUser(Connection conn, long userId) throws SQLException {
        String sql = "DELETE FROM session WHERE (userId = ?);";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            stmt.executeUpdate();
        }
    }
    public void clearTokensForUser(long userId) throws SQLException {
        try(Connection conn = CONSTANTS.getConnection()) {
            beginTransaction(conn, Connection.TRANSACTION_SERIALIZABLE);
            clearTokensForUser(conn, userId);
            endTransaction(conn);
        }
    }
    public void checkExist(String token) throws NotFoundException, SQLException {
        try(Connection conn = CONSTANTS.getConnection()) {
            String sql = "SELECT * FROM session where token = ?";
            beginTransaction(conn, Connection.TRANSACTION_SERIALIZABLE);
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, token);
                ResultSet resultSet = stmt.executeQuery();
                if (!resultSet.next()) {
                    throw new NotFoundException("Session not Found");
                }
            } catch (SQLException e) {
                conn.rollback();
                throw new DatabaseException(e);
            }
        }
    }
    public long maxId() throws SQLException {
        try(Connection conn = CONSTANTS.getConnection()) {
            String sql = "SELECT max(tokenid) FROM session";
            beginTransaction(conn, Connection.TRANSACTION_SERIALIZABLE);
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                ResultSet result = stmt.executeQuery();
                if (result.next()) {
                    return result.getLong(1);
                } else {
                    throw new DatabaseException("No result obtained for the sessions");
                }
            } catch (SQLException e) {
                conn.rollback();
                throw new DatabaseException(e);
            }
        }
    }
}
