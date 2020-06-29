package nl.utwente.di.team26.Product.dao.Maps;

import nl.utwente.di.team26.Constants;
import nl.utwente.di.team26.Exception.Exceptions.NotFoundException;
import nl.utwente.di.team26.Product.dao.Dao;
import nl.utwente.di.team26.Product.dao.DaoInterface;
import nl.utwente.di.team26.Product.model.Map.MapObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;


/**
 * MapObject Data Access Object (DAO).
 * This class contains all database handling that is needed to
 * permanently store and retrieve MapObject object instances.
 */
public class MapObjectsDao extends Dao implements DaoInterface<MapObject> {

    public synchronized long create(MapObject mapObject) throws SQLException {
        try(Connection conn = Constants.getConnection()) {
            String sql = "INSERT INTO MapObjects (mapId, resourceId, "
                    + "latLangs) VALUES (?, ?, ?) RETURNING objectId";
            beginTransaction(conn, Connection.TRANSACTION_READ_COMMITTED);
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setLong(1, mapObject.getMapId());
                stmt.setLong(2, mapObject.getResourceId());
                stmt.setString(3, Arrays.toString(mapObject.getLatLangs()));
                return executeCreate(conn, stmt);
            }
        }
    }

    public void deleteAll() throws SQLException, NotFoundException {
        try(Connection conn = Constants.getConnection()) {
            String sql = "DELETE FROM MapObjects";
            beginTransaction(conn, Connection.TRANSACTION_READ_COMMITTED);
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                databaseUpdate(conn, stmt);
            }
        }
    }

    public void save(MapObject mapObject) throws NotFoundException, SQLException {
        try(Connection conn = Constants.getConnection()) {
            String sql = "UPDATE MapObjects SET mapId = ?, resourceId = ?, latLangs = ? WHERE (objectId = ? ) ";
            beginTransaction(conn, Connection.TRANSACTION_READ_COMMITTED);
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, mapObject.getMapId());
                stmt.setLong(2, mapObject.getResourceId());
                stmt.setString(3, Arrays.toString(mapObject.getLatLangs()));
                stmt.setLong(4, mapObject.getObjectId());
                databaseUpdate(conn, stmt);
            }
        }
    }

    public void delete(MapObject mapObject) throws NotFoundException, SQLException {
        try(Connection conn = Constants.getConnection()) {
            String sql = "DELETE FROM MapObjects WHERE (objectId = ? ) ";
            beginTransaction(conn, Connection.TRANSACTION_READ_COMMITTED);
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, mapObject.getObjectId());
                databaseUpdate(conn, stmt);
            }
        }
    }

    public void deleteSelected(Long[] arrayOfObjectsToDelete) throws SQLException {
        for (Long oid : arrayOfObjectsToDelete) {
            try {
                delete(new MapObject(oid));
            } catch (NotFoundException e) {
                //It is ok if an object was not found, maybe already deleted.
            }
        }
    }

    public void deleteAllForMap(long mapId) throws NotFoundException, SQLException {
        try(Connection conn = Constants.getConnection()) {
            String sql = "DELETE FROM MapObjects WHERE (mapId = ? )";
            beginTransaction(conn, Connection.TRANSACTION_READ_COMMITTED);
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, mapId);
                databaseUpdate(conn, stmt);
            }
        }
    }

    public String generateReport(long mapId) throws SQLException, NotFoundException {
        try(Connection conn = Constants.getConnection()) {
            String sql = "SELECT generateMapReport(?)";
            beginTransaction(conn, Connection.TRANSACTION_SERIALIZABLE);
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, mapId);
                return getResultOfQuery(conn, stmt);
            }
        }
    }

    public String getAllObjectsOnMap(long mapId) throws NotFoundException, SQLException {
        try(Connection conn = Constants.getConnection()) {
            String sql = "SELECT getAllObjectsOnMap(?)";
            beginTransaction(conn, Connection.TRANSACTION_SERIALIZABLE);
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, mapId);
                return getResultOfQuery(conn, stmt);
            }
        }
    }

    public void putSelected(MapObject[] arrayOfObjectsToPut) throws SQLException {
        for (MapObject mapObject : arrayOfObjectsToPut) {
            try {
                save(mapObject);
            } catch (NotFoundException e) {
                //It is ok if an object was not found, maybe already deleted.
            }
        }
    }

    public void createMany(MapObject[] newObjectToAdd) throws SQLException {
        for (MapObject mapObject : newObjectToAdd) {
            create(mapObject);
        }
    }
}