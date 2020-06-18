package nl.utwente.di.team26.Product.dao.Maps;

import nl.utwente.di.team26.Exception.Exceptions.DatabaseException;
import nl.utwente.di.team26.Exception.Exceptions.NotFoundException;
import nl.utwente.di.team26.Product.dao.Dao;
import nl.utwente.di.team26.Product.dao.DaoInterface;
import nl.utwente.di.team26.Product.model.Map.Map;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MapsDao extends Dao implements DaoInterface<Map> {

    @Override
    public synchronized long create(Connection conn, Map valueObject) throws SQLException {

        String sql = "INSERT INTO Maps (name, description, "
                + "createdBy, lastEditedBy) VALUES (?, ?, ?, ?) returning mapId";
        beginTransaction(conn, Connection.TRANSACTION_READ_COMMITTED);
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, valueObject.getName());
            stmt.setString(2, valueObject.getDescription());
            stmt.setLong(3, valueObject.getCreatedBy());
            stmt.setLong(4, valueObject.getLastEditedBy());
            stmt.execute();

            ResultSet resultSet = stmt.getResultSet();
            endTransaction(conn);
            if (resultSet.next()) {
                return resultSet.getInt(1);
            } else {
                throw new DatabaseException("Some error creating a new map.");
            }
        } catch (SQLException e) {
            conn.rollback();
            throw new DatabaseException(e);
        }
    }

    @Override
    public void save(Connection conn, Map valueObject) throws NotFoundException, SQLException {

        String sql = "UPDATE Maps SET name = ?, description = ?, "
                + "lastEditedBy = ? WHERE (mapId = ? ) ";
        beginTransaction(conn, Connection.TRANSACTION_READ_COMMITTED);
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, valueObject.getName());
            stmt.setString(2, valueObject.getDescription());
            stmt.setLong(3, valueObject.getLastEditedBy());

            stmt.setLong(4, valueObject.getMapId());

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
    public void delete(Connection conn, Map valueObject) throws NotFoundException, SQLException {

        String sql = "DELETE FROM Maps WHERE (mapId = ? ) ";
        beginTransaction(conn, Connection.TRANSACTION_READ_COMMITTED);
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, valueObject.getMapId());

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

        String sql = "DELETE FROM Maps";
        beginTransaction(conn, Connection.TRANSACTION_READ_COMMITTED);
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            int rowcount = databaseUpdate(stmt);
        } catch (SQLException e) {
            conn.rollback();
            throw new DatabaseException(e);
        }
    }

    public String getAllMaps(Connection conn) throws NotFoundException, SQLException {
        String sql = "SELECT getAllMaps();";
        beginTransaction(conn, Connection.TRANSACTION_SERIALIZABLE);
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            return getResultOfQuery(conn, stmt);
        }
    }

    public String getMap(Connection conn, long mapId) throws SQLException, NotFoundException {
        String sql = "SELECT getMap(?)";
        beginTransaction(conn, Connection.TRANSACTION_SERIALIZABLE);
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, mapId);
            return getResultOfQuery(conn, stmt);
        }
    }

    public void saveLastEditedBy(Connection conn, long mapId, long userId) throws SQLException, NotFoundException {
        String sql = "UPDATE Maps SET lastEditedBy = ? WHERE (mapId = ?) ";
        beginTransaction(conn, Connection.TRANSACTION_SERIALIZABLE);
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            stmt.setLong(2, mapId);

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

}
