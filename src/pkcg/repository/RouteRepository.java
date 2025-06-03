package pkcg.repository;

import pkcg.model.Route;
import pkcg.model.Station;
import pkcg.service.DatabaseService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Repository class for managing Route entities in the database.
 * Handles CRUD operations for routes between stations.
 * 
 * Routes represent connections between two stations with associated pricing.
 * This class manages the persistence of routes and enforces referential integrity
 * with the stations table.
 */
public class RouteRepository {
    private Connection connection;
    private StationRepository stationRepository;
    
    /**
     * Constructs a new RouteRepository instance, initializing the database connection
     * and creating the routes table if it doesn't exist.
     */
    public RouteRepository() {
        this.connection = DatabaseService.getInstance().getConnection();
        this.stationRepository = new StationRepository();
        initTable();
    }
    
    /**
     * Initializes the routes table in the database if it doesn't already exist.
     * Creates foreign key constraints to maintain referential integrity with stations.
     */
    private void initTable() {
        try {
            Statement stmt = connection.createStatement();
            stmt.execute("CREATE TABLE IF NOT EXISTS routes (" +
                        "id SERIAL PRIMARY KEY," +
                        "origin_station VARCHAR(100) NOT NULL," +
                        "destination_station VARCHAR(100) NOT NULL," +
                        "base_price DECIMAL(10, 2) NOT NULL," +
                        "CONSTRAINT fk_origin FOREIGN KEY (origin_station) REFERENCES stations(name)," +
                        "CONSTRAINT fk_destination FOREIGN KEY (destination_station) REFERENCES stations(name)," +
                        "CONSTRAINT unique_route UNIQUE (origin_station, destination_station)" +
                        ")");
        } catch (SQLException e) {
            // Silently handle error to prevent exposing database errors to end users
        }
    }
    
    /**
     * Saves a new route to the database or updates it if it already exists.
     * Also ensures that the origin and destination stations exist in the database.
     *
     * @param route The Route object to be saved
     */
    public void save(Route route) {
        try {
            // Ensure stations exist before saving the route
            Optional<Station> existingOrigin = stationRepository.findByName(route.getOrigin().getName());
            Optional<Station> existingDestination = stationRepository.findByName(route.getDestination().getName());
            
            // Save stations if they don't already exist
            if (existingOrigin.isEmpty()) {
                try {
                    stationRepository.save(route.getOrigin());
                } catch (Exception e) {
                    // Silently handle station already exists error
                }
            }
            
            if (existingDestination.isEmpty()) {
                try {
                    stationRepository.save(route.getDestination());
                } catch (Exception e) {
                    // Silently handle station already exists error
                }
            }
            
            // Save the route with a connection to existing stations
            PreparedStatement pstmt = connection.prepareStatement(
                "INSERT INTO routes (origin_station, destination_station, base_price) VALUES (?, ?, ?)");
            
            pstmt.setString(1, route.getOrigin().getName());
            pstmt.setString(2, route.getDestination().getName());
            pstmt.setDouble(3, route.getBasePrice());
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            // Silently handle database errors for better user experience
        }
    }
    
    /**
     * Retrieves all routes from the database.
     * Each route includes references to its origin and destination stations.
     *
     * @return List of all routes in the database
     */
    public List<Route> findAll() {
        List<Route> routes = new ArrayList<>();
        
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM routes");
            
            while (rs.next()) {
                String originName = rs.getString("origin_station");
                String destinationName = rs.getString("destination_station");
                double basePrice = rs.getDouble("base_price");
                
                // Find the actual station objects to maintain object relationships
                Station origin = stationRepository.findByName(originName).orElse(null);
                Station destination = stationRepository.findByName(destinationName).orElse(null);
                
                if (origin != null && destination != null) {
                    routes.add(new Route(origin, destination, basePrice));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding routes: " + e.getMessage());
        }
        
        return routes;
    }
    
    /**
     * Finds a route between two specific stations by name.
     *
     * @param originName The name of the origin station
     * @param destName The name of the destination station
     * @return Optional containing the route if found, empty otherwise
     */
    public Optional<Route> findByStations(String originName, String destName) {
        try {
            if (!DatabaseService.getInstance().isConnected()) {
                return Optional.empty();
            }
            
            PreparedStatement pstmt = connection.prepareStatement(
                "SELECT * FROM routes WHERE origin_station = ? AND destination_station = ?");
            
            pstmt.setString(1, originName);
            pstmt.setString(2, destName);
            
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Optional<Station> origin = stationRepository.findByName(originName);
                Optional<Station> destination = stationRepository.findByName(destName);
                
                if (origin.isPresent() && destination.isPresent()) {
                    double basePrice = rs.getDouble("base_price");
                    Route route = new Route(origin.get(), destination.get(), basePrice);
                    return Optional.of(route);
                }
            }
        } catch (SQLException e) {
            // Silently handle exception
        }
        
        return Optional.empty();
    }
    
    /**
     * Updates the price of an existing route.
     *
     * @param route The route to update
     * @param newPrice The new base price for the route
     * @return true if update was successful, false otherwise
     */
    public boolean updatePrice(Route route, double newPrice) {
        try {
            PreparedStatement pstmt = connection.prepareStatement(
                "UPDATE routes SET base_price = ? WHERE origin_station = ? AND destination_station = ?");
            
            pstmt.setDouble(1, newPrice);
            pstmt.setString(2, route.getOrigin().getName());
            pstmt.setString(3, route.getDestination().getName());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating route price: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Deletes a route from the database.
     *
     * @param route The route to delete
     * @return true if deletion was successful, false otherwise
     */
    public boolean delete(Route route) {
        try {
            PreparedStatement pstmt = connection.prepareStatement(
                "DELETE FROM routes WHERE origin_station = ? AND destination_station = ?");
            
            pstmt.setString(1, route.getOrigin().getName());
            pstmt.setString(2, route.getDestination().getName());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting route: " + e.getMessage());
            return false;
        }
    }

    /**
     * Removes all routes from the database.
     * Used during database reset operations.
     */
    public void clearAll() {
        try {
            if (!DatabaseService.getInstance().isConnected()) {
                return;
            }
            
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("DELETE FROM routes");
        } catch (SQLException e) {
            // Silently handle error
        }
    }
}