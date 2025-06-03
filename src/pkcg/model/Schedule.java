package pkcg.model;

/**
 * Represents a scheduled train journey on a specific route.
 * 
 * A schedule combines a train, a route, departure and arrival times, and a platform number.
 * It represents a specific train service that customers can book tickets for.
 */
public class Schedule {
    private Train train;
    private Route route;
    private String departureTime;
    private String arrivalTime;
    private int platformNumber;
    
    /**
     * Constructs a new Schedule with the specified details.
     * 
     * @param train The train assigned to this schedule
     * @param route The route that this train will follow
     * @param departureTime The departure time in HH:MM format
     * @param arrivalTime The arrival time in HH:MM format
     * @param platformNumber The platform number for boarding
     */
    public Schedule(Train train, Route route, String departureTime, String arrivalTime, int platformNumber) {
        this.train = train;
        this.route = route;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.platformNumber = platformNumber;
    }
    
    /**
     * Gets the train assigned to this schedule.
     * 
     * @return The train
     */
    public Train getTrain() {
        return train;
    }
    
    /**
     * Gets the route for this scheduled journey.
     * 
     * @return The route
     */
    public Route getRoute() {
        return route;
    }
    
    /**
     * Gets the departure time.
     * 
     * @return The departure time in HH:MM format
     */
    public String getDepartureTime() {
        return departureTime;
    }
    
    /**
     * Gets the arrival time.
     * 
     * @return The arrival time in HH:MM format
     */
    public String getArrivalTime() {
        return arrivalTime;
    }
    
    /**
     * Gets the platform number for boarding.
     * 
     * @return The platform number
     */
    public int getPlatformNumber() {
        return platformNumber;
    }
    
    /**
     * Returns a string representation of this Schedule object.
     * 
     * @return A string containing the schedule details
     */
    @Override
    public String toString() {
        return "Schedule{" +
                "train=" + train.getNumber() +
                ", route=" + route.getOrigin().getName() + " to " + route.getDestination().getName() +
                ", departure=" + departureTime +
                ", arrival=" + arrivalTime +
                ", platform=" + platformNumber +
                '}';
    }
}