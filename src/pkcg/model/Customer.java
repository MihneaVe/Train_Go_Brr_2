package pkcg.model;

import java.util.ArrayList;
import java.util.List;

public class Customer extends User {
    private String fullName;
    private String email;
    private List<Ticket> tickets;
    
    public Customer(String username, String password, String fullName, String email) {
        super(username, password);
        this.fullName = fullName;
        this.email = email;
        this.tickets = new ArrayList<>();
    }
    
    public String getFullName() {
        return fullName;
    }
    
    public String getEmail() {
        return email;
    }
    
    public List<Ticket> getTickets() {
        return tickets;
    }
    
    public void addTicket(Ticket ticket) {
        tickets.add(ticket);
    }
    
    @Override
    public String toString() {
        return "Customer{" +
                "username='" + getUsername() + '\'' +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}