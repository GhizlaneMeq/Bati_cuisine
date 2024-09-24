package Repositories;

import Config.DatabaseConnection;
import Entities.Labor;
import Entities.Project;
import Repositories.Interfaces.GenericRepositoryInterface;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LaborRepository implements GenericRepositoryInterface<Labor> {
    private final Connection connection;

    public LaborRepository() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public Labor save(Labor labor) {
        String query = "INSERT INTO labor (name, componentType, vatRate, project_id, hourlyRate, hoursWorked, workerProductivity) VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING id";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, labor.getName());
            stmt.setString(2, labor.getComponentType());
            stmt.setDouble(3, labor.getVatRate());
            stmt.setLong(4, labor.getProject().getId());
            stmt.setDouble(5, labor.getHourlyRate());
            stmt.setDouble(6, labor.getHoursWorked());
            stmt.setDouble(7, labor.getWorkerProductivity());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                labor.setId(rs.getLong("id"));
            }
            return labor;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Optional<Labor> findById(Long id) {
        String query = "SELECT * FROM labor WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToLabor(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public List<Labor> findAll() {
        String query = "SELECT * FROM labor";
        List<Labor> labors = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                labors.add(mapResultSetToLabor(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return labors;
    }

    @Override
    public Labor update(Labor labor) {
        String query = "UPDATE labor SET name = ?, componentType = ?, vatRate = ?, project_id = ?, hourlyRate = ?, hoursWorked = ?, workerProductivity = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, labor.getName());
            stmt.setString(2, labor.getComponentType());
            stmt.setDouble(3, labor.getVatRate());
            stmt.setLong(4, labor.getProject().getId());
            stmt.setDouble(5, labor.getHourlyRate());
            stmt.setDouble(6, labor.getHoursWorked());
            stmt.setDouble(7, labor.getWorkerProductivity());
            stmt.setLong(8, labor.getId());

            stmt.executeUpdate();
            return labor;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean delete(Long id) {
        String query = "DELETE FROM labor WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Labor> findByProjectId(Long projectId) {
        String query = "SELECT * FROM labor WHERE project_id = ?";
        List<Labor> labors = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, projectId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                labors.add(mapResultSetToLabor(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return labors;
    }

    private Labor mapResultSetToLabor(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id");
        String name = rs.getString("name");
        String componentType = rs.getString("componentType");
        double vatRate = rs.getDouble("vatRate");
        Long projectId = rs.getLong("project_id");
        double hourlyRate = rs.getDouble("hourlyRate");
        double hoursWorked = rs.getDouble("hoursWorked");
        double workerProductivity = rs.getDouble("workerProductivity");

        ProjectRepository projectRepo = new ProjectRepository();
        Project project = projectRepo.findById(projectId).orElse(null);

        return new Labor(id,name, componentType, vatRate, project, hourlyRate, hoursWorked, workerProductivity);
    }

    public List<Labor> findByName(String laborName) {
        String query = "SELECT * FROM labor WHERE name ILIKE ?";
        List<Labor> labors = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, "%" + laborName + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                labors.add(mapResultSetToLabor(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return labors;
    }
}
