package nl.utwente.di.team26.Product.dao.Events;

import nl.utwente.di.team26.Exceptions.NotFoundException;
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
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, valueObject.getEventId());
            stmt.setLong(2, valueObject.getMapId());

            int rowcount = databaseUpdate(stmt);
            if (rowcount != 1) {
                throw new SQLException("PrimaryKey Error when updating DB!");
            }
            return rowcount;
        }

    }

    @Override
    public void save(Connection conn, EventMap valueObject) throws NotFoundException, SQLException {
        //Nothing to save.
    }

    public void delete(Connection conn, EventMap valueObject) throws NotFoundException, SQLException {

        String sql = "DELETE FROM EventMap WHERE (eventId = ? AND mapId = ? ) ";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, valueObject.getEventId());
            stmt.setLong(2, valueObject.getMapId());

            int rowcount = databaseUpdate(stmt);
            if (rowcount == 0) {
                //System.out.println("Object could not be deleted (PrimaryKey not found)");
                throw new NotFoundException("Object could not be deleted! (PrimaryKey not found)");
            }
        }
    }

    public void deleteAll(Connection conn) throws SQLException {

        String sql = "DELETE FROM EventMap";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            int rowcount = databaseUpdate(stmt);
        }
    }

    public void deleteAllForEvent(Connection conn, long eventId) throws SQLException, NotFoundException {
        String sql = "DELETE FROM EventMap WHERE (eventId = ?) ";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, eventId);
            int rowcount = databaseUpdate(stmt);
            if (rowcount == 0) {
                //System.out.println("Object could not be deleted (PrimaryKey not found)");
                throw new NotFoundException("Object could not be deleted! (PrimaryKey not found)");
            }
        }
    }

    public String getAllMapsFor(Connection conn, long eventId) throws NotFoundException, SQLException {
        String sql = "select getAllMapsForEvent(?);";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, eventId);
            return getResultOfQuery(stmt);
        }
    }

    public String allEventsFor(Connection conn, int mapId) throws SQLException, NotFoundException {
        String sql = "select getAllEventsForMap(?);";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, mapId);
            return getResultOfQuery(stmt);
        }
    }
}
