package nl.utwente.di.team26.Product.dao.Resources;

import nl.utwente.di.team26.CONSTANTS;
import nl.utwente.di.team26.Exception.Exceptions.NotFoundException;
import nl.utwente.di.team26.Product.dao.Dao;
import nl.utwente.di.team26.Product.dao.DaoInterface;
import nl.utwente.di.team26.Product.model.TypeOfResource.Drawing;
import nl.utwente.di.team26.Product.model.TypeOfResource.Material;
import nl.utwente.di.team26.Product.model.TypeOfResource.TypeOfResource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ResourceDao extends Dao implements DaoInterface<TypeOfResource> {

    @Override
    public long create(TypeOfResource resource) throws SQLException {
        if (resource instanceof Material) {
            return createMaterial((Material) resource);
        } else {
            return createDrawing((Drawing) resource);
        }
    }

    @Override
    public void save(TypeOfResource resource) throws NotFoundException, SQLException {
        if (resource instanceof Material) {
            saveMaterial((Material) resource);
        } else {
            saveDrawing((Drawing) resource);
        }
    }

    private void saveDrawing(Drawing resource) throws SQLException, NotFoundException {
        try(Connection conn = CONSTANTS.getConnection()) {
            String sql =
                    "UPDATE TypeOfResource SET name = ?, description = ?  WHERE resourceId = ?; " +
                            "UPDATE Drawing SET image = ? WHERE resourceId = ?;";
            beginTransaction(conn, Connection.TRANSACTION_READ_COMMITTED);
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, resource.getName());
                stmt.setString(2, resource.getDescription());
                stmt.setLong(3, resource.getResourceId());
                stmt.setString(4, resource.getImage());
                stmt.setLong(5, resource.getResourceId());

                databaseUpdate(conn, stmt);
            }
        }
    }

    private void saveMaterial(Material resource) throws SQLException, NotFoundException {
        try(Connection conn = CONSTANTS.getConnection()) {
            String sql =
                    "UPDATE TypeOfResource SET name = ?, description = ?  WHERE resourceId = ?; " +
                            "UPDATE Material SET image = ? WHERE resourceId = ?;";
            beginTransaction(conn, Connection.TRANSACTION_READ_COMMITTED);
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, resource.getName());
                stmt.setString(2, resource.getDescription());
                stmt.setLong(3, resource.getResourceId());
                stmt.setString(4, resource.getImage());
                stmt.setLong(5, resource.getResourceId());

                databaseUpdate(conn, stmt);
            }
        }
    }

    @Override
    public void delete(TypeOfResource resourceToDelete) throws SQLException, NotFoundException {
        try(Connection conn = CONSTANTS.getConnection()) {
            String sql = "DELETE FROM TypeOfResource WHERE (resourceId = ?)";
            beginTransaction(conn, Connection.TRANSACTION_READ_COMMITTED);
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, resourceToDelete.getResourceId());
                databaseUpdate(conn, stmt);
            }
        }
    }

    public void deleteAll() throws SQLException, NotFoundException {
        try(Connection conn = CONSTANTS.getConnection()) {
            String sql = "DELETE FROM TypeOfResource";
            beginTransaction(conn, Connection.TRANSACTION_READ_COMMITTED);
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                databaseUpdate(conn, stmt);
            }
        }
    }

    public String getAllResources() throws NotFoundException, SQLException {
        try(Connection conn = CONSTANTS.getConnection()) {
            String sql = "SELECT getAllResources()";
            beginTransaction(conn, Connection.TRANSACTION_SERIALIZABLE);
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                return getResultOfQuery(conn, stmt);
            }
        }
    }

    public String getResource(long resourceId) throws NotFoundException, SQLException {
        try(Connection conn = CONSTANTS.getConnection()) {
            String sql = "SELECT getResource(?)";
            beginTransaction(conn, Connection.TRANSACTION_SERIALIZABLE);
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, resourceId);
                return getResultOfQuery(conn, stmt);
            }
        }
    }

    private long createDrawing(Drawing drawingToAdd) throws SQLException {
        try(Connection conn = CONSTANTS.getConnection()) {
            String sql =
                    "WITH draw AS (INSERT INTO TypeOfResource(name, description) VALUES (?, ?) RETURNING resourceId)" +
                            "INSERT INTO Drawing(resourceId, image)" +
                            "select draw.resourceId, ? from draw;";
            beginTransaction(conn, Connection.TRANSACTION_READ_COMMITTED);
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, drawingToAdd.getName());
                stmt.setString(2, drawingToAdd.getDescription());
                stmt.setString(3, drawingToAdd.getImage());
                return executeCreate(conn, stmt);
            }
        }
    }

    private long createMaterial(Material materialToAdd) throws SQLException {
        try(Connection conn = CONSTANTS.getConnection()) {
            String sql =
                    "WITH newMaterial AS (INSERT INTO TypeOfResource(name, description) VALUES (?, ?) RETURNING resourceId)" +
                            "INSERT INTO Materials(resourceId, image)" +
                            "select newMaterial.resourceId, ? from newMaterial;";
            beginTransaction(conn, Connection.TRANSACTION_READ_COMMITTED);
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, materialToAdd.getName());
                stmt.setString(2, materialToAdd.getDescription());
                stmt.setString(3, materialToAdd.getImage());
                return executeCreate(conn, stmt);
            }
        }
    }

    public String getAllMaterials() throws NotFoundException, SQLException {
        try(Connection conn = CONSTANTS.getConnection()) {
            String sql = "SELECT getAllMaterials()";
            beginTransaction(conn, Connection.TRANSACTION_SERIALIZABLE);
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                return getResultOfQuery(conn, stmt);
            }
        }
    }

    public String getAllDrawings() throws NotFoundException, SQLException {
        try(Connection conn = CONSTANTS.getConnection()) {
            String sql = "SELECT getAllDrawings()";
            beginTransaction(conn, Connection.TRANSACTION_SERIALIZABLE);
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                return getResultOfQuery(conn, stmt);
            }
        }
    }
}
