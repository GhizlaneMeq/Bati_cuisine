package Repositories;

import Config.DatabaseConnection;
import Entities.Material;
import Entities.Project;
import Repositories.Interfaces.GenericRepositoryInterface;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MaterialRepository implements GenericRepositoryInterface<Material> {
    private final Connection connection;

    public MaterialRepository() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public Material save(Material material) {
        String query = "INSERT INTO materials (name, componentType, vatRate, project_id, unitCost, quantity, transportCost, qualityCoefficient) VALUES (?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, material.getName());
            stmt.setString(2, material.getComponentType());
            stmt.setDouble(3, material.getVatRate());
            stmt.setLong(4, material.getProject().getId());
            stmt.setDouble(5, material.getUnitCost());
            stmt.setDouble(6, material.getQuantity());
            stmt.setDouble(7, material.getTransportCost());
            stmt.setDouble(8, material.getQualityCoefficient());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                material.setId(rs.getLong("id"));
            }
            return material;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Optional<Material> findById(Long id) {
        String query = "SELECT * FROM materials WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, id);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToMaterial(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public List<Material> findAll() {
        String query = "SELECT * FROM materials";
        List<Material> materials = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                materials.add(mapResultSetToMaterial(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return materials;
    }

    @Override
    public Material update(Material material) {
        String query = "UPDATE materials SET name = ?, componentType = ?, vatRate = ?, project_id = ?, unitCost = ?, quantity = ?, transportCost = ?, qualityCoefficient = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, material.getName());
            stmt.setString(2, material.getComponentType());
            stmt.setDouble(3, material.getVatRate());
            stmt.setLong(4, material.getProject().getId());
            stmt.setDouble(5, material.getUnitCost());
            stmt.setDouble(6, material.getQuantity());
            stmt.setDouble(7, material.getTransportCost());
            stmt.setDouble(8, material.getQualityCoefficient());
            stmt.setLong(9, material.getId());

            stmt.executeUpdate();
            return material;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean delete(Long id) {
        String query = "DELETE FROM materials WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Material> findByProjectId(Long projectId) {
        String query = "SELECT * FROM materials WHERE project_id = ?";
        List<Material> materials = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, projectId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                materials.add(mapResultSetToMaterial(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return materials;
    }

    public List<Material> findByName(String name) {
        String query = "SELECT * FROM materials WHERE name LIKE ?";
        List<Material> materials = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, "%" + name + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                materials.add(mapResultSetToMaterial(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return materials;
    }

    private Material mapResultSetToMaterial(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id");
        String name = rs.getString("name");
        String componentType = rs.getString("componentType");
        double vatRate = rs.getDouble("vatRate");
        Long projectId = rs.getLong("project_id");
        double unitCost = rs.getDouble("unitCost");
        double quantity = rs.getDouble("quantity");
        double transportCost = rs.getDouble("transportCost");
        double qualityCoefficient = rs.getDouble("qualityCoefficient");

        ProjectRepository projectRepo = new ProjectRepository();
        Project project = projectRepo.findById(projectId).orElse(null);

        return new Material(name, componentType, vatRate, project, unitCost, quantity, transportCost, qualityCoefficient);
    }
}
