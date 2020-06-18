package nl.utwente.di.team26.Product.dao.Authentication;

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

    @Override
    public long create(Connection conn, Session valueObject) throws SQLException {
        String sql = "INSERT INTO session (token, userId) VALUES (?, ?) returning tokenId";
        beginTransaction(conn, Connection.TRANSACTION_READ_COMMITTED);
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            clearTokensForUser(conn, valueObject.getUserId(), false);
            stmt.setString(1, valueObject.getToken());
            stmt.setLong(2, valueObject.getUserId());

            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                endTransaction(conn);
                return resultSet.getLong(1);
            } else {
                throw new DatabaseException("Could not create a new Session.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            conn.rollback();
            throw new DatabaseException(e);
        }
    }

    public void clearTokensForUser(Connection conn, long userId, boolean createNewTransaction) throws SQLException {
        String sql = "DELETE FROM session WHERE (userId = ?);";
        if (createNewTransaction) {
            beginTransaction(conn, Connection.TRANSACTION_READ_COMMITTED);
        }
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            int rowcount = databaseUpdate(stmt);
            if (createNewTransaction) {
                endTransaction(conn);
            }
        } catch (SQLException e) {
            conn.rollback();
            throw new DatabaseException(e);
        }
    }

    @Override
    public void save(Connection conn, Session valueObject) throws NotFoundException, SQLException {
        //no used.
    }

    public void delete(Connection conn, Session valueObject) throws NotFoundException, SQLException {

        String sql = "DELETE FROM session WHERE (tokenId = ? ) ";
        beginTransaction(conn, Connection.TRANSACTION_READ_COMMITTED);
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, valueObject.getTokenId());

            int rowcount = databaseUpdate(stmt);
            endTransaction(conn);
            if (rowcount == 0) {
                //System.out.println("Object could not be deleted (PrimaryKey not found)");
                throw new NotFoundException("Object could not be deleted! (PrimaryKey not found)");
            }
        }
    }

    public void deleteAll(Connection conn) throws SQLException {

        String sql = "DELETE FROM session";
        beginTransaction(conn, Connection.TRANSACTION_READ_COMMITTED);
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            int rowcount = databaseUpdate(stmt);
            endTransaction(conn);
        } catch (SQLException e) {
            conn.rollback();
            throw new DatabaseException(e);
        }
    }

    public void checkExist(Connection conn, String token) throws NotFoundException, SQLException {
        String sql = "SELECT * FROM session where token = ?";
        beginTransaction(conn, Connection.TRANSACTION_SERIALIZABLE);
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, token);
            ResultSet resultSet = stmt.executeQuery();
            endTransaction(conn);
            if (resultSet.next()) {
                return;
            } else {
                throw new NotFoundException("Session not Found");
            }
        } catch (SQLException e) {
            conn.rollback();
            throw new DatabaseException(e);
        }
    }

    public long maxId(Connection conn) throws SQLException {

        String sql = "SELECT max(tokenid) FROM session";
        beginTransaction(conn, Connection.TRANSACTION_SERIALIZABLE);
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet result = stmt.executeQuery();
            endTransaction(conn);
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
