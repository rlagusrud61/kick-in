package nl.utwente.di.team26.Product.dao.Maps;

import nl.utwente.di.team26.Exceptions.NotFoundException;
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
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, valueObject.getName());
            stmt.setString(2, valueObject.getDescription());
            stmt.setLong(3, valueObject.getCreatedBy());
            stmt.setLong(4, valueObject.getLastEditedBy());
            stmt.execute();

            ResultSet resultSet = stmt.getResultSet();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            } else {
                throw new SQLException("Some error creating a new map.");
            }

        }
    }

    @Override
    public void save(Connection conn, Map valueObject) throws NotFoundException, SQLException {

        String sql = "UPDATE Maps SET name = ?, description = ?, "
                + "lastEditedBy = ? WHERE (mapId = ? ) ";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, valueObject.getName());
            stmt.setString(2, valueObject.getDescription());
            stmt.setLong(3, valueObject.getLastEditedBy());

            stmt.setLong(4, valueObject.getMapId());

            int rowcount = databaseUpdate(stmt);
            if (rowcount == 0) {
                //System.out.println("Object could not be saved! (PrimaryKey not found)");
                throw new NotFoundException("Object could not be saved! (PrimaryKey not found)");
            }
        }
    }

    @Override
    public void delete(Connection conn, Map valueObject) throws NotFoundException, SQLException {

        String sql = "DELETE FROM Maps WHERE (mapId = ? ) ";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, valueObject.getMapId());

            int rowcount = databaseUpdate(stmt);
            if (rowcount == 0) {
                //System.out.println("Object could not be deleted (PrimaryKey not found)");
                throw new NotFoundException("Object could not be deleted! (PrimaryKey not found)");
            }
        }
    }

    @Override
    public void deleteAll(Connection conn) throws SQLException {

        String sql = "DELETE FROM Maps";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            int rowcount = databaseUpdate(stmt);
        }
    }

    public String getAllMaps(Connection conn) throws NotFoundException, SQLException {
        String sql = "SELECT getAllMaps();";
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            return getResultOfQuery(stmt);
        }
    }

    public String getMap(Connection conn, long mapId) throws SQLException, NotFoundException {
        String sql = "SELECT getMap(?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, mapId);
            return getResultOfQuery(stmt);
        }
    }

    public void saveLastEditedBy(Connection conn, long mapId, long userId) throws SQLException, NotFoundException {
        String sql = "UPDATE Maps SET lastEditedBy = ? WHERE (mapId = ?) ";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            stmt.setLong(2, mapId);

            int rowcount = databaseUpdate(stmt);
            if (rowcount == 0) {
                //System.out.println("Object could not be saved! (PrimaryKey not found)");
                throw new NotFoundException("Object could not be saved! (PrimaryKey not found)");
            }
        }
    }

}
