package pkcg.service;

import pkcg.model.*;

import java.util.*;

public class StationService {
    private List<Station> stations;
    private List<Train> trains;
    private Set<Route> routes;
    private List<Schedule> schedules;
    private List<Reservation> reservations;
    
    public StationService() {
        this.stations = new ArrayList<>();
        this.trains = new ArrayList<>();
        // Using TreeSet for naturally sorted routes
        this.routes = new TreeSet<>(Comparator.comparing(r -> 
            r.getOrigin().getName() + "-" + r.getDestination().getName()));
        this.schedules = new ArrayList<>();
        this.reservations = new ArrayList<>();
    }
    
    // Station operations
    public void addStation(Station station) {
        stations.add(station);
    }
    
    public List<Station> getStations() {
        return Collections.unmodifiableList(stations);
    }
    
    // Train operations
    public void addTrain(Train train) {
        trains.add(train);
    }
    
    public List<Train> getTrains() {
        return Collections.unmodifiableList(trains);
    }
    
    // Route operations
    public void addRoute(Route route) {
        routes.add(route);
    }
    
    public Set<Route> getRoutes() {
        return Collections.unmodifiableSet(routes);
    }
    
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
    
    // Schedule operations
    public void addSchedule(Schedule schedule) {
        schedules.add(schedule);
    }
    
    public List<Schedule> getSchedules() {
        return Collections.unmodifiableList(schedules);
    }
    
    public List<Schedule> findSchedulesByDestination(String destination) {
        List<Schedule> result = new ArrayList<>();
        for (Schedule schedule : schedules) {
            if (schedule.getRoute().getDestination().getName().equalsIgnoreCase(destination)) {
                result.add(schedule);
            }
        }
        return result;
    }
    
    // Reservation operations
    public Reservation reserveSeat(Customer customer, Schedule schedule, int seatNumber) {
        Reservation reservation = new Reservation(customer, schedule, seatNumber);
        reservations.add(reservation);
        return reservation;
    }
    
    public boolean confirmReservation(String reservationId) {
        for (Reservation reservation : reservations) {
            if (reservation.getId().equals(reservationId)) {
                reservation.confirm();
                return true;
            }
        }
        return false;
    }
    
    public boolean cancelReservation(String reservationId) {
        return reservations.removeIf(r -> r.getId().equals(reservationId));
    }
}