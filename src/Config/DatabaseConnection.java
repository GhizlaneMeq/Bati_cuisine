package Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static DatabaseConnection instance; // Singleton instance
    private Connection connection; // Connection object
    private String url = "jdbc:postgresql://localhost:5432/postgres"; // Database URL
    private String user = "ghizlane"; // Database username
    private String password = ""; // Database password

    // Private constructor to prevent instantiation
    private DatabaseConnection() {
        try {
            // Load PostgreSQL JDBC Driver (optional for newer JDBC versions)
            Class.forName("org.postgresql.Driver");
            // Establish the connection
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            System.out.println("Connection to the database failed: " + e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL JDBC Driver not found.");
            e.printStackTrace();
        }
    }

    // Get the singleton instance
    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    // Get the connection
    public Connection getConnection() {
        return connection;
    }
}
