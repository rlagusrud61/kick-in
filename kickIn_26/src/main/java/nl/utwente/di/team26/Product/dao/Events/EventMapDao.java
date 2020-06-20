package nl.utwente.di.team26.Product.dao.Events;

import nl.utwente.di.team26.CONSTANTS;
import nl.utwente.di.team26.Exception.Exceptions.NotFoundException;
import nl.utwente.di.team26.Product.dao.Dao;
import nl.utwente.di.team26.Product.dao.DaoInterface;
import nl.utwente.di.team26.Product.model.Event.EventMap;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


/**
 * EventMap Data Access Object (DAO).
 * This class contains all database handling that is needed to
 * permanently store and retrieve EventMap object instances.
 */

public class EventMapDao extends Dao implements DaoInterface<EventMap> {

    @Override
    public synchronized long create(EventMap valueObject) throws SQLException {
        try(Connection conn = CONSTANTS.getConnection()) {
            String sql = "INSERT INTO EventMap (eventId, mapId) VALUES (?, ?) returning eventId";
            beginTransaction(conn, Connection.TRANSACTION_READ_COMMITTED);
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, valueObject.getEventId());
                stmt.setLong(2, valueObject.getMapId());
                return executeCreate(conn, stmt);//unused variable, eventId is returned.
            }
        }
    }

    @Override
    public void save(EventMap valueObject) throws NotFoundException, SQLException {
        //Nothing to save.
    }

    public void delete(EventMap valueObject) throws NotFoundException, SQLException {
        try(Connection conn = CONSTANTS.getConnection()) {
            String sql = "DELETE FROM EventMap WHERE (eventId = ? AND mapId = ? ) ";
            beginTransaction(conn, Connection.TRANSACTION_READ_COMMITTED);
            //only read data already committed, do not want to delete data that doesn't even exist.
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, valueObject.getEventId());
                stmt.setLong(2, valueObject.getMapId());
                databaseUpdate(conn, stmt);
            }
        }
    }

    public void deleteAll() throws SQLException, NotFoundException {
        try(Connection conn = CONSTANTS.getConnection()) {
            beginTransaction(conn, Connection.TRANSACTION_READ_COMMITTED);
            String sql = "DELETE FROM EventMap";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                databaseUpdate(conn, stmt);
            }
        }
    }

    public void deleteAllForEvent(long eventId) throws SQLException, NotFoundException {
        try(Connection conn = CONSTANTS.getConnection()) {
            String sql = "DELETE FROM EventMap WHERE (eventId = ?) ";
            beginTransaction(conn, Connection.TRANSACTION_READ_COMMITTED);
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, eventId);
                databaseUpdate(conn, stmt);
            }
        }
    }

    public String getAllMapsFor(long eventId) throws NotFoundException, SQLException {
        try(Connection conn = CONSTANTS.getConnection()) {
            String sql = "select getAllMapsForEvent(?);";
            beginTransaction(conn, Connection.TRANSACTION_SERIALIZABLE);
            //all get requests will have to be serializable, due to phantoms.
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, eventId);
                return getResultOfQuery(conn, stmt);
            }
        }
    }

    public String allEventsFor(long mapId) throws SQLException, NotFoundException {
        try(Connection conn = CONSTANTS.getConnection()) {
            String sql = "select getAllEventsForMap(?);";
            beginTransaction(conn, Connection.TRANSACTION_SERIALIZABLE);
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, mapId);
                return getResultOfQuery(conn, stmt);
            }
        }
    }
}
