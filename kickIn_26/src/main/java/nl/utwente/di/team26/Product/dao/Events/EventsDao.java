package nl.utwente.di.team26.Product.dao.Events;

import nl.utwente.di.team26.Exceptions.NotFoundException;
import nl.utwente.di.team26.Product.dao.Dao;
import nl.utwente.di.team26.Product.dao.DaoInterface;
import nl.utwente.di.team26.Product.model.Event.Event;
import nl.utwente.di.team26.Utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EventsDao extends Dao implements DaoInterface<Event> {

    public synchronized long create(Connection conn, Event valueObject) throws SQLException {

        String sql = "INSERT INTO Events (name, description, location, date, createdBy, lastEditedBy) VALUES (?, ?, ?, ?::date, ?, ?) returning eventid";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, valueObject.getName());
            stmt.setString(2, valueObject.getDescription());
            stmt.setString(3, valueObject.getLocation());
            stmt.setString(4, valueObject.getDate());
            stmt.setLong(5, valueObject.getCreatedBy());
            stmt.setLong(6, valueObject.getLastEditedBy());

            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                return resultSet.getLong(1);
            } else {
                //System.out.println("PrimaryKey Error when updating DB!");
                throw new SQLException("PrimaryKey Error when updating DB!");
            }
        }
    }

    public void save(Connection conn, Event valueObject) throws NotFoundException, SQLException {

        String sql = "UPDATE Events SET name = ?, description = ?, location = ?, "
                + "lastEditedBy = ? WHERE (eventId = ? ) ";

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
        }
    }

    public void delete(Connection conn, Event valueObject) throws NotFoundException, SQLException {

        String sql = "DELETE FROM Events WHERE (eventId = ? ) ";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, valueObject.getEventId());

            int rowcount = databaseUpdate(stmt);
            if (rowcount == 0) {
                //System.out.println("Object could not be deleted (PrimaryKey not found)");
                throw new NotFoundException("Object could not be deleted! (PrimaryKey not found)");
            }
        }
    }

    public void deleteAll(Connection conn) throws SQLException {

        String sql = "DELETE FROM Events";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            int rowcount = databaseUpdate(stmt);
        }
    }

    public String getAllEvents(Connection conn) throws SQLException, NotFoundException {
        String sql = "SELECT getAllEvents();";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            return getResultOfQuery(stmt);
        }
    }

    public String getEvent(Connection conn, long eventId) throws NotFoundException, SQLException {
        String sql = "SELECT getEvent(?);";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, eventId);
            return getResultOfQuery(stmt);
        }
    }
}
