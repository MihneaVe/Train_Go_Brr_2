package pkcg.repository;

import pkcg.model.Train;
import pkcg.service.DatabaseService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TrainRepository {
    private Connection connection;
    
    public TrainRepository() {
        this.connection = DatabaseService.getInstance().getConnection();
        initTable();
    }
    
    private void initTable() {
        try {
            Statement stmt = connection.createStatement();
            stmt.execute("CREATE TABLE IF NOT EXISTS trains (" +
                        "number VARCHAR(20) PRIMARY KEY," +
                        "type VARCHAR(50) NOT NULL," +
                        "capacity INT NOT NULL" +
                        ")");
        } catch (SQLException e) {
            System.err.println("Error creating trains table: " + e.getMessage());
        }
    }
    
    public void save(Train train) {
        try {
            PreparedStatement pstmt = connection.prepareStatement(
                "INSERT INTO trains (number, type, capacity) VALUES (?, ?, ?)");
            
            pstmt.setString(1, train.getNumber());
            pstmt.setString(2, train.getType());
            pstmt.setInt(3, train.getCapacity());
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error saving train: " + e.getMessage());
        }
    }
    
    public List<Train> findAll() {
        List<Train> trains = new ArrayList<>();
        
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM trains");
            
            while (rs.next()) {
                String number = rs.getString("number");
                String type = rs.getString("type");
                int capacity = rs.getInt("capacity");
                
                trains.add(new Train(number, type, capacity));
            }
        } catch (SQLException e) {
            System.err.println("Error finding trains: " + e.getMessage());
        }
        
        return trains;
    }
    
    public Optional<Train> findByNumber(String number) {
        try {
            PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM trains WHERE number = ?");
            pstmt.setString(1, number);
            
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                String type = rs.getString("type");
                int capacity = rs.getInt("capacity");
                return Optional.of(new Train(number, type, capacity));
            }
        } catch (SQLException e) {
            System.err.println("Error finding train: " + e.getMessage());
        }
        
        return Optional.empty();
    }
    
    public boolean update(Train train) {
        try {
            PreparedStatement pstmt = connection.prepareStatement(
                "UPDATE trains SET type = ?, capacity = ? WHERE number = ?");
            
            pstmt.setString(1, train.getType());
            pstmt.setInt(2, train.getCapacity());
            pstmt.setString(3, train.getNumber());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating train: " + e.getMessage());
            return false;
        }
    }
    
    public boolean delete(String number) {
        try {
            PreparedStatement pstmt = connection.prepareStatement("DELETE FROM trains WHERE number = ?");
            pstmt.setString(1, number);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting train: " + e.getMessage());
            return false;
        }
    }
    
    public void clearAll() {
        if (!DatabaseService.getInstance().isConnected()) return;
        
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("DELETE FROM trains");
            System.out.println("All trains cleared from database");
        } catch (SQLException e) {
            System.err.println("Error clearing trains: " + e.getMessage());
        }
    }
}