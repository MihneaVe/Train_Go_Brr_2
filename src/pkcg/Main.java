package pkcg;

import pkcg.model.*;
import pkcg.service.StationService;
import pkcg.service.UserService;
import pkcg.service.TicketService;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Train Station Management System");
        
        // Initialize services
        StationService stationService = new StationService();
        UserService userService = new UserService();
        TicketService ticketService = new TicketService(stationService);
        
        // Sample data initialization
        initializeSampleData(stationService, userService);
        
        // Demo operations
        demoOperations(stationService, userService, ticketService);
    }
    
    private static void initializeSampleData(StationService stationService, UserService userService) {
        // Create stations
        Station bucharest = new Station("Bucharest North", 5);
        Station constanta = new Station("Constanta", 3);
        Station brasov = new Station("Brasov", 4);
        
        stationService.addStation(bucharest);
        stationService.addStation(constanta);
        stationService.addStation(brasov);
        
        // Create trains
        Train ir1582 = new Train("IR1582", "InterRegio", 120);
        Train r9351 = new Train("R9351", "Regio", 80);
        
        // Create routes
        Route bucToCon = new Route(bucharest, constanta, 225.0);
        Route bucToBra = new Route(bucharest, brasov, 166.0);
        
        stationService.addTrain(ir1582);
        stationService.addTrain(r9351);
        stationService.addRoute(bucToCon);
        stationService.addRoute(bucToBra);
        
        // Create schedules
        stationService.addSchedule(new Schedule(ir1582, bucToCon, "08:00", "10:30", 1));
        stationService.addSchedule(new Schedule(r9351, bucToBra, "09:15", "11:45", 3));
        
        // Create users
        userService.registerAdmin("admin", "admin123");
        userService.registerCustomer("john", "pass123", "John Doe", "john@example.com");
    }
    
    private static void demoOperations(StationService stationService, UserService userService, 
                                      TicketService ticketService) {
        // Demo admin login
        System.out.println("\n--- Admin Operations ---");
        if (userService.login("admin", "admin123")) {
            System.out.println("Admin logged in successfully");
            
            // Admin can modify ticket prices
            stationService.getRoutes().stream()
                .findFirst()
                .ifPresent(route -> stationService.updateRoutePrice(route, 250.0));
            
            System.out.println("Updated route prices:");
            stationService.getRoutes().forEach(System.out::println);
        }
        
        // Demo customer operations
        System.out.println("\n--- Customer Operations ---");
        if (userService.login("john", "pass123")) {
            System.out.println("Customer logged in successfully");
            
            // View available routes
            System.out.println("Available routes:");
            stationService.getRoutes().forEach(System.out::println);
            
            // Purchase a ticket
            User customer = userService.getCurrentUser();
            if (customer instanceof Customer) {
                Schedule schedule = stationService.getSchedules().get(0);
                Ticket ticket = ticketService.purchaseTicket((Customer)customer, schedule, false);
                System.out.println("Purchased ticket: " + ticket);
            }
        }
    }
}