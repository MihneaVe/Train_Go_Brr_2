package pkcg.model;

import java.util.UUID;

/**
 * Represents a ticket purchased by a customer for a specific train schedule.
 * 
 * A ticket grants a customer the right to travel on a particular train journey.
 * Tickets have a unique ID, can be either first class or standard class,
 * and have a price calculated based on the route's base price and ticket class.
 */
public class Ticket {
    private String id;
    private Customer customer;
    private Schedule schedule;
    private double price;
    private boolean isFirstClass;
    
    /**
     * Constructs a new Ticket with the specified details.
     * Automatically calculates the price based on the route's base price and ticket class.
     * 
     * @param customer The customer who purchased this ticket
     * @param schedule The train schedule this ticket is valid for
     * @param isFirstClass Whether this is a first-class ticket
     */
    public Ticket(Customer customer, Schedule schedule, boolean isFirstClass) {
        this.id = UUID.randomUUID().toString().substring(0, 8);
        this.customer = customer;
        this.schedule = schedule;
        this.isFirstClass = isFirstClass;
        
        // Calculate price based on route base price and ticket class
        this.price = calculatePrice();
    }
    
    /**
     * Calculates the ticket price based on the route's base price and the ticket class.
     * First-class tickets cost 50% more than standard tickets.
     * 
     * @return The calculated ticket price in RON
     */
    private double calculatePrice() {
        double basePrice = schedule.getRoute().getBasePrice();
        return isFirstClass ? basePrice * 1.5 : basePrice;
    }
    
    /**
     * Gets the unique identifier for this ticket.
     * 
     * @return The ticket ID
     */
    public String getId() {
        return id;
    }
    
    /**
     * Gets the customer who purchased this ticket.
     * 
     * @return The customer
     */
    public Customer getCustomer() {
        return customer;
    }
    
    /**
     * Gets the schedule this ticket is valid for.
     * 
     * @return The schedule
     */
    public Schedule getSchedule() {
        return schedule;
    }
    
    /**
     * Gets the price paid for this ticket.
     * 
     * @return The price in RON
     */
    public double getPrice() {
        return price;
    }
    
    /**
     * Checks if this is a first-class ticket.
     * 
     * @return true if first class, false otherwise (standard class)
     */
    public boolean isFirstClass() {
        return isFirstClass;
    }
    
    /**
     * Returns a string representation of this Ticket object.
     * 
     * @return A string containing the ticket details
     */
    @Override
    public String toString() {
        return "Ticket{" +
                "id='" + id + '\'' +
                ", customer=" + customer.getFullName() +
                ", from=" + schedule.getRoute().getOrigin().getName() +
                ", to=" + schedule.getRoute().getDestination().getName() +
                ", departure=" + schedule.getDepartureTime() +
                ", price=" + price +
                ", firstClass=" + isFirstClass +
                '}';
    }
}