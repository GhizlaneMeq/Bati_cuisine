package Repositories;

import Config.DatabaseConnection;
import Entities.Client;
import Repositories.Interfaces.ClientrepositoryInterface;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClientRepository implements ClientrepositoryInterface {
    private final Connection connection;

    public ClientRepository() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public Client save(Client client) {
        String query = "INSERT INTO clients (name, address, phone, isProfessional) VALUES (?, ?, ?, ?) RETURNING id";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, client.getName());
            stmt.setString(2, client.getAddress());
            stmt.setString(3, client.getPhone());
            stmt.setBoolean(4, client.isProfessional());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                client.setId(rs.getLong("id"));
            }
            return client;
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public Optional<Client> findById(Long id) {
        String query = "SELECT * FROM clients WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, id);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToClient(rs));
            }
            return Optional.empty();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

        return Optional.empty();
    }

    @Override
    public List<Client> findAll() {
        String query = "SELECT * FROM clients";
        List<Client> clients = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                clients.add(mapResultSetToClient(rs));
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return clients;
    }

    @Override
    public Client update(Client client) {
        String query = "UPDATE clients SET name = ?, address = ?, phone = ?, isProfessional = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, client.getName());
            stmt.setString(2, client.getAddress());
            stmt.setString(3, client.getPhone());
            stmt.setBoolean(4, client.isProfessional());
            stmt.setLong(5, client.getId());

            stmt.executeUpdate();
            return client;
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public boolean delete(Long id) {
        String query = "DELETE FROM clients WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return false;
    }
    @Override
    public Optional<Client> findByName(String name) {
        String query = "SELECT * FROM clients WHERE name = ?";
        try(PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1,name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToClient(rs));
            }
            return Optional.empty();

        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return Optional.empty();
    }

    private Client mapResultSetToClient(ResultSet rs) throws SQLException {
        long id = rs.getLong("id");
        String name = rs.getString("name");
        String address = rs.getString("address");
        String phone = rs.getString("phone");
        boolean isProfessional = rs.getBoolean("isProfessional");
        return new Client(id,name, address, phone, isProfessional);
    }


}
