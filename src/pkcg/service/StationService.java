package pkcg.service;

import pkcg.model.*;

import java.util.*;

/**
 * Service for managing railway stations, trains, routes, schedules, and reservations.
 * 
 * This service acts as the core business logic layer for railway operations,
 * providing methods to add, retrieve, and manage all railway entities.
 */
public class StationService {
    private List<Station> stations;        // All stations in the system
    private List<Train> trains;            // All trains in the fleet
    private Set<Route> routes;             // All routes between stations
    private List<Schedule> schedules;      // All scheduled train journeys
    private List<Reservation> reservations; // All seat reservations
    
    /**
     * Constructs a new StationService with empty collections.
     */
    public StationService() {
        this.stations = new ArrayList<>();
        this.trains = new ArrayList<>();
        // Using TreeSet for naturally sorted routes
        this.routes = new TreeSet<>(Comparator.comparing(r -> 
            r.getOrigin().getName() + "-" + r.getDestination().getName()));
        this.schedules = new ArrayList<>();
        this.reservations = new ArrayList<>();
    }
    
    /**
     * Adds a new station to the system.
     * 
     * @param station The station to add
     */
    public void addStation(Station station) {
        stations.add(station);
    }
    
    /**
     * Gets all stations in the system.
     * 
     * @return An unmodifiable list of stations
     */
    public List<Station> getStations() {
        return Collections.unmodifiableList(stations);
    }
    
    /**
     * Adds a new train to the fleet.
     * 
     * @param train The train to add
     */
    public void addTrain(Train train) {
        trains.add(train);
    }
    
    /**
     * Gets all trains in the fleet.
     * 
     * @return An unmodifiable list of trains
     */
    public List<Train> getTrains() {
        return Collections.unmodifiableList(trains);
    }
    
    /**
     * Adds a new route between stations.
     * 
     * @param route The route to add
     */
    public void addRoute(Route route) {
        routes.add(route);
    }
    
    /**
     * Gets all routes in the system.
     * 
     * @return An unmodifiable set of routes
     */
    public Set<Route> getRoutes() {
        return Collections.unmodifiableSet(routes);
    }
    
    /**
     * Updates the price of an existing route.
     * 
     * @param route The route to update
     * @param newPrice The new base price for the route
     * @return true if the price was updated successfully, false otherwise
     */
    public boolean updateRoutePrice(Route route, double newPrice) {
        for (Route r : routes) {
            if (r.getOrigin().getName().equals(route.getOrigin().getName()) && 
                r.getDestination().getName().equals(route.getDestination().getName())) {
                r.setBasePrice(newPrice);
                return true;
            }
        }
        return false;
    }
    
    /**
     * Adds a new train schedule.
     * 
     * @param schedule The schedule to add
     */
    public void addSchedule(Schedule schedule) {
        schedules.add(schedule);
    }
    
    /**
     * Gets all schedules in the system.
     * 
     * @return An unmodifiable list of schedules
     */
    public List<Schedule> getSchedules() {
        return Collections.unmodifiableList(schedules);
    }
    
    /**
     * Finds schedules ending at the specified destination station.
     * 
     * @param destination The name of the destination station
     * @return A list of schedules going to the specified destination
     */
    public List<Schedule> findSchedulesByDestination(String destination) {
        List<Schedule> result = new ArrayList<>();
        for (Schedule schedule : schedules) {
            if (schedule.getRoute().getDestination().getName().equalsIgnoreCase(destination)) {
                result.add(schedule);
            }
        }
        return result;
    }
    
    /**
     * Creates a new seat reservation for a customer on a specific schedule.
     * 
     * @param customer The customer making the reservation
     * @param schedule The schedule to reserve a seat on
     * @param seatNumber The specific seat number to reserve
     * @return The created reservation
     */
    public Reservation reserveSeat(Customer customer, Schedule schedule, int seatNumber) {
        Reservation reservation = new Reservation(customer, schedule, seatNumber);
        reservations.add(reservation);
        return reservation;
    }
    
    /**
     * Confirms a previously made reservation.
     * 
     * @param reservationId The ID of the reservation to confirm
     * @return true if the reservation was found and confirmed, false otherwise
     */
    public boolean confirmReservation(String reservationId) {
        for (Reservation reservation : reservations) {
            if (reservation.getId().equals(reservationId)) {
                reservation.confirm();
                return true;
            }
        }
        return false;
    }
    
    /**
     * Cancels an existing reservation.
     * 
     * @param reservationId The ID of the reservation to cancel
     * @return true if the reservation was found and removed, false otherwise
     */
    public boolean cancelReservation(String reservationId) {
        return reservations.removeIf(r -> r.getId().equals(reservationId));
    }
}