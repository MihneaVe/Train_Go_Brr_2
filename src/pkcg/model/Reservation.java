package pkcg.model;

import java.util.UUID;

public class Reservation {
    private String id;
    private Customer customer;
    private Schedule schedule;
    private int seatNumber;
    private boolean confirmed;
    
    public Reservation(Customer customer, Schedule schedule, int seatNumber) {
        this.id = UUID.randomUUID().toString().substring(0, 8);
        this.customer = customer;
        this.schedule = schedule;
        this.seatNumber = seatNumber;
        this.confirmed = false;
    }
    
    public String getId() {
        return id;
    }
    
    public Customer getCustomer() {
        return customer;
    }
    
    public Schedule getSchedule() {
        return schedule;
    }
    
    public int getSeatNumber() {
        return seatNumber;
    }
    
    public boolean isConfirmed() {
        return confirmed;
    }
    
    public void confirm() {
        this.confirmed = true;
    }
    
    public void cancel() {
        this.confirmed = false;
    }
    
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