package pkcg.repository;

import pkcg.model.Station;
import pkcg.service.DatabaseService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StationRepository {
    private Connection connection;
    
    public StationRepository() {
        this.connection = DatabaseService.getInstance().getConnection();
        initTable();
    }
    
    private void initTable() {
        try {
            Statement stmt = connection.createStatement();
            stmt.execute("CREATE TABLE IF NOT EXISTS stations (" +
                        "name VARCHAR(100) PRIMARY KEY," +
                        "platform_count INT NOT NULL" +
                        ")");
        } catch (SQLException e) {
            // Silently handle error
        }
    }
    
    public void save(Station station) {
        try {
            // Check if the station already exists
            Optional<Station> existingStation = findByName(station.getName());
            if (existingStation.isPresent()) {
                // Station already exists - update instead
                update(station);
                return;
            }
            
            PreparedStatement pstmt = connection.prepareStatement(
                "INSERT INTO stations (name, platform_count) VALUES (?, ?)");
            
            pstmt.setString(1, station.getName());
            pstmt.setInt(2, station.getPlatforms().size());
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            // Silently handle error
        }
    }
    
    public List<Station> findAll() {
        List<Station> stations = new ArrayList<>();
        
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM stations");
            
            while (rs.next()) {
                String name = rs.getString("name");
                int platformCount = rs.getInt("platform_count");
                
                stations.add(new Station(name, platformCount));
            }
        } catch (SQLException e) {
            System.err.println("Error finding stations: " + e.getMessage());
        }
        
        return stations;
    }
    
    public Optional<Station> findByName(String name) {
        try {
            PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM stations WHERE name = ?");
            pstmt.setString(1, name);
            
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                int platformCount = rs.getInt("platform_count");
                return Optional.of(new Station(name, platformCount));
            }
        } catch (SQLException e) {
            System.err.println("Error finding station: " + e.getMessage());
        }
        
        return Optional.empty();
    }
    
    public boolean update(Station station) {
        try {
            PreparedStatement pstmt = connection.prepareStatement(
                "UPDATE stations SET platform_count = ? WHERE name = ?");
            
            pstmt.setInt(1, station.getPlatforms().size());
            pstmt.setString(2, station.getName());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating station: " + e.getMessage());
            return false;
        }
    }
    
    public boolean delete(String name) {
        try {
            PreparedStatement pstmt = connection.prepareStatement("DELETE FROM stations WHERE name = ?");
            pstmt.setString(1, name);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting station: " + e.getMessage());
            return false;
        }
    }
    
    public void clearAll() {
        if (!DatabaseService.getInstance().isConnected()) return;
        
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("DELETE FROM stations");
            System.out.println("All stations cleared from database");
        } catch (SQLException e) {
            System.err.println("Error clearing stations: " + e.getMessage());
        }
    }
}