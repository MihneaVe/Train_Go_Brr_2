package pkcg;

import pkcg.model.*;
import pkcg.repository.*;
import pkcg.service.*;
import pkcg.util.ValidationUtil;

import java.time.LocalTime;
import java.util.*;

/**
 * Main user interface class for the Railway Station Management System.
 * 
 * This class handles all interaction with users through a menu-driven console interface.
 * It provides separate menus for administrators and customers, with access to appropriate
 * functionality based on the user's role.
 * 
 * The Menu class connects the business logic layer (services) with user input/output,
 * coordinating operations like ticket purchases, station management, and scheduling.
 * 
 * @author Velcea Mihnea-Andrei
 */
public class Menu {
    private Scanner scanner;               // For reading user input
    private UserService userService;       // Handles user authentication and management
    private StationService stationService; // Manages stations, schedules, and routes
    private TicketService ticketService;   // Handles ticket purchases and retrieval
    private AuditService auditService;     // For logging user actions
    
    // Repositories for database access
    private UserRepository userRepository;
    private StationRepository stationRepository;
    private TrainRepository trainRepository;
    private RouteRepository routeRepository;
    
    /**
     * Constructs a new Menu instance.
     * Initializes all services and repositories and loads sample data.
     */
    public Menu() {
        this.scanner = new Scanner(System.in);
        this.userService = new UserService();
        this.stationService = new StationService();
        this.ticketService = new TicketService(stationService);
        this.auditService = AuditService.getInstance();
        
        this.userRepository = new UserRepository();
        this.stationRepository = new StationRepository();
        this.trainRepository = new TrainRepository();
        this.routeRepository = new RouteRepository();
        
        // Initialize sample data
        initializeSampleData();
    }
    
    /**
     * Creates sample data in the database if it doesn't already exist.
     * Includes stations, trains, routes, schedules, and user accounts.
     * Only runs if the database is connected and tables are empty.
     */
    private void initializeSampleData() {
        // Checking if database is connected before trying to save
        if (!DatabaseService.getInstance().isConnected()) {
            System.out.println("Running in memory mode - database not connected.");
            return;
        }
        
        try {
            // Check if data already exists in DB - only add if tables are empty
            if (stationRepository.findAll().isEmpty()) {
                // Create sample stations, trains, routes, and users
                Station bucharest = new Station("Bucharest North", 5);
                Station constanta = new Station("Constanta", 3);
                Station brasov = new Station("Brasov", 4);
                
                stationRepository.save(bucharest);
                stationRepository.save(constanta);
                stationRepository.save(brasov);
                
                Train ir1582 = new Train("IR1582", "InterRegio", 120);
                Train r9351 = new Train("R9351", "Regio", 80);
                
                trainRepository.save(ir1582);
                trainRepository.save(r9351);
                
                Route bucToCon = new Route(bucharest, constanta, 225.0);
                Route bucToBra = new Route(bucharest, brasov, 166.0);
                
                routeRepository.save(bucToCon);
                routeRepository.save(bucToBra);
                
                // Create users
                Admin admin = new Admin("admin", "admin123");
                Customer customer = new Customer("john", "pass123", "John Doe", "john@example.com");
                
                userRepository.save(admin);
                userRepository.save(customer);
                
                // Add schedules to stationService
                stationService.addSchedule(new Schedule(ir1582, bucToCon, "08:00", "10:30", 1));
                stationService.addSchedule(new Schedule(r9351, bucToBra, "09:15", "11:45", 3));
            }
        } catch (Exception e) {
            // Silently handle any exceptions during sample data creation
            System.out.println("Using existing data from the database.");
        }
    }
    
    /**
     * Starts the application's main menu loop.
     * Handles user login, registration, and system exit.
     * Also ensures proper cleanup of resources on exit.
     */
    public void start() {
        boolean exit = false;
        
        System.out.println("\nWELCOME TO THE RAILWAY STATION MANAGEMENT SYSTEM");
        
        while (!exit) {
            // Display main menu options
            System.out.println("\n===== Railway Station Management System =====");
            System.out.println("1. Login");
            System.out.println("2. Register as Customer");
            System.out.println("3. Exit");
            
            int option = ValidationUtil.readValidInt(scanner, "Choose an option", 1, 3, false);
            
            switch (option) {
                case 1:
                    login();
                    break;
                case 2:
                    registerCustomer();
                    break;
                case 3:
                    exit = true;
                    System.out.println("Goodbye!");
                    break;
            }
        }
        
        // Clean up resources
        scanner.close();
        auditService.close();
        DatabaseService.getInstance().closeConnection();
    }
    
    /**
     * Handles user login process.
     * Validates credentials and redirects to appropriate menu based on user role.
     */
    private void login() {
        String username = ValidationUtil.readValidString(scanner, "Enter username", 1, false, true);
        if (username == null) return; // User chose to go back
        
        String password = ValidationUtil.readValidString(scanner, "Enter password", 1, false, true);
        if (password == null) return; // User chose to go back
        
        if (userService.login(username, password)) {
            auditService.logAction("LOGIN");
            System.out.println("Login successful!");
            
            // Direct user to the appropriate menu based on role
            if (userService.isAdmin()) {
                showAdminMenu();
            } else {
                showCustomerMenu();
            }
        } else {
            System.out.println("Login failed. Invalid username or password.");
            System.out.println("Press Enter to continue...");
            scanner.nextLine();
        }
    }
    
    /**
     * Handles customer registration process.
     * Collects and validates user details, then creates account.
     */
    private void registerCustomer() {
        // Username validation with database check
        String username = null;
        boolean validUsername = false;
        
        while (!validUsername) {
            username = ValidationUtil.readValidString(scanner, "Enter username", 3, false, true);
            if (username == null) return; // User chose to go back
            
            // Check if username already exists in the database
            Optional<User> existingUser = userRepository.findByUsername(username);
            if (existingUser.isPresent()) {
                System.out.println("Username already exists. Please choose a different username.");
            } else {
                validUsername = true;
            }
        }
        
        // Password validation
        String password = null;
        boolean validPassword = false;
        
        while (!validPassword) {
            password = ValidationUtil.readValidString(scanner, 
                "Enter password (min 4 letters, 3 numbers, 1 special character, max 20 chars)", 8, false, true);
            
            if (password == null) return; // User chose to go back
            
            validPassword = ValidationUtil.isValidPassword(password);
            if (!validPassword) {
                System.out.println("Invalid password format. Please try again.");
            }
        }
        
        String fullName = ValidationUtil.readValidString(scanner, "Enter full name", 3, false, true);
        if (fullName == null) return; // User chose to go back
        
        // Email validation
        String email = null;
        boolean validEmail = false;
        
        while (!validEmail) {
            email = ValidationUtil.readValidString(scanner, "Enter email (format: example@domain.com)", 5, false, true);
            if (email == null) return; // User chose to go back
            
            validEmail = ValidationUtil.isValidEmail(email);
            if (!validEmail) {
                System.out.println("Invalid email format. Please try again.");
            }
        }
        
        // Register the customer
        userService.registerCustomer(username, password, fullName, email);
        
        // Save to database
        User newUser = new Customer(username, password, fullName, email);
        userRepository.save(newUser);
        
        auditService.logAction("REGISTER_CUSTOMER");
        System.out.println("Customer registration successful!");
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }
    
    /**
     * Displays the administrator menu.
     * Provides options for managing stations, trains, routes, and schedules.
     */
    private void showAdminMenu() {
        boolean logout = false;
        
        while (!logout) {
            System.out.println("\n===== Admin Menu =====");
            System.out.println("1. Manage Stations");
            System.out.println("2. Manage Trains");
            System.out.println("3. Manage Routes");
            System.out.println("4. Manage Schedules");
            System.out.println("5. View Revenue Report");
            System.out.println("6. Clear Database");
            System.out.println("7. Logout");
            
            int option = ValidationUtil.readValidInt(scanner, "Choose an option", 1, 7, false);
            
            switch (option) {
                case 1:
                    manageStations();
                    break;
                case 2:
                    manageTrains();
                    break;
                case 3:
                    manageRoutes();
                    break;
                case 4:
                    manageSchedules();
                    break;
                case 5:
                    viewRevenueReport();
                    break;
                case 6:
                    clearDatabase();
                    break;
                case 7:
                    logout = true;
                    userService.logout();
                    auditService.logAction("LOGOUT");
                    System.out.println("Logged out successfully.");
                    break;
            }
        }
    }
    
