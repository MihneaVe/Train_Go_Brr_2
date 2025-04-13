package pkcg.model;

public class Schedule {
    private Train train;
    private Route route;
    private String departureTime;
    private String arrivalTime;
    private int platformNumber;
    
    public Schedule(Train train, Route route, String departureTime, String arrivalTime, int platformNumber) {
        this.train = train;
        this.route = route;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.platformNumber = platformNumber;
    }
    
    public Train getTrain() {
        return train;
    }
    
    public Route getRoute() {
        return route;
    }
    
    public String getDepartureTime() {
        return departureTime;
    }
    
    public String getArrivalTime() {
        return arrivalTime;
    }
    
    public int getPlatformNumber() {
        return platformNumber;
    }
    
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