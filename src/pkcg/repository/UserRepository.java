package pkcg.repository;

import pkcg.model.User;
import pkcg.model.Admin;
import pkcg.model.Customer;
import pkcg.service.DatabaseService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepository {
    private Connection connection;
    private boolean isConnected;
    
    public UserRepository() {
        DatabaseService dbService = DatabaseService.getInstance();
        this.connection = dbService.getConnection();
        this.isConnected = dbService.isConnected();
        
        if (isConnected) {
            initTable();
        }
    }
    
    private void initTable() {
        try {
            DatabaseMetaData meta = connection.getMetaData();
            ResultSet tables = meta.getTables(null, null, "users", null);
            
            if (!tables.next()) {
                // Table doesn't exist, create it
                Statement stmt = connection.createStatement();
                stmt.execute("CREATE TABLE IF NOT EXISTS users (" +
                            "username VARCHAR(50) PRIMARY KEY," +
                            "password VARCHAR(100) NOT NULL," +
                            "user_type VARCHAR(20) NOT NULL," +
                            "full_name VARCHAR(100)," +
                            "email VARCHAR(100)" +
                            ")");
            }
        } catch (SQLException e) {
            isConnected = false;
            System.err.println("Error initializing user table: " + e.getMessage());
        }
    }
    
    public void save(User user) {
        if (!isConnected) return;
        
        try {
            // First check if user already exists
            PreparedStatement checkStmt = connection.prepareStatement(
                "SELECT username FROM users WHERE username = ?");
            checkStmt.setString(1, user.getUsername());
            ResultSet rs = checkStmt.executeQuery();
            
            if (rs.next()) {
                // User exists, update instead
                update(user);
                return;
            }
            
            PreparedStatement pstmt = connection.prepareStatement(
                "INSERT INTO users (username, password, user_type, full_name, email) " +
                "VALUES (?, ?, ?, ?, ?)");
            
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            
            if (user instanceof Admin) {
                pstmt.setString(3, "ADMIN");
                pstmt.setString(4, null);
                pstmt.setString(5, null);
            } else if (user instanceof Customer) {
                Customer customer = (Customer) user;
                pstmt.setString(3, "CUSTOMER");
                pstmt.setString(4, customer.getFullName());
                pstmt.setString(5, customer.getEmail());
            }
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error saving user: " + e.getMessage());
        }
    }
    
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        
        if (!isConnected) return users;
        
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM users");
            
            while (rs.next()) {
                String username = rs.getString("username");
                String password = rs.getString("password");
                String userType = rs.getString("user_type");
                
                if ("ADMIN".equals(userType)) {
                    users.add(new Admin(username, password));
                } else if ("CUSTOMER".equals(userType)) {
                    String fullName = rs.getString("full_name");
                    String email = rs.getString("email");
                    users.add(new Customer(username, password, fullName, email));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding users: " + e.getMessage());
        }
        
        return users;
    }
    
    public Optional<User> findByUsername(String username) {
        if (!isConnected) return Optional.empty();
        
        try {
            PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM users WHERE username = ?");
            pstmt.setString(1, username);
            
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                String password = rs.getString("password");
                String userType = rs.getString("user_type");
                
                if ("ADMIN".equals(userType)) {
                    return Optional.of(new Admin(username, password));
                } else if ("CUSTOMER".equals(userType)) {
                    String fullName = rs.getString("full_name");
                    String email = rs.getString("email");
                    return Optional.of(new Customer(username, password, fullName, email));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding user: " + e.getMessage());
        }
        
        return Optional.empty();
    }
    
    public boolean update(User user) {
        if (!isConnected) return false;
        
        try {
            String sql;
            PreparedStatement pstmt;
            
            if (user instanceof Admin) {
                sql = "UPDATE users SET password = ? WHERE username = ?";
                pstmt = connection.prepareStatement(sql);
                pstmt.setString(1, user.getPassword());
                pstmt.setString(2, user.getUsername());
            } else if (user instanceof Customer) {
                Customer customer = (Customer) user;
                sql = "UPDATE users SET password = ?, full_name = ?, email = ? WHERE username = ?";
                pstmt = connection.prepareStatement(sql);
                pstmt.setString(1, user.getPassword());
                pstmt.setString(2, customer.getFullName());
                pstmt.setString(3, customer.getEmail());
                pstmt.setString(4, user.getUsername());
            } else {
                return false;
            }
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating user: " + e.getMessage());
            return false;
        }
    }
    
    public boolean delete(String username) {
        if (!isConnected) return false;
        
        try {
            PreparedStatement pstmt = connection.prepareStatement("DELETE FROM users WHERE username = ?");
            pstmt.setString(1, username);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting user: " + e.getMessage());
            return false;
        }
    }
    
    public void clearAll() {
        if (!isConnected) return;
        
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("DELETE FROM users");
            System.out.println("All users cleared from database");
        } catch (SQLException e) {
            System.err.println("Error clearing users: " + e.getMessage());
        }
    }
    
    public void clearAllExceptAdmin() {
        if (!isConnected) return;
        
        try {
            PreparedStatement pstmt = connection.prepareStatement("DELETE FROM users WHERE user_type != 'ADMIN'");
            int count = pstmt.executeUpdate();
            System.out.println(count + " customers cleared from database");
        } catch (SQLException e) {
            System.err.println("Error clearing customer accounts: " + e.getMessage());
        }
    }
    
    public boolean isConnected() {
        return isConnected;
    }
}