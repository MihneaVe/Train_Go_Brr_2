package pkcg.model;

public class Route {
    private Station origin;
    private Station destination;
    private double basePrice;
    
    public Route(Station origin, Station destination, double basePrice) {
        this.origin = origin;
        this.destination = destination;
        this.basePrice = basePrice;
    }
    
    public Station getOrigin() {
        return origin;
    }
    
    public Station getDestination() {
        return destination;
    }
    
    public double getBasePrice() {
        return basePrice;
    }
    
    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }
    
    @Override
    public String toString() {
        return "Route{" +
                "origin=" + origin.getName() +
                ", destination=" + destination.getName() +
                ", basePrice=" + basePrice +
                '}';
    }
}