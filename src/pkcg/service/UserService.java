package pkcg.service;

import pkcg.model.User;
import pkcg.model.Admin;
import pkcg.model.Customer;
import pkcg.repository.UserRepository;
import pkcg.util.ValidationUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service class that manages user authentication, registration, and session management.
 * 
 * This service maintains an in-memory cache of users for faster access and
 * provides methods for user authentication and authorization. It also handles
 * the creation of default admin accounts and synchronization with the database.
 */
public class UserService {
    private Map<String, User> users;           // In-memory user cache
    private User currentUser;                 // Currently logged-in user
    private UserRepository userRepository;    // Data access layer for users
    private boolean debug = false;            // Debug flag for logging
    
    /**
     * Constructs a new UserService.
     * Initializes in-memory user cache, creates default admin account,
     * and loads users from the database if available.
     */
    public UserService() {
        this.users = new HashMap<>();
        this.currentUser = null;
        this.userRepository = new UserRepository();
        
        // Always ensure default admin account exists in memory
        registerAdmin("admin", "password");
        
        // Ensure admin exists in database if connection is available
        if (userRepository.isConnected()) {
            if (userRepository.findByUsername("admin").isEmpty()) {
                userRepository.save(new Admin("admin", "password"));
            }
        }
        
        // Load all users from database to in-memory cache
        loadUsersFromDatabase();
    }
    
    /**
     * Loads all users from the database into the in-memory cache.
     * Only executes if database connection is available.
     */
    private void loadUsersFromDatabase() {
        if (userRepository.isConnected()) {
            List<User> dbUsers = userRepository.findAll();
            for (User user : dbUsers) {
                users.put(user.getUsername(), user);
            }
            if (debug) System.out.println("Loaded " + dbUsers.size() + " users from database");
        }
    }
    
    /**
     * Registers a new administrator account.
     * 
     * @param username The admin username
     * @param password The admin password
     */
    public void registerAdmin(String username, String password) {
        users.put(username, new Admin(username, password));
        if (debug) System.out.println("Registered admin: " + username);
    }
    
    /**
     * Registers a new customer with validation.
     * 
     * @param username Customer's username
     * @param password Customer's password
     * @param fullName Customer's full name
     * @param email Customer's email address
     * @return true if registration successful, false otherwise
     */
    public boolean registerCustomer(String username, String password, String fullName, String email) {
        // Check if username already exists
        if (users.containsKey(username)) {
            return false;
        }
        
        // Validate password and email
        if (!ValidationUtil.isValidPassword(password) || !ValidationUtil.isValidEmail(email)) {
            return false;
        }
        
        // Register the customer
        users.put(username, new Customer(username, password, fullName, email));
        return true;
    }
    
    /**
     * Authenticates a user with the provided credentials.
     * First checks the in-memory cache, then falls back to database if not found.
     * 
     * @param username The username to authenticate
     * @param password The password to verify
     * @return true if authentication successful, false otherwise
     */
    public boolean login(String username, String password) {
        if (debug) System.out.println("Login attempt: " + username);
        
        // First check if the user is loaded in memory
        User user = users.get(username);
        
        if (user != null) {
            if (debug) System.out.println("User found in memory: " + username);
        } else {
            if (debug) System.out.println("User not found in memory: " + username);
        }
        
        // If not in memory, try to find in database
        if (user == null && userRepository.isConnected()) {
            if (debug) System.out.println("Checking database for user: " + username);
            Optional<User> dbUser = userRepository.findByUsername(username);
            if (dbUser.isPresent()) {
                user = dbUser.get();
                users.put(username, user); // Add to in-memory cache
                if (debug) System.out.println("User loaded from database: " + username);
            }
        }
        
        // Authenticate the user
        if (user != null && user.authenticate(password)) {
            currentUser = user;
            if (debug) System.out.println("Authentication successful for " + username);
            return true;
        }
        
        if (debug) System.out.println("Authentication failed for " + username);
        return false;
    }
    
    /**
     * Logs out the current user.
     */
    public void logout() {
        currentUser = null;
    }
    
    /**
     * Gets the currently logged-in user.
     * 
     * @return The current User object
     */
    public User getCurrentUser() {
        return currentUser;
    }
    
    /**
     * Checks if any user is currently logged in.
     * 
     * @return true if a user is logged in, false otherwise
     */
    public boolean isLoggedIn() {
        return currentUser != null;
    }
    
    /**
     * Checks if the current user has administrator privileges.
     * 
     * @return true if current user is an admin, false otherwise
     */
    public boolean isAdmin() {
        return isLoggedIn() && currentUser instanceof Admin;
    }
}