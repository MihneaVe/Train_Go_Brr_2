package pkcg.service;

import pkcg.model.Customer;
import pkcg.model.Schedule;
import pkcg.model.Ticket;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TicketService {
    private List<Ticket> tickets;
    private StationService stationService;
    
    public TicketService(StationService stationService) {
        this.tickets = new ArrayList<>();
        this.stationService = stationService;
    }
    
    public Ticket purchaseTicket(Customer customer, Schedule schedule, boolean isFirstClass) {
        Ticket ticket = new Ticket(customer, schedule, isFirstClass);
        tickets.add(ticket);
        customer.addTicket(ticket);
        return ticket;
    }
    
    public List<Ticket> getTicketsByCustomer(Customer customer) {
        List<Ticket> customerTickets = new ArrayList<>();
        for (Ticket ticket : tickets) {
            if (ticket.getCustomer().equals(customer)) {
                customerTickets.add(ticket);
            }
        }
        return customerTickets;
    }
    
    public double getTotalRevenue() {
        return tickets.stream().mapToDouble(Ticket::getPrice).sum();
    }
    
    public List<Ticket> getAllTickets() {
        return Collections.unmodifiableList(tickets);
    }
}