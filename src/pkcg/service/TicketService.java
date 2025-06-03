package pkcg.service;

import pkcg.model.Customer;
import pkcg.model.Schedule;
import pkcg.model.Ticket;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Service for managing ticket purchases and retrieval.
 * 
 * This service handles the creation of tickets when customers purchase them,
 * tracks all tickets in the system, and provides methods for filtering and
 * calculating ticket-related statistics.
 */
public class TicketService {
    private List<Ticket> tickets;
    
    /**
     * Constructs a new TicketService.
     * 
     * @param stationService The StationService instance for retrieving schedule information
     */
    public TicketService(StationService stationService) {
        this.tickets = new ArrayList<>();
    }
    
    /**
     * Creates a new ticket for a customer on a specific schedule.
     * Adds the ticket to the system records and to the customer's personal ticket collection.
     * 
     * @param customer The customer purchasing the ticket
     * @param schedule The schedule the ticket is for
     * @param isFirstClass Whether this is a first-class ticket
     * @return The created ticket
     */
    public Ticket purchaseTicket(Customer customer, Schedule schedule, boolean isFirstClass) {
        Ticket ticket = new Ticket(customer, schedule, isFirstClass);
        tickets.add(ticket);
        customer.addTicket(ticket);
        return ticket;
    }
    
    /**
     * Retrieves all tickets purchased by a specific customer.
     * 
     * @param customer The customer whose tickets to retrieve
     * @return A list of tickets purchased by the customer
     */
    public List<Ticket> getTicketsByCustomer(Customer customer) {
        List<Ticket> customerTickets = new ArrayList<>();
        for (Ticket ticket : tickets) {
            if (ticket.getCustomer().equals(customer)) {
                customerTickets.add(ticket);
            }
        }
        return customerTickets;
    }
    
    /**
     * Calculates the total revenue from all ticket sales.
     * 
     * @return The total revenue in RON
     */
    public double getTotalRevenue() {
        return tickets.stream().mapToDouble(Ticket::getPrice).sum();
    }
    
    /**
     * Gets all tickets in the system.
     * 
     * @return An unmodifiable list of all tickets
     */
    public List<Ticket> getAllTickets() {
        return Collections.unmodifiableList(tickets);
    }
}