package pkcg.model;

/**
 * Represents a route between two stations in the railway network.
 * 
 * A route connects an origin station to a destination station and has a base price
 * that serves as the foundation for ticket pricing. Routes are typically one-way;
 * a return journey would be represented by a separate Route object.
 */
public class Route {
    private Station origin;
    private Station destination;
    private double basePrice;
    
    /**
     * Constructs a new Route between the specified stations with the given base price.
     * 
     * @param origin The station where the route begins
     * @param destination The station where the route ends
     * @param basePrice The base price for tickets on this route
     */
    public Route(Station origin, Station destination, double basePrice) {
        this.origin = origin;
        this.destination = destination;
        this.basePrice = basePrice;
    }
    
    /**
     * Gets the origin station of this route.
     * 
     * @return The origin station
     */
    public Station getOrigin() {
        return origin;
    }
    
    /**
     * Gets the destination station of this route.
     * 
     * @return The destination station
     */
    public Station getDestination() {
        return destination;
    }
    
    /**
     * Gets the base price for this route.
     * This price serves as the foundation for calculating ticket prices.
     * 
     * @return The base price in RON
     */
    public double getBasePrice() {
        return basePrice;
    }
    
    /**
     * Updates the base price for this route.
     * 
     * @param basePrice The new base price in RON
     */
    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }
    
    /**
     * Returns a string representation of this Route object.
     * 
     * @return A string containing the route details
     */
    @Override
    public String toString() {
        return "Route{" +
                "origin=" + origin.getName() +
                ", destination=" + destination.getName() +
                ", basePrice=" + basePrice +
                '}';
    }
}