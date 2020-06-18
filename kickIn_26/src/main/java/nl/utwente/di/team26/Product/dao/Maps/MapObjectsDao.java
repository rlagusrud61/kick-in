package nl.utwente.di.team26.Product.dao.Maps;

import nl.utwente.di.team26.Exception.Exceptions.DatabaseException;
import nl.utwente.di.team26.Exception.Exceptions.NotFoundException;
import nl.utwente.di.team26.Product.dao.Dao;
import nl.utwente.di.team26.Product.dao.DaoInterface;
import nl.utwente.di.team26.Product.model.Map.MapObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * MapObject Data Access Object (DAO).
 * This class contains all database handling that is needed to
 * permanently store and retrieve MapObject object instances.
 */
public class MapObjectsDao extends Dao implements DaoInterface<MapObject> {

    public synchronized long create(Connection conn, MapObject valueObject) throws SQLException {

        String sql = "INSERT INTO MapObjects (mapId, resourceId, "
                + "latLangs) VALUES (?, ?, ?) RETURNING objectId";
        beginTransaction(conn, Connection.TRANSACTION_READ_COMMITTED);
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, valueObject.getMapId());
            stmt.setLong(2, valueObject.getResourceId());
            stmt.setString(3, valueObject.getLatLangs());
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                endTransaction(conn);
                return resultSet.getLong(1);//dummy return
            } else {
                throw new DatabaseException("Object could not be created");
            }
        } catch (SQLException e) {
            conn.rollback();
            throw new DatabaseException(e);
        }
    }

    public void deleteAll(Connection conn) throws SQLException {

        String sql = "DELETE FROM MapObjects";
        beginTransaction(conn, Connection.TRANSACTION_READ_COMMITTED);
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            endTransaction(conn);
            int rowcount = databaseUpdate(stmt);
        } catch (SQLException e) {
            conn.rollback();
            throw new DatabaseException(e);
        }
    }

    public void save(Connection conn, MapObject valueObject) throws NotFoundException, SQLException {

        String sql = "UPDATE MapObjects SET mapId = ?, resourceId = ?, latLangs = ? WHERE (objectId = ? ) ";
        beginTransaction(conn, Connection.TRANSACTION_READ_COMMITTED);
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, valueObject.getMapId());
            stmt.setLong(2, valueObject.getResourceId());
            stmt.setString(3, valueObject.getLatLangs());

            stmt.setLong(4, valueObject.getObjectId());

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

    public void delete(Connection conn, MapObject valueObject) throws NotFoundException, SQLException {

        String sql = "DELETE FROM MapObjects WHERE (objectId = ? ) ";
        beginTransaction(conn, Connection.TRANSACTION_READ_COMMITTED);
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, valueObject.getObjectId());

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

    public void deleteAllForMap(Connection conn, MapObject valueObject) throws NotFoundException, SQLException {

        String sql = "DELETE FROM MapObjects WHERE (mapId = ? )";
        beginTransaction(conn, Connection.TRANSACTION_READ_COMMITTED);
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, valueObject.getObjectId());

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

    public String generateReport(Connection conn, long mapId) throws SQLException, NotFoundException {
        String sql = "SELECT generateMapReport(?)";
        beginTransaction(conn, Connection.TRANSACTION_SERIALIZABLE);
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, mapId);
            return getResultOfQuery(conn, stmt);
        }
    }

    public String getAllObjectsOnMap(Connection conn, long mapId) throws NotFoundException, SQLException {
        String sql = "SELECT getAllObjectsOnMap(?)";
        beginTransaction(conn, Connection.TRANSACTION_SERIALIZABLE);
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, mapId);
            return getResultOfQuery(conn, stmt);
        }
    }
}