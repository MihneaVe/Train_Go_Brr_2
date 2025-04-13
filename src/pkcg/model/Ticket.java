package pkcg.model;

import java.util.UUID;

public class Ticket {
    private String id;
    private Customer customer;
    private Schedule schedule;
    private double price;
    private boolean isFirstClass;
    
    public Ticket(Customer customer, Schedule schedule, boolean isFirstClass) {
        this.id = UUID.randomUUID().toString().substring(0, 8);
        this.customer = customer;
        this.schedule = schedule;
        this.isFirstClass = isFirstClass;
        
        // Calculate price
        this.price = calculatePrice();
    }
    
    private double calculatePrice() {
        double basePrice = schedule.getRoute().getBasePrice();
        return isFirstClass ? basePrice * 1.5 : basePrice;
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
    
    public double getPrice() {
        return price;
    }
    
    public boolean isFirstClass() {
        return isFirstClass;
    }
    
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