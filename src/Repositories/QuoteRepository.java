package Repositories;

import Config.DatabaseConnection;
import Entities.Quote;
import Entities.Project;
import Repositories.Interfaces.GenericRepositoryInterface;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class QuoteRepository implements GenericRepositoryInterface<Quote> {
    private final Connection connection;

    public QuoteRepository() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public Quote save(Quote quote) {
        String query = "INSERT INTO quotes (estimatedAmount, issueDate, validityDate, isAccepted, project_id) VALUES (?, ?, ?, ?, ?) RETURNING id";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setDouble(1, quote.getEstimatedAmount());
            stmt.setDate(2, Date.valueOf(quote.getIssueDate()));
            stmt.setDate(3, Date.valueOf(quote.getValidityDate()));
            stmt.setBoolean(4, quote.isAccepted());
            stmt.setLong(5, quote.getProject().getId());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                quote.setId(rs.getLong("id"));
            }
            return quote;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Optional<Quote> findById(Long id) {
        String query = "SELECT * FROM quotes WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, id);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToQuote(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public List<Quote> findAll() {
        String query = "SELECT * FROM quotes";
        List<Quote> quotes = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                quotes.add(mapResultSetToQuote(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return quotes;
    }

    @Override
    public Quote update(Quote quote) {
        String query = "UPDATE quotes SET estimatedAmount = ?, issueDate = ?, validityDate = ?, isAccepted = ?, project_id = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setDouble(1, quote.getEstimatedAmount());
            stmt.setDate(2, Date.valueOf(quote.getIssueDate()));
            stmt.setDate(3, Date.valueOf(quote.getValidityDate()));
            stmt.setBoolean(4, quote.isAccepted());
            stmt.setLong(5, quote.getProject().getId());
            stmt.setLong(6, quote.getId());

            stmt.executeUpdate();
            return quote;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean delete(Long id) {
        String query = "DELETE FROM quotes WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Quote mapResultSetToQuote(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id");
        double estimatedAmount = rs.getDouble("estimatedAmount");
        LocalDate issueDate = rs.getDate("issueDate").toLocalDate();
        LocalDate validityDate = rs.getDate("validityDate").toLocalDate();
        boolean isAccepted = rs.getBoolean("isAccepted");
        Long projectId = rs.getLong("project_id");

        ProjectRepository projectRepo = new ProjectRepository();
        Project project = projectRepo.findById(projectId).orElse(null);

        return new Quote(id,estimatedAmount, issueDate, validityDate, isAccepted, project);
    }
}
