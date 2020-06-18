package nl.utwente.di.team26.Product.dao.Events;

import nl.utwente.di.team26.Exception.Exceptions.DatabaseException;
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
    public synchronized long create(Connection conn, EventMap valueObject) throws SQLException {

        String sql = "INSERT INTO EventMap (eventId, mapId) VALUES (?, ?) ";
        beginTransaction(conn, Connection.TRANSACTION_READ_COMMITTED);
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, valueObject.getEventId());
            stmt.setLong(2, valueObject.getMapId());

            int rowcount = databaseUpdate(stmt);
            if (rowcount == 0) {
                throw new DatabaseException("Object could not be created: " + valueObject);
            }
            endTransaction(conn);
            return rowcount;
        } catch (SQLException e) {
            conn.rollback();
            throw new DatabaseException(e);
        }
    }

    @Override
    public void save(Connection conn, EventMap valueObject) throws NotFoundException, SQLException {
        //Nothing to save.
    }

    public void delete(Connection conn, EventMap valueObject) throws NotFoundException, SQLException {

        String sql = "DELETE FROM EventMap WHERE (eventId = ? AND mapId = ? ) ";
        beginTransaction(conn, Connection.TRANSACTION_READ_COMMITTED);
        //only read data already committed, do not want to delete data that doesn't even exist.
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, valueObject.getEventId());
            stmt.setLong(2, valueObject.getMapId());

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
        beginTransaction(conn, Connection.TRANSACTION_READ_COMMITTED);
        String sql = "DELETE FROM EventMap";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            int rowcount = databaseUpdate(stmt);
            endTransaction(conn);
        } catch (SQLException e) {
            conn.rollback();
            throw new DatabaseException(e);
        }
    }

    public void deleteAllForEvent(Connection conn, long eventId) throws SQLException, NotFoundException {
        String sql = "DELETE FROM EventMap WHERE (eventId = ?) ";
        beginTransaction(conn, Connection.TRANSACTION_READ_COMMITTED);
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, eventId);
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

    public String getAllMapsFor(Connection conn, long eventId) throws NotFoundException, SQLException {
        String sql = "select getAllMapsForEvent(?);";
        beginTransaction(conn, Connection.TRANSACTION_SERIALIZABLE);
        //all get requests will have to be serializable, due to phantoms.
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, eventId);
            return getResultOfQuery(conn, stmt);
        }
    }

    public String allEventsFor(Connection conn, long mapId) throws SQLException, NotFoundException {
        String sql = "select getAllEventsForMap(?);";
        beginTransaction(conn, Connection.TRANSACTION_SERIALIZABLE);
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, mapId);
            return getResultOfQuery(conn, stmt);
        }
    }
}
