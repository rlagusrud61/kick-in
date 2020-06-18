package nl.utwente.di.team26.Product.dao.Events;

import nl.utwente.di.team26.Exception.Exceptions.DatabaseException;
import nl.utwente.di.team26.Exception.Exceptions.NotFoundException;
import nl.utwente.di.team26.Product.dao.Dao;
import nl.utwente.di.team26.Product.dao.DaoInterface;
import nl.utwente.di.team26.Product.model.Event.Event;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EventsDao extends Dao implements DaoInterface<Event> {

    public synchronized long create(Connection conn, Event valueObject) throws SQLException {

        String sql = "INSERT INTO Events (name, description, location, date, createdBy, lastEditedBy) VALUES (?, ?, ?, ?::date, ?, ?) returning eventid";
        beginTransaction(conn, Connection.TRANSACTION_READ_COMMITTED);
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, valueObject.getName());
            stmt.setString(2, valueObject.getDescription());
            stmt.setString(3, valueObject.getLocation());
            stmt.setString(4, valueObject.getDate());
            stmt.setLong(5, valueObject.getCreatedBy());
            stmt.setLong(6, valueObject.getLastEditedBy());

            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                endTransaction(conn);
                return resultSet.getLong(1);
            } else {
                throw new DatabaseException("Object could not be created!");
            }
        } catch (SQLException e) {
            conn.rollback();
            throw new DatabaseException(e);
        }
    }

    public void save(Connection conn, Event valueObject) throws NotFoundException, SQLException {

        String sql = "UPDATE Events SET name = ?, description = ?, location = ?, "
                + "lastEditedBy = ? WHERE (eventId = ? ) ";
        beginTransaction(conn, Connection.TRANSACTION_READ_COMMITTED);
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, valueObject.getName());
            stmt.setString(2, valueObject.getDescription());
            stmt.setString(3, valueObject.getLocation());
            stmt.setLong(4, valueObject.getLastEditedBy());

            stmt.setLong(5, valueObject.getEventId());

            int rowcount = databaseUpdate(stmt);
            if (rowcount == 0) {
                //System.out.println("Object could not be saved! (PrimaryKey not found)");
                throw new NotFoundException("Object could not be saved! (PrimaryKey not found)");
            }
            endTransaction(conn);
        } catch (SQLException e) {
            conn.rollback();
            throw new DatabaseException(e);
        }
    }

    public void delete(Connection conn, Event valueObject) throws NotFoundException, SQLException {

        String sql = "DELETE FROM Events WHERE (eventId = ? ) ";
        beginTransaction(conn, Connection.TRANSACTION_READ_COMMITTED);
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, valueObject.getEventId());

            int rowcount = databaseUpdate(stmt);
            if (rowcount == 0) {
                //System.out.println("Object could not be deleted (PrimaryKey not found)");
                throw new NotFoundException("Object could not be deleted! (PrimaryKey not found)");
            }
            endTransaction(conn);
        } catch (SQLException e) {
            conn.rollback();
            throw new DatabaseException(e);
        }
    }

    public void deleteAll(Connection conn) throws SQLException {

        String sql = "DELETE FROM Events";
        beginTransaction(conn, Connection.TRANSACTION_READ_COMMITTED);
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            int rowcount = databaseUpdate(stmt);
            endTransaction(conn);
        } catch(SQLException e) {
            conn.rollback();
            throw new DatabaseException(e);
        }
    }

    public String getAllEvents(Connection conn) throws SQLException, NotFoundException {
        String sql = "SELECT getAllEvents();";
        beginTransaction(conn, Connection.TRANSACTION_SERIALIZABLE);
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            return getResultOfQuery(conn, stmt);
        }
    }

    public String getEvent(Connection conn, long eventId) throws NotFoundException, SQLException {
        String sql = "SELECT getEvent(?);";
        beginTransaction(conn, Connection.TRANSACTION_SERIALIZABLE);
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, eventId);
            return getResultOfQuery(conn, stmt);
        }
    }
}