    /**
     * Handles database clearing with confirmation and authentication.
     * Requires admin password verification before proceeding.
     */
    private void clearDatabase() {
        System.out.println("\n===== Clear Database =====");
        System.out.println("WARNING: This will delete all data from the database!");
        
        boolean confirm = ValidationUtil.readYesNo(scanner, "Are you sure you want to proceed?");
        if (!confirm) {
            System.out.println("Database clearing aborted.");
            return;
        }
        
        String password = ValidationUtil.readValidString(scanner, "Enter admin password to confirm", 1, false, true);
        if (password == null) return; // User chose to go back
        
        // Verify admin password before proceeding
        if (userService.getCurrentUser().authenticate(password)) {
            // Clear the database
            boolean success = clearAllTables();
            
            if (success) {
                auditService.logAction("CLEAR_DATABASE");
                System.out.println("Database cleared successfully!");
            } else {
                System.out.println("Failed to clear database. Check logs for details.");
            }
        } else {
            System.out.println("Incorrect password. Database clearing aborted.");
        }
        
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }
    
    /**
     * Executes the actual database clearing operation.
     * Clears tables in reverse order of dependencies.
     * 
     * @return true if successful, false if exception occurred
     */
    private boolean clearAllTables() {
        try {
            // Clear in reverse order of dependencies
            routeRepository.clearAll();
            trainRepository.clearAll();
            stationRepository.clearAll();
            
            // Keep the admin user but clear all customers
            userRepository.clearAllExceptAdmin();
            
            return true;
        } catch (Exception e) {
            System.err.println("Error clearing database: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Displays the station management menu.
     * Provides options for adding and viewing stations.
     */
    private void manageStations() {
        boolean back = false;
        
        while (!back) {
            System.out.println("\n===== Station Management =====");
            System.out.println("1. Add New Station");
            System.out.println("2. View All Stations");
            System.out.println("3. Back to Admin Menu");
            
            int option = ValidationUtil.readValidInt(scanner, "Choose an option", 1, 3, false);
            
            switch (option) {
                case 1:
                    addStation();
                    break;
                case 2:
                    viewStations();
                    break;
                case 3:
                    back = true;
                    break;
            }
        }
    }
    
    /**
     * Displays a list of all stations in the system.
     * Shows station names and their platform counts in tabular format.
     */
    private void viewStations() {
        List<Station> stations = stationRepository.findAll();
        
        System.out.println("\n===== All Stations =====");
        if (stations.isEmpty()) {
            System.out.println("No stations available.");
        } else {
            // Table header
            System.out.println(String.format("%-25s | %-15s", "Station Name", "Platforms"));
            System.out.println("-".repeat(43));
            
            // Table rows
            for (Station station : stations) {
                System.out.println(String.format("%-25s | %-15d", 
                    station.getName(), 
                    station.getPlatforms().size()));
            }
        }
        auditService.logAction("VIEW_STATIONS");
        
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }
    
    /**
     * Handles creation of a new station.
     * Collects station name and platform count, then creates the station.
     */
    private void addStation() {
        String stationName = ValidationUtil.readValidString(scanner, "Enter station name", 2, false, true);
        if (stationName == null) return; // User chose to go back
        
        // Check if station already exists
        Optional<Station> existingStation = stationRepository.findByName(stationName);
        if (existingStation.isPresent()) {
            System.out.println("A station with this name already exists.");
            System.out.println("Press Enter to continue...");
            scanner.nextLine();
            return;
        }
        
        int platformCount = ValidationUtil.readValidInt(scanner, "Enter number of platforms", 1, 20, true);
        if (platformCount == -1) return; // User chose to go back
        
        Station station = new Station(stationName, platformCount);
        stationRepository.save(station);
        
        auditService.logAction("ADD_STATION");
        System.out.println("Station added successfully!");
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }
    
    /**
     * Displays the train management menu.
     * Provides options for adding and viewing trains.
     */
    private void manageTrains() {
        boolean back = false;
        
        while (!back) {
            System.out.println("\n===== Train Management =====");
            System.out.println("1. Add New Train");
            System.out.println("2. View All Trains");
            System.out.println("3. Back to Admin Menu");
            
            int option = ValidationUtil.readValidInt(scanner, "Choose an option", 1, 3, false);
            
            switch (option) {
                case 1:
                    addTrain();
                    break;
                case 2:
                    viewTrains();
                    break;
                case 3:
                    back = true;
                    break;
            }
        }
    }
    
    /**
     * Displays a list of all trains in the system.
     * Shows train numbers, types, and capacities in tabular format.
     */
    private void viewTrains() {
        List<Train> trains = trainRepository.findAll();
        
        System.out.println("\n===== All Trains =====");
        if (trains.isEmpty()) {
            System.out.println("No trains available.");
        } else {
            // Table header
            System.out.println(String.format("%-10s | %-15s | %-10s", 
                "Number", "Type", "Capacity"));
            System.out.println("-".repeat(40));
            
            // Table rows
            for (Train train : trains) {
                System.out.println(String.format("%-10s | %-15s | %-10d", 
                    train.getNumber(), 
                    train.getType(), 
                    train.getCapacity()));
            }
        }
        auditService.logAction("VIEW_TRAINS");
        
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }
    
    /**
     * Handles creation of a new train.
     * Collects train number, type, and capacity, then creates the train.
     */
    private void addTrain() {
        String trainNumber = ValidationUtil.readValidString(scanner, "Enter train number", 2, false, true);
        if (trainNumber == null) return; // User chose to go back
        
        // Check if train already exists
        Optional<Train> existingTrain = trainRepository.findByNumber(trainNumber);
        if (existingTrain.isPresent()) {
            System.out.println("A train with this number already exists.");
            System.out.println("Press Enter to continue...");
            scanner.nextLine();
            return;
        }
        
        String trainType = ValidationUtil.readValidString(scanner, "Enter train type (e.g., InterRegio, Regio)", 2, false, true);
        if (trainType == null) return; // User chose to go back
        
        int capacity = ValidationUtil.readValidInt(scanner, "Enter train capacity", 1, 1000, true);
        if (capacity == -1) return; // User chose to go back
        
        Train train = new Train(trainNumber, trainType, capacity);
        trainRepository.save(train);
        
        auditService.logAction("ADD_TRAIN");
        System.out.println("Train added successfully!");
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }
    
    /**
     * Displays the route management menu.
     * Provides options for adding, updating, and viewing routes.
     */
    private void manageRoutes() {
        boolean back = false;
        
        while (!back) {
            System.out.println("\n===== Route Management =====");
            System.out.println("1. Add New Route");
            System.out.println("2. Update Route Price");
            System.out.println("3. View All Routes");
            System.out.println("4. Back to Admin Menu");
            
            int option = ValidationUtil.readValidInt(scanner, "Choose an option", 1, 4, false);
            
            switch (option) {
                case 1:
                    addRoute();
                    break;
                case 2:
                    updateRoutePrice();
                    break;
                case 3:
                    viewRoutes();
                    break;
                case 4:
                    back = true;
                    break;
            }
        }
    }
    
    /**
     * Displays a list of all routes in the system.
     * Shows origin, destination, and price in tabular format.
     */
    private void viewRoutes() {
        List<Route> routes = new ArrayList<>(routeRepository.findAll());
        
        System.out.println("\n===== All Routes =====");
        if (routes.isEmpty()) {
            System.out.println("No routes available.");
        } else {
            // Table header
            System.out.println(String.format("%-25s | %-25s | %-10s", 
                "Origin", "Destination", "Price (RON)"));
            System.out.println("-".repeat(64));
            
            // Table rows
            for (Route route : routes) {
                System.out.println(String.format("%-25s | %-25s | %10.2f", 
                    route.getOrigin().getName(), 
                    route.getDestination().getName(), 
                    route.getBasePrice()));
            }
        }
        auditService.logAction("VIEW_ROUTES");
        
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }
    
    /**
     * Handles creation of a new route between stations.
     * Selects origin and destination stations and sets a price.
     */
    private void addRoute() {
        List<Station> stations = stationRepository.findAll();
        
        if (stations.size() < 2) {
            System.out.println("You need at least two stations to create a route.");
            System.out.println("Press Enter to continue...");
            scanner.nextLine();
            return;
        }
        
        // Display available stations
        System.out.println("\n===== Available Stations =====");
        for (int i = 0; i < stations.size(); i++) {
            System.out.println((i + 1) + ". " + stations.get(i).getName());
        }
        
        // Select origin station
        int originIndex = ValidationUtil.readValidInt(scanner, "Select origin station number", 1, stations.size(), true);
        if (originIndex == -1) return; // User chose to go back
        originIndex--; // Convert to zero-based index
        
        // Select destination station
        int destIndex = ValidationUtil.readValidInt(scanner, "Select destination station number", 1, stations.size(), true);
        if (destIndex == -1) return; // User chose to go back
        destIndex--; // Convert to zero-based index
        
        if (destIndex == originIndex) {
            System.out.println("Origin and destination cannot be the same station.");
            System.out.println("Press Enter to continue...");
            scanner.nextLine();
            return;
        }
        
        // Check if route already exists
        Station origin = stations.get(originIndex);
        Station destination = stations.get(destIndex);
        Optional<Route> existingRoute = routeRepository.findByStations(origin.getName(), destination.getName());
        
        if (existingRoute.isPresent()) {
            System.out.println("A route between these stations already exists.");
            System.out.println("Press Enter to continue...");
            scanner.nextLine();
            return;
        }
        
        // Get base price
        double basePrice = ValidationUtil.readValidDouble(scanner, "Enter base price for this route (RON)", 0.01, true);
        if (basePrice == -1) return; // User chose to go back
        
        // Create and save the route
        Route route = new Route(origin, destination, basePrice);
        routeRepository.save(route);
        
        auditService.logAction("ADD_ROUTE");
        System.out.println("Route added successfully!");
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }
    
    /**
     * Handles updating the price of an existing route.
     * User selects a route and enters a new price.
     */
    private void updateRoutePrice() {
        List<Route> routes = routeRepository.findAll();
        
        if (routes.isEmpty()) {
            System.out.println("No routes available to update.");
            System.out.println("Press Enter to continue...");
            scanner.nextLine();
            return;
        }
        
        // Display routes with current prices
        System.out.println("\n===== Routes for Price Update =====");
        for (int i = 0; i < routes.size(); i++) {
            Route route = routes.get(i);
            System.out.println((i + 1) + ". " + 
                route.getOrigin().getName() + " -> " + 
                route.getDestination().getName() + 
                " (Current price: " + route.getBasePrice() + " RON)");
        }
        
        // Select route to update
        int routeIndex = ValidationUtil.readValidInt(scanner, "Select route number to update", 1, routes.size(), true);
        if (routeIndex == -1) return; // User chose to go back
        routeIndex--; // Convert to zero-based index
        
        // Get new price
        double newPrice = ValidationUtil.readValidDouble(scanner, "Enter new price for this route (RON)", 0.01, true);
        if (newPrice == -1) return; // User chose to go back
        
        Route route = routes.get(routeIndex);
        route.setBasePrice(newPrice);
        routeRepository.save(route);
        
        auditService.logAction("UPDATE_ROUTE_PRICE");
        System.out.println("Route price updated successfully!");
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }
    
    /**
     * Displays the schedule management menu.
     * Provides options for adding, viewing, and finding schedules.
     */
    private void manageSchedules() {
        boolean back = false;
        
        while (!back) {
            System.out.println("\n===== Schedule Management =====");
            System.out.println("1. Add New Schedule");
            System.out.println("2. View All Schedules");
            System.out.println("3. Find Schedules by Destination");
            System.out.println("4. Back to Admin Menu");
            
            int option = ValidationUtil.readValidInt(scanner, "Choose an option", 1, 4, false);
            
            switch (option) {
                case 1:
                    addSchedule();
                    break;
                case 2:
                    viewSchedules();
                    break;
                case 3:
                    findSchedulesByDestination();
                    break;
                case 4:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }
    }
    
    /**
     * Displays a list of all train schedules in the system.
     * Shows detailed information about each schedule in tabular format.
     */
    private void viewSchedules() {
        List<Schedule> schedules = stationService.getSchedules();
        
        System.out.println("\n===== All Schedules =====");
        if (schedules.isEmpty()) {
            System.out.println("No schedules available.");
        } else {
            // Table header
            System.out.println(String.format("%-10s | %-25s | %-25s | %-10s | %-10s | %-8s",
                "Train", "Origin", "Destination", "Departure", "Arrival", "Platform"));
            System.out.println("-".repeat(96));
            
            // Table rows
            for (Schedule schedule : schedules) {
                System.out.println(String.format("%-10s | %-25s | %-25s | %-10s | %-10s | %-8d", 
                    schedule.getTrain().getNumber(),
                    schedule.getRoute().getOrigin().getName(),
                    schedule.getRoute().getDestination().getName(),
                    schedule.getDepartureTime(),
                    schedule.getArrivalTime(),
                    schedule.getPlatformNumber()));
            }
        }
        auditService.logAction("VIEW_SCHEDULES");
        
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }
    
    /**
     * Handles creation of a new train schedule.
     * Selects train, route, departure/arrival times, and platform.
     * Validates that platform is within origin station's capacity.
     */
    private void addSchedule() {
        List<Train> trains = trainRepository.findAll();
        List<Route> routes = routeRepository.findAll();
        
        if (trains.isEmpty()) {
            System.out.println("No trains available. Please add trains first.");
            System.out.println("Press Enter to continue...");
            scanner.nextLine();
            return;
        }
        
        if (routes.isEmpty()) {
            System.out.println("No routes available. Please add routes first.");
            System.out.println("Press Enter to continue...");
            scanner.nextLine();
            return;
        }
        
        // Display available trains
        System.out.println("\n===== Available Trains =====");
        for (int i = 0; i < trains.size(); i++) {
            Train train = trains.get(i);
            System.out.println((i + 1) + ". " + train.getNumber() + " (" + train.getType() + ")");
        }
        
        // Select train
        int trainIndex = ValidationUtil.readValidInt(scanner, "Select train number", 1, trains.size(), true);
        if (trainIndex == -1) return; // User chose to go back
        trainIndex--; // Convert to zero-based index
        
        // Display available routes
        System.out.println("\n===== Available Routes =====");
        for (int i = 0; i < routes.size(); i++) {
            Route route = routes.get(i);
            System.out.println((i + 1) + ". " + route.getOrigin().getName() + 
                " -> " + route.getDestination().getName());
        }
        
        // Select route
        int routeIndex = ValidationUtil.readValidInt(scanner, "Select route number", 1, routes.size(), true);
        if (routeIndex == -1) return; // User chose to go back
        routeIndex--; // Convert to zero-based index
        
        // Get departure time
        String departureTime = ValidationUtil.readValidTime(scanner, "Enter departure time", true);
        if (departureTime == null) return; // User chose to go back
        
        // Get arrival time
        String arrivalTime = ValidationUtil.readValidTime(scanner, "Enter arrival time", true);
        if (arrivalTime == null) return; // User chose to go back
        
        // Validate arrival time is after departure time
        try {
            if (LocalTime.parse(departureTime).isAfter(LocalTime.parse(arrivalTime))) {
                System.out.println("Arrival time cannot be earlier than departure time.");
                System.out.println("Press Enter to continue...");
                scanner.nextLine();
                return;
            }
        } catch (Exception e) {
            System.out.println("Error validating times. Using times as provided.");
        }
        
        // Get selected route and determine the maximum platform number from the origin station
        Route route = routes.get(routeIndex);
        Station originStation = route.getOrigin();
        int maxPlatform = originStation.getPlatforms().size();
        
        if (maxPlatform <= 0) {
            System.out.println("Error: The origin station (" + originStation.getName() + 
                              ") doesn't have any platforms available.");
            System.out.println("Press Enter to continue...");
            scanner.nextLine();
            return;
        }
        
        // Display available platforms
        System.out.println("\n===== Available Platforms at " + originStation.getName() + " =====");
        System.out.println("This station has " + maxPlatform + " platform(s) numbered 1 to " + maxPlatform);
        
        // Get platform number with validation against the station's capacity
        int platformNumber = ValidationUtil.readValidInt(scanner, 
            "Enter platform number (1-" + maxPlatform + ")", 1, maxPlatform, true);
        if (platformNumber == -1) return; // User chose to go back
        
        // Create and save schedule
        Train train = trains.get(trainIndex);
        Schedule schedule = new Schedule(train, route, departureTime, arrivalTime, platformNumber);
        
        stationService.addSchedule(schedule);
        
        auditService.logAction("ADD_SCHEDULE");
        System.out.println("Schedule added successfully!");
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }
    
    /**
     * Displays the customer menu.
     * Provides options for viewing routes, schedules, purchasing tickets, and making reservations.
     */
    private void showCustomerMenu() {
        boolean logout = false;
        
        while (!logout) {
            System.out.println("\n===== Customer Menu =====");
            System.out.println("1. View Available Routes");
            System.out.println("2. View Schedules");
            System.out.println("3. Find Schedules by Destination");
            System.out.println("4. Purchase Ticket");
            System.out.println("5. View My Tickets");
            System.out.println("6. Make Reservation");
            System.out.println("7. Logout");
            
            int option = ValidationUtil.readValidInt(scanner, "Choose an option", 1, 7, false);
            
            switch (option) {
                case 1:
                    viewRoutes();
                    break;
                case 2:
                    viewSchedules();
                    break;
                case 3:
                    findSchedulesByDestination();
                    break;
                case 4:
                    purchaseTicket();
                    break;
                case 5:
                    viewMyTickets();
                    break;
                case 6:
                    makeReservation();
                    break;
                case 7:
                    logout = true;
                    userService.logout();
                    auditService.logAction("LOGOUT");
                    System.out.println("Logged out successfully.");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }
    }
    
    /**
     * Displays tickets purchased by the current customer.
     * Shows detailed ticket information in tabular format.
     */
    private void viewMyTickets() {
        Customer customer = (Customer) userService.getCurrentUser();
        List<Ticket> tickets = ticketService.getTicketsByCustomer(customer);
        
        System.out.println("\n===== My Tickets =====");
        if (tickets.isEmpty()) {
            System.out.println("You don't have any tickets yet.");
        } else {
            // Table header
            System.out.println(String.format("%-8s | %-25s | %-25s | %-10s | %-10s | %-12s | %-10s",
                "ID", "From", "To", "Departure", "Price", "First Class", "Train"));
            System.out.println("-".repeat(110));
            
            // Table rows
            for (Ticket ticket : tickets) {
                Schedule schedule = ticket.getSchedule();
                System.out.println(String.format("%-8s | %-25s | %-25s | %-10s | %10.2f | %-12s | %-10s",
                    ticket.getId(),
                    schedule.getRoute().getOrigin().getName(),
                    schedule.getRoute().getDestination().getName(),
                    schedule.getDepartureTime(),
                    ticket.getPrice(),
                    ticket.isFirstClass() ? "Yes" : "No",
                    schedule.getTrain().getNumber()));
            }
        }
        auditService.logAction("VIEW_MY_TICKETS");
        
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }
    
    /**
     * Handles the ticket purchasing process.
     * User selects a schedule, class preference, and confirms purchase.
     */
    private void purchaseTicket() {
        List<Schedule> schedules = stationService.getSchedules();
        
        if (schedules.isEmpty()) {
            System.out.println("No schedules available for booking tickets.");
            System.out.println("Press Enter to continue...");
            scanner.nextLine();
            return;
        }
        
        // Display available schedules
        System.out.println("\n===== Available Schedules =====");
        for (int i = 0; i < schedules.size(); i++) {
            Schedule schedule = schedules.get(i);
            System.out.println((i + 1) + ". Train: " + schedule.getTrain().getNumber() + 
                " | From: " + schedule.getRoute().getOrigin().getName() +
                " | To: " + schedule.getRoute().getDestination().getName() +
                " | Departure: " + schedule.getDepartureTime());
        }
        
        // Select schedule
        int scheduleIndex = ValidationUtil.readValidInt(scanner, "Select schedule number", 1, schedules.size(), true);
        if (scheduleIndex == -1) return; // User chose to go back
        scheduleIndex--; // Convert to zero-based index
        
        // Ask for first class option
        boolean isFirstClass = ValidationUtil.readYesNo(scanner, "Would you like first class?");
        
        Customer customer = (Customer) userService.getCurrentUser();
        Schedule selectedSchedule = schedules.get(scheduleIndex);
        
        // Calculate price for display
        double basePrice = selectedSchedule.getRoute().getBasePrice();
        double finalPrice = isFirstClass ? basePrice * 1.5 : basePrice;
        
        // Confirm purchase
        System.out.printf("Ticket price will be %.2f RON\n", finalPrice);
        boolean confirmPurchase = ValidationUtil.readYesNo(scanner, "Confirm purchase?");
        
        if (!confirmPurchase) {
            System.out.println("Ticket purchase canceled.");
            System.out.println("Press Enter to continue...");
            scanner.nextLine();
            return;
        }
        
        // Create and process ticket
        Ticket ticket = ticketService.purchaseTicket(customer, selectedSchedule, isFirstClass);
        
        auditService.logAction("PURCHASE_TICKET");
        System.out.println("Ticket purchased successfully!");
        System.out.println("Ticket ID: " + ticket.getId());
        System.out.println("Total price: " + String.format("%.2f", ticket.getPrice()) + " RON");
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }
    
    /**
     * Handles the seat reservation process.
     * User selects a schedule and seat number for reservation.
     */
    private void makeReservation() {
        List<Schedule> schedules = stationService.getSchedules();
        
        if (schedules.isEmpty()) {
            System.out.println("No schedules available for reservation.");
            System.out.println("Press Enter to continue...");
            scanner.nextLine();
            return;
        }
        
        // Display available schedules
        System.out.println("\n===== Available Schedules for Reservation =====");
        for (int i = 0; i < schedules.size(); i++) {
            Schedule schedule = schedules.get(i);
            System.out.println((i + 1) + ". Train: " + schedule.getTrain().getNumber() + 
                " | From: " + schedule.getRoute().getOrigin().getName() +
                " | To: " + schedule.getRoute().getDestination().getName() +
                " | Departure: " + schedule.getDepartureTime());
        }
        
        // Select schedule
        int scheduleIndex = ValidationUtil.readValidInt(scanner, "Select schedule number for reservation", 1, schedules.size(), true);
        if (scheduleIndex == -1) return; // User chose to go back
        scheduleIndex--; // Convert to zero-based index
        
        // Ask for seat number
        int seatNumber = ValidationUtil.readValidInt(scanner, "Enter seat number", 1, 100, true);
        if (seatNumber == -1) return; // User chose to go back
        
        Schedule selectedSchedule = schedules.get(scheduleIndex);
        
        // Validate seat number against train capacity
        if (seatNumber > selectedSchedule.getTrain().getCapacity()) {
            System.out.println("Selected seat number exceeds train capacity (" + 
                selectedSchedule.getTrain().getCapacity() + ").");
            System.out.println("Press Enter to continue...");
            scanner.nextLine();
            return;
        }
        
        Customer customer = (Customer) userService.getCurrentUser();
        
        // Create reservation
        Reservation reservation = stationService.reserveSeat(customer, selectedSchedule, seatNumber);
        
        auditService.logAction("MAKE_RESERVATION");
        System.out.println("Reservation made successfully!");
        System.out.println("Reservation ID: " + reservation.getId());
        System.out.println("Please pay " + String.format("%.2f", selectedSchedule.getRoute().getBasePrice()) + 
            " RON within 24 hours to confirm your reservation.");
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }
    
    /**
     * Finds and displays train schedules for a specific destination.
     * User enters destination name and schedules are shown if found.
     */
    private void findSchedulesByDestination() {
        String destName = ValidationUtil.readValidString(scanner, "Enter destination station name", 2, false, true);
        if (destName == null) return; // User chose to go back
        
        List<Schedule> schedules = stationService.findSchedulesByDestination(destName);
        
        System.out.println("\n===== Schedules to " + destName + " =====");
        if (schedules.isEmpty()) {
            System.out.println("No schedules available for this destination.");
        } else {
            // Table header
            System.out.println(String.format("%-10s | %-25s | %-10s | %-10s | %-8s",
                "Train", "From", "Departure", "Arrival", "Platform"));
            System.out.println("-".repeat(70));
            
            // Table rows
            for (Schedule schedule : schedules) {
                System.out.println(String.format("%-10s | %-25s | %-10s | %-10s | %-8d", 
                    schedule.getTrain().getNumber(),
                    schedule.getRoute().getOrigin().getName(),
                    schedule.getDepartureTime(),
                    schedule.getArrivalTime(),
                    schedule.getPlatformNumber()));
            }
        }
        auditService.logAction("FIND_SCHEDULES_BY_DESTINATION");
        
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }
    
    /**
     * Displays a revenue report for administrators.
     * Shows the total revenue from all ticket sales.
     */
    private void viewRevenueReport() {
        double totalRevenue = ticketService.getTotalRevenue();
        
        System.out.println("\n===== Revenue Report =====");
        System.out.println("Total Revenue: " + String.format("%.2f RON", totalRevenue));
        auditService.logAction("VIEW_REVENUE_REPORT");
        
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }
}