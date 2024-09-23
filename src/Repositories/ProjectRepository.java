package Repositories;

import Config.DatabaseConnection;
import Entities.Project;
import Entities.Enum.ProjectStatus;
import Entities.Client;
import Repositories.Interfaces.GenericRepositoryInterface;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProjectRepository implements GenericRepositoryInterface<Project> {
    private final Connection connection;

    public ProjectRepository() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public Project save(Project project) {
        String query = "INSERT INTO projects (name, profitMargin, totalCost, projectStatus, client_id) VALUES (?, ?, ?, ?::project_status, ?) RETURNING id";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, project.getName());
            stmt.setDouble(2, project.getProfitMargin());
            stmt.setDouble(3, project.getTotalCost());
            stmt.setString(4, project.getProjectStatus().name());
            stmt.setLong(5, project.getClient().getId());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                project.setId(rs.getLong("id"));
            }
            return project;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Optional<Project> findById(Long id) {
        String query = "SELECT * FROM projects WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, id);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToProject(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public List<Project> findAll() {
        String query = "SELECT * FROM projects";
        List<Project> projects = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                projects.add(mapResultSetToProject(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return projects;
    }

    @Override
    public Project update(Project project) {
        String query = "UPDATE projects SET name = ?, profitMargin = ?, totalCost = ?, projectStatus = ?::project_status, client_id = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, project.getName());
            stmt.setDouble(2, project.getProfitMargin());
            stmt.setDouble(3, project.getTotalCost());
            stmt.setString(4, project.getProjectStatus().name());
            stmt.setLong(5, project.getClient().getId());
            stmt.setLong(6, project.getId());

            stmt.executeUpdate();
            return project;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean delete(Long id) {
        String query = "DELETE FROM projects WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Project mapResultSetToProject(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id");
        String name = rs.getString("name");
        double profitMargin = rs.getDouble("profitMargin");
        double totalCost = rs.getDouble("totalCost");
        ProjectStatus status = ProjectStatus.valueOf(rs.getString("projectStatus"));
        Long clientId = rs.getLong("client_id");
        ClientRepository clientRepo = new ClientRepository();
        Client client = clientRepo.findById(clientId).orElse(null);

        return new Project(name, profitMargin, totalCost, status, client);
    }

    public List<Project> findProjectsByClient(Long clientId) {
        String query = "SELECT * FROM projects WHERE client_id = ?";
        List<Project> projects = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, clientId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                // Assuming you have a Project constructor that takes ResultSet or a mapping method
                projects.add(mapResultSetToProject(rs));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return projects;
    }

}
