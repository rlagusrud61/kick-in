package nl.utwente.di.team26.Product.dao.Maps;

import nl.utwente.di.team26.CONSTANTS;
import nl.utwente.di.team26.Exception.Exceptions.NotFoundException;
import nl.utwente.di.team26.Product.dao.Dao;
import nl.utwente.di.team26.Product.dao.DaoInterface;
import nl.utwente.di.team26.Product.model.Map.MapObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


/**
 * MapObject Data Access Object (DAO).
 * This class contains all database handling that is needed to
 * permanently store and retrieve MapObject object instances.
 */
public class MapObjectsDao extends Dao implements DaoInterface<MapObject> {

    public synchronized long create(MapObject valueObject) throws SQLException {
        try(Connection conn = CONSTANTS.getConnection()) {
            String sql = "INSERT INTO MapObjects (mapId, resourceId, "
                    + "latLangs) VALUES (?, ?, ?) RETURNING objectId";
            beginTransaction(conn, Connection.TRANSACTION_READ_COMMITTED);
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setLong(1, valueObject.getMapId());
                stmt.setLong(2, valueObject.getResourceId());
                stmt.setString(3, valueObject.getLatLangs());
                return executeCreate(conn, stmt);
            }
        }
    }

    public void deleteAll() throws SQLException, NotFoundException {
        try(Connection conn = CONSTANTS.getConnection()) {
            String sql = "DELETE FROM MapObjects";
            beginTransaction(conn, Connection.TRANSACTION_READ_COMMITTED);
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                databaseUpdate(conn, stmt);
            }
        }
    }

    public void save(MapObject valueObject) throws NotFoundException, SQLException {
        try(Connection conn = CONSTANTS.getConnection()) {
            String sql = "UPDATE MapObjects SET mapId = ?, resourceId = ?, latLangs = ? WHERE (objectId = ? ) ";
            beginTransaction(conn, Connection.TRANSACTION_READ_COMMITTED);
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, valueObject.getMapId());
                stmt.setLong(2, valueObject.getResourceId());
                stmt.setString(3, valueObject.getLatLangs());
                stmt.setLong(4, valueObject.getObjectId());
                databaseUpdate(conn, stmt);
            }
        }
    }

    public void delete(MapObject valueObject) throws NotFoundException, SQLException {
        try(Connection conn = CONSTANTS.getConnection()) {
            String sql = "DELETE FROM MapObjects WHERE (objectId = ? ) ";
            beginTransaction(conn, Connection.TRANSACTION_READ_COMMITTED);
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, valueObject.getObjectId());
                databaseUpdate(conn, stmt);
            }
        }
    }

    public void deleteAllForMap(MapObject valueObject) throws NotFoundException, SQLException {
        try(Connection conn = CONSTANTS.getConnection()) {
            String sql = "DELETE FROM MapObjects WHERE (mapId = ? )";
            beginTransaction(conn, Connection.TRANSACTION_READ_COMMITTED);
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, valueObject.getObjectId());
                databaseUpdate(conn, stmt);
            }
        }
    }

    public String generateReport(long mapId) throws SQLException, NotFoundException {
        try(Connection conn = CONSTANTS.getConnection()) {
            String sql = "SELECT generateMapReport(?)";
            beginTransaction(conn, Connection.TRANSACTION_SERIALIZABLE);
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, mapId);
                return getResultOfQuery(conn, stmt);
            }
        }
    }

    public String getAllObjectsOnMap(long mapId) throws NotFoundException, SQLException {
        try(Connection conn = CONSTANTS.getConnection()) {
            String sql = "SELECT getAllObjectsOnMap(?)";
            beginTransaction(conn, Connection.TRANSACTION_SERIALIZABLE);
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, mapId);
                return getResultOfQuery(conn, stmt);
            }
        }
    }
}