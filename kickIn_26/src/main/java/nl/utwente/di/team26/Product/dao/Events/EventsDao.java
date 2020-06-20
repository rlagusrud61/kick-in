package nl.utwente.di.team26.Product.dao.Events;

import nl.utwente.di.team26.CONSTANTS;
import nl.utwente.di.team26.Exception.Exceptions.NotFoundException;
import nl.utwente.di.team26.Product.dao.Dao;
import nl.utwente.di.team26.Product.dao.DaoInterface;
import nl.utwente.di.team26.Product.model.Event.Event;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EventsDao extends Dao implements DaoInterface<Event> {

    public synchronized long create(Event valueObject) throws SQLException {
        try(Connection conn = CONSTANTS.getConnection()) {
            String sql = "INSERT INTO Events (name, description, location, date, createdBy, lastEditedBy) VALUES (?, ?, ?, ?::date, ?, ?) returning eventid";
            beginTransaction(conn, Connection.TRANSACTION_READ_COMMITTED);
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, valueObject.getName());
                stmt.setString(2, valueObject.getDescription());
                stmt.setString(3, valueObject.getLocation());
                stmt.setString(4, valueObject.getDate());
                stmt.setLong(5, valueObject.getCreatedBy());
                stmt.setLong(6, valueObject.getLastEditedBy());
                return executeCreate(conn, stmt);
            }
        }
    }

    public void save(Event valueObject) throws NotFoundException, SQLException {
        try(Connection conn = CONSTANTS.getConnection()) {
            String sql = "UPDATE Events SET name = ?, description = ?, location = ?, "
                    + "lastEditedBy = ? WHERE (eventId = ? ) ";
            beginTransaction(conn, Connection.TRANSACTION_READ_COMMITTED);
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, valueObject.getName());
                stmt.setString(2, valueObject.getDescription());
                stmt.setString(3, valueObject.getLocation());
                stmt.setLong(4, valueObject.getLastEditedBy());
                stmt.setLong(5, valueObject.getEventId());
                databaseUpdate(conn, stmt);
            }
        }
    }

    public void delete(Event valueObject) throws NotFoundException, SQLException {
        try(Connection conn = CONSTANTS.getConnection()) {
            String sql = "DELETE FROM Events WHERE (eventId = ? ) ";
            beginTransaction(conn, Connection.TRANSACTION_READ_COMMITTED);
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, valueObject.getEventId());
                databaseUpdate(conn, stmt);
            }
        }
    }

    public void deleteAll() throws SQLException, NotFoundException {
        try(Connection conn = CONSTANTS.getConnection()) {
            String sql = "DELETE FROM Events";
            beginTransaction(conn, Connection.TRANSACTION_READ_COMMITTED);
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                databaseUpdate(conn, stmt);
            }
        }
    }

    public String getAll() throws SQLException, NotFoundException {
        try(Connection conn = CONSTANTS.getConnection()) {
            String sql = "SELECT getAllEvents();";
            beginTransaction(conn, Connection.TRANSACTION_SERIALIZABLE);
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                return getResultOfQuery(conn, stmt);
            }
        }
    }

    public String get(long eventId) throws NotFoundException, SQLException {
        try(Connection conn = CONSTANTS.getConnection()) {
            String sql = "SELECT getEvent(?);";
            beginTransaction(conn, Connection.TRANSACTION_SERIALIZABLE);
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, eventId);
                return getResultOfQuery(conn, stmt);
            }
        }
    }
}
