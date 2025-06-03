package pkcg.model;

/**
 * Represents an administrator user in the system.
 * 
 * Administrators have elevated privileges that allow them to:
 * - Manage stations, trains, routes and schedules
 * - View revenue reports
 * - Perform database management tasks
 * 
 * This class extends the base User class, inheriting authentication capabilities
 * while adding administrator-specific functionality.
 */
public class Admin extends User {
    
    /**
     * Constructs a new Admin with the specified credentials.
     * 
     * @param username The unique username for this administrator
     * @param password The password for authentication
     */
    public Admin(String username, String password) {
        super(username, password);
    }
    
    /**
     * Returns a string representation of this Admin object.
     * 
     * @return A string containing the username of this admin
     */
    @Override
    public String toString() {
        return "Admin{" +
                "username='" + getUsername() + '\'' +
                '}';
    }
}