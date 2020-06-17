package nl.utwente.di.team26.Product.dao.Resources;

import nl.utwente.di.team26.Exceptions.NotFoundException;
import nl.utwente.di.team26.Product.dao.Dao;
import nl.utwente.di.team26.Product.dao.DaoInterface;
import nl.utwente.di.team26.Product.model.TypeOfResource.Drawing;
import nl.utwente.di.team26.Product.model.TypeOfResource.Material;
import nl.utwente.di.team26.Product.model.TypeOfResource.TypeOfResource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ResourceDao extends Dao implements DaoInterface<TypeOfResource> {

    @Override
    public long create(Connection conn, TypeOfResource resource) throws SQLException {
        if (resource instanceof Material) {
            return createMaterial(conn, (Material) resource);
        } else {
            return createDrawing(conn, (Drawing) resource);
        }
    }

    @Override
    public void save(Connection conn, TypeOfResource resource) throws NotFoundException, SQLException {
        if (resource instanceof Material) {
            saveMaterial(conn, (Material) resource);
        } else {
            saveDrawing(conn, (Drawing) resource);
        }
    }

    private void saveDrawing(Connection conn, Drawing resource) throws SQLException, NotFoundException {
        String sql =
                "UPDATE TypeOfResource SET name = ?, description = ?  WHERE resourceId = ?; " +
                "UPDATE Drawing SET image = ? WHERE resourceId = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, resource.getName());
            stmt.setString(2, resource.getDescription());
            stmt.setLong(3, resource.getResourceId());
            stmt.setString(4, resource.getImage());
            stmt.setLong(5, resource.getResourceId());

            int rowCount = databaseUpdate(stmt);
            if (rowCount == 0) {
                throw new NotFoundException("Resource with Id: " + resource.getResourceId() + " could not be found");
            }
        }
    }

    private void saveMaterial(Connection conn, Material resource) throws SQLException, NotFoundException {
        String sql =
                "UPDATE TypeOfResource SET name = ?, description = ?  WHERE resourceId = ?; " +
                        "UPDATE Material SET image = ? WHERE resourceId = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, resource.getName());
            stmt.setString(2, resource.getDescription());
            stmt.setLong(3, resource.getResourceId());
            stmt.setString(4, resource.getImage());
            stmt.setLong(5, resource.getResourceId());

            int rowCount = databaseUpdate(stmt);
            if (rowCount == 0) {
                throw new NotFoundException("Resource with Id: " + resource.getResourceId() + " could not be found");
            }
        }
    }

    @Override
    public void delete(Connection conn, TypeOfResource resourceToDelete) throws SQLException, NotFoundException {
        String sql = "DELETE FROM TypeOfResource WHERE (resourceId = ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, resourceToDelete.getResourceId());
            int rowcount = databaseUpdate(stmt);
            if (rowcount == 0) {
                throw new NotFoundException("Resource with Id: " + resourceToDelete.getResourceId() + "not found!");
            }
        }
    }

    public void deleteAll(Connection conn) throws SQLException {
        String sql = "DELETE FROM TypeOfResource";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            int rowcount = databaseUpdate(stmt);
        }
    }

    public String getAllResources(Connection conn) throws NotFoundException, SQLException {
        String sql = "SELECT getAllResources()";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            return getResultOfQuery(stmt);
        }
    }

    public String getResource(Connection conn, long resourceId) throws NotFoundException, SQLException {
        String sql = "SELECT getResource(?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, resourceId);
            return getResultOfQuery(stmt);
        }
    }

    private long createDrawing(Connection conn, Drawing drawingToAdd) throws SQLException {
        String sql =
                "WITH draw AS (INSERT INTO TypeOfResource(name, description) VALUES (?, ?) RETURNING resourceId)" +
                "INSERT INTO Drawing(resourceId, image)" +
                "select draw.resourceId, ? from draw;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, drawingToAdd.getName());
            stmt.setString(2, drawingToAdd.getDescription());
            stmt.setString(3, drawingToAdd.getImage());
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                return resultSet.getLong(1);
            } else {
                throw new SQLException("Problem adding the resource");
            }
        }
    }

    private long createMaterial(Connection conn, Material materialToAdd) throws SQLException {
        String sql =
                "WITH newMaterial AS (INSERT INTO TypeOfResource(name, description) VALUES (?, ?) RETURNING resourceId)" +
                        "INSERT INTO Materials(resourceId, image)" +
                        "select newMaterial.resourceId, ? from newMaterial;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, materialToAdd.getName());
            stmt.setString(2, materialToAdd.getDescription());
            stmt.setString(3, materialToAdd.getImage());
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                return resultSet.getLong(1);
            } else {
                throw new SQLException("Problem adding the resource");
            }
        }
    }

    public String getAllMaterials(Connection conn) throws NotFoundException, SQLException {
        String sql = "SELECT getAllMaterials()";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            return getResultOfQuery(stmt);
        }
    }

    public String getAllDrawings(Connection conn) throws NotFoundException, SQLException {
        String sql = "SELECT getAllDrawings()";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            return getResultOfQuery(stmt);
        }
    }
}