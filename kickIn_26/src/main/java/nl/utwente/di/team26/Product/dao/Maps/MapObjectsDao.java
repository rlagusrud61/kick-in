package nl.utwente.di.team26.Product.dao.Maps;

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
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, valueObject.getMapId());
            stmt.setLong(2, valueObject.getResourceId());
            stmt.setString(3, valueObject.getLatLangs());
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                return resultSet.getLong(1);//dummy return
            } else {
                throw new SQLException("Object could not be created");
            }
        }
    }

    public void deleteAll(Connection conn) throws SQLException {

        String sql = "DELETE FROM MapObjects";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            int rowcount = databaseUpdate(stmt);
        }
    }

    public void save(Connection conn, MapObject valueObject) throws NotFoundException, SQLException {

        String sql = "UPDATE MapObjects SET mapId = ?, resourceId = ?, latLangs = ? WHERE (objectId = ? ) ";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, valueObject.getMapId());
            stmt.setLong(2, valueObject.getResourceId());
            stmt.setString(3, valueObject.getLatLangs());

            stmt.setLong(4, valueObject.getObjectId());

            int rowcount = databaseUpdate(stmt);
            if (rowcount == 0) {
                //System.out.println("Object could not be saved! (PrimaryKey not found)");
                throw new NotFoundException("Object could not be saved! (PrimaryKey not found)");
            }
        }
    }

    public void delete(Connection conn, MapObject valueObject) throws NotFoundException, SQLException {

        String sql = "DELETE FROM MapObjects WHERE (objectId = ? ) ";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, valueObject.getObjectId());

            int rowcount = databaseUpdate(stmt);
            if (rowcount == 0) {
                //System.out.println("Object could not be deleted (PrimaryKey not found)");
                throw new NotFoundException("Object could not be deleted! (PrimaryKey not found)");
            }
            if (rowcount > 1) {
                //System.out.println("PrimaryKey Error when updating DB! (Many objects were deleted!)");
                throw new SQLException("PrimaryKey Error when updating DB! (Many objects were deleted!)");
            }
        }
    }

    public void deleteAllForMap(Connection conn, MapObject valueObject) throws NotFoundException, SQLException {

        String sql = "DELETE FROM MapObjects WHERE (mapId = ? )";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, valueObject.getObjectId());

            int rowcount = databaseUpdate(stmt);
            if (rowcount == 0) {
                //System.out.println("Object could not be deleted (PrimaryKey not found)");
                throw new NotFoundException("Object could not be deleted! (PrimaryKey not found)");
            }
        }
    }

    public String generateReport(Connection conn, long mapId) throws SQLException, NotFoundException {
        String sql = "SELECT generateMapReport(?)";
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, mapId);
            return getResultOfQuery(conn, stmt);
        }
    }

    public String getAllObjectsOnMap(Connection conn, long mapId) throws NotFoundException, SQLException {
        String sql = "SELECT getAllObjectsOnMap(?)";
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, mapId);
            return getResultOfQuery(conn, stmt);
        }
    }
}