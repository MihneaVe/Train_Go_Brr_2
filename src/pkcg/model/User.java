package pkcg.model;

/**
 * Abstract base class representing a user in the railway management system.
 * 
 * This class defines common attributes and behavior for all user types,
 * such as authentication and identity. Specific user types (Admin, Customer)
 * extend this class to add specialized functionality.
 * 
 * In a production system, passwords would be stored as secure hashes rather thanplaintext.
 */
public abstract class User {
    private String username;
    private String password; // In a real system, this would be hashed
    
    /**
     * Constructs a new User with the specified credentials.
     * 
     * @param username The unique username for this user
     * @param password The password for authentication
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    /**
     * Gets the username of this user.
     * 
     * @return The username
     */
    public String getUsername() {
        return username;
    }
    
    /**
     * Gets the password of this user.
     * In a production system, this method would be removed or restricted
     * to prevent direct access to password values.
     * 
     * @return The password
     */
    public String getPassword() {
        return password; 
    }
    
    /**
     * Updates the user's password.
     * 
     * @param password The new password
     */
    public void setPassword(String password) {
        this.password = password;
    }
    
    /**
     * Authenticates a user by comparing the provided password with the stored password.
     * In a production system, this would compare password hashes instead.
     * 
     * @param password The password to verify
     * @return true if authentication succeeds, false otherwise
     */
    public boolean authenticate(String password) {
        // In a real system, you would compare hashes instead
        System.out.println("Authenticating user: " + username);
        System.out.println("Provided password: " + password);
        System.out.println("Stored password: " + this.password);
        boolean matches = this.password.equals(password);
        System.out.println("Passwords match: " + matches);
        return matches;
    }
    
    /**
     * Compares this user with another object for equality.
     * Users are considered equal if they have the same username.
     * 
     * @param o The object to compare with
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass().getSuperclass() != o.getClass().getSuperclass()) return false;
        User user = (User) o;
        return username.equals(user.username);
    }
    
    /**
     * Returns a hash code value for this user.
     * 
     * @return A hash code based on the username
     */
    @Override
    public int hashCode() {
        return username.hashCode();
    }
    
    /**
     * Returns a string representation of this User object.
     * 
     * @return A string containing the username
     */
    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                '}';
    }
}