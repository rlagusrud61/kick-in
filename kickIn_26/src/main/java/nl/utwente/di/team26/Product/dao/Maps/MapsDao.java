package nl.utwente.di.team26.Product.dao.Maps;

import nl.utwente.di.team26.Constants;
import nl.utwente.di.team26.Exception.Exceptions.NotFoundException;
import nl.utwente.di.team26.Product.dao.Dao;
import nl.utwente.di.team26.Product.dao.DaoInterface;
import nl.utwente.di.team26.Product.model.Map.Map;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MapsDao extends Dao implements DaoInterface<Map> {

    @Override
    public synchronized long create(Map valueObject) throws SQLException {
        try(Connection conn = Constants.getConnection()) {
            String sql = "INSERT INTO Maps (name, description, "
                    + "createdBy, lastEditedBy) VALUES (?, ?, ?, ?) returning mapId";
            beginTransaction(conn, Connection.TRANSACTION_READ_COMMITTED);
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, valueObject.getName());
                stmt.setString(2, valueObject.getDescription());
                stmt.setLong(3, valueObject.getCreatedBy());
                stmt.setLong(4, valueObject.getLastEditedBy());
                return executeCreate(conn, stmt);
            }
        }
    }

    @Override
    public void save(Map valueObject) throws NotFoundException, SQLException {
        try(Connection conn = Constants.getConnection()) {
            String sql = "UPDATE Maps SET name = ?, description = ?, "
                    + "lastEditedBy = ? WHERE (mapId = ? ) ";
            beginTransaction(conn, Connection.TRANSACTION_READ_COMMITTED);
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, valueObject.getName());
                stmt.setString(2, valueObject.getDescription());
                stmt.setLong(3, valueObject.getLastEditedBy());

                stmt.setLong(4, valueObject.getMapId());

                databaseUpdate(conn, stmt);
            }
        }
    }

    public void setNewImage(Map mapToUpdate) throws NotFoundException, SQLException {
        try(Connection conn = Constants.getConnection()) {
            String sql = "UPDATE Maps SET image = ? WHERE (mapId = ? ) ";
            beginTransaction(conn, Connection.TRANSACTION_READ_COMMITTED);
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, mapToUpdate.getImage());
                stmt.setLong(2, mapToUpdate.getMapId());
                databaseUpdate(conn, stmt);
            }
        }
    }

    @Override
    public void delete(Map valueObject) throws NotFoundException, SQLException {
        try(Connection conn = Constants.getConnection()) {
            String sql = "DELETE FROM Maps WHERE (mapId = ? ) ";
            beginTransaction(conn, Connection.TRANSACTION_READ_COMMITTED);
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, valueObject.getMapId());
                databaseUpdate(conn, stmt);
            }
        }
    }

    @Override
    public void deleteAll() throws SQLException, NotFoundException {
        try(Connection conn = Constants.getConnection()) {
            String sql = "DELETE FROM Maps";
            beginTransaction(conn, Connection.TRANSACTION_READ_COMMITTED);
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                databaseUpdate(conn, stmt);
            }
        }
    }

    public String getAllMaps() throws NotFoundException, SQLException {
        try(Connection conn = Constants.getConnection()) {
            String sql = "SELECT getAllMaps();";
            beginTransaction(conn, Connection.TRANSACTION_SERIALIZABLE);
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                return getResultOfQuery(conn, stmt);
            }
        }
    }

    public String getMap(long mapId) throws SQLException, NotFoundException {
        try(Connection conn = Constants.getConnection()) {
            String sql = "SELECT getMap(?)";
            beginTransaction(conn, Connection.TRANSACTION_SERIALIZABLE);
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, mapId);
                return getResultOfQuery(conn, stmt);
            }
        }
    }

    public String getMapImage(long mapId) throws NotFoundException, SQLException {
        try(Connection conn = Constants.getConnection()) {
            String sql = "SELECT getMapImage(?)";
            beginTransaction(conn, Connection.TRANSACTION_SERIALIZABLE);
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, mapId);
                return getResultOfQuery(conn, stmt);
            }
        }
    }

    public void saveLastEditedBy(long mapId, long userId) throws SQLException, NotFoundException {
        try(Connection conn = Constants.getConnection()) {
            String sql = "UPDATE Maps SET lastEditedBy = ? WHERE (mapId = ?) ";
            beginTransaction(conn, Connection.TRANSACTION_READ_COMMITTED);
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, userId);
                stmt.setLong(2, mapId);

                databaseUpdate(conn, stmt);
            }
        }
    }
}
