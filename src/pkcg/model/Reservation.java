package pkcg.model;

import java.util.UUID;

/**
 * Represents a seat reservation for a specific schedule.
 * 
 * Reservations allow customers to secure a specific seat on a train journey.
 * They are initially created in an unconfirmed state and must be confirmed
 * (typically through payment) to be finalized.
 */
public class Reservation {
    private String id;
    private Customer customer;
    private Schedule schedule;
    private int seatNumber;
    private boolean confirmed;
    
    /**
     * Constructs a new Reservation with the specified details.
     * By default, new reservations are not confirmed until payment is received.
     * 
     * @param customer The customer making the reservation
     * @param schedule The train schedule for the reservation
     * @param seatNumber The specific seat number being reserved
     */
    public Reservation(Customer customer, Schedule schedule, int seatNumber) {
        this.id = UUID.randomUUID().toString().substring(0, 8);
        this.customer = customer;
        this.schedule = schedule;
        this.seatNumber = seatNumber;
        this.confirmed = false;
    }
    
    /**
     * Gets the unique identifier for this reservation.
     * 
     * @return The reservation ID
     */
    public String getId() {
        return id;
    }
    
    /**
     * Gets the customer who made this reservation.
     * 
     * @return The customer
     */
    public Customer getCustomer() {
        return customer;
    }
    
    /**
     * Gets the schedule associated with this reservation.
     * 
     * @return The schedule
     */
    public Schedule getSchedule() {
        return schedule;
    }
    
    /**
     * Gets the reserved seat number.
     * 
     * @return The seat number
     */
    public int getSeatNumber() {
        return seatNumber;
    }
    
    /**
     * Checks if this reservation is confirmed.
     * 
     * @return true if confirmed, false otherwise
     */
    public boolean isConfirmed() {
        return confirmed;
    }
    
    /**
     * Confirms this reservation, typically after payment is received.
     */
    public void confirm() {
        this.confirmed = true;
    }
    
    /**
     * Cancels this reservation, setting it back to unconfirmed status.
     */
    public void cancel() {
        this.confirmed = false;
    }
    
    /**
     * Returns a string representation of this Reservation object.
     * 
     * @return A string containing the reservation details
     */
    @Override
    public String toString() {
        return "Reservation{" +
                "id='" + id + '\'' +
                ", customer=" + customer.getFullName() +
                ", schedule=" + schedule +
                ", seatNumber=" + seatNumber +
                ", confirmed=" + confirmed +
                '}';
    }
}