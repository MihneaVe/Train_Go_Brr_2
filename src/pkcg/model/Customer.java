package pkcg.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a customer user in the system.
 * 
 * Customers can:
 * - View available routes and schedules
 * - Purchase tickets
 * - Make seat reservations
 * - View their ticket history
 * 
 * This class extends the base User class, adding customer-specific attributes
 * like full name, email, and tickets purchased.
 */
public class Customer extends User {
    private String fullName;
    private String email;
    private List<Ticket> tickets;
    
    /**
     * Constructs a new Customer with the specified credentials and personal information.
     * 
     * @param username The unique username for this customer
     * @param password The password for authentication
     * @param fullName The customer's full name
     * @param email The customer's email address
     */
    public Customer(String username, String password, String fullName, String email) {
        super(username, password);
        this.fullName = fullName;
        this.email = email;
        this.tickets = new ArrayList<>();
    }
    
    /**
     * Gets the customer's full name.
     * 
     * @return The customer's full name
     */
    public String getFullName() {
        return fullName;
    }
    
    /**
     * Gets the customer's email address.
     * 
     * @return The customer's email
     */
    public String getEmail() {
        return email;
    }
    
    /**
     * Gets the list of tickets purchased by this customer.
     * 
     * @return An unmodifiable list of the customer's tickets
     */
    public List<Ticket> getTickets() {
        return tickets;
    }
    
    /**
     * Adds a purchased ticket to the customer's ticket collection.
     * 
     * @param ticket The ticket to add
     */
    public void addTicket(Ticket ticket) {
        tickets.add(ticket);
    }
    
    /**
     * Returns a string representation of this Customer object.
     * 
     * @return A string containing the customer's username, full name, and email
     */
    @Override
    public String toString() {
        return "Customer{" +
                "username='" + getUsername() + '\'' +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}