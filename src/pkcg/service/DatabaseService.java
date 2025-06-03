package pkcg.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Service for managing database connections.
 * 
 * Implements the Singleton pattern to provide a single database connection throughout the application.
 * Handles connection establishment, validation, and graceful termination.
 */
public class DatabaseService {
    private static DatabaseService instance;
    private Connection connection;
    private boolean showLogs = false;
    
    // Database connection parameters
    private static final String URL = "jdbc:postgresql://localhost:5432/Proiect_PAO";
    private static final String USER = "mihnea2";
    private static final String PASSWORD = "mihnea2";
    
    /**
     * Private constructor to prevent direct instantiation.
     * Attempts to establish a database connection on creation.
     */
    private DatabaseService() {
        try {
            // Try to load the PostgreSQL JDBC driver explicitly
            Class.forName("org.postgresql.Driver");
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
            if (showLogs) System.out.println("Database connection established successfully.");
        } catch (ClassNotFoundException e) {
            // Silently log error, don't show to user
            logError("PostgreSQL JDBC Driver not found. Include it in your library path!");
        } catch (SQLException e) {
            // Silently log error, don't show to user
            logError("Database connection error: " + e.getMessage());
        }
    }
    
    /**
     * Logs an error message, respecting the logging configuration.
     * 
     * @param message The error message to log
     */
    private void logError(String message) {
        if (showLogs) {
            System.err.println(message);
        }
        // In a real app, you would log this to a file
    }
    
    /**
     * Gets the singleton instance of the DatabaseService.
     * Creates the instance if it doesn't already exist.
     * 
     * @return The DatabaseService instance
     */
    public static synchronized DatabaseService getInstance() {
        if (instance == null) {
            instance = new DatabaseService();
        }
        return instance;
    }
    
    /**
     * Gets the database connection.
     * May be null if connection failed during initialization.
     * 
     * @return The database connection
     */
    public Connection getConnection() {
        return connection;
    }
    
    /**
     * Closes the database connection when the application terminates.
     */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                if (showLogs) System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            // Silently log error
            logError("Error closing database connection: " + e.getMessage());
        }
    }
    
    /**
     * Checks whether a valid database connection is established.
     * 
     * @return true if connected, false otherwise
     */
    public boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
}