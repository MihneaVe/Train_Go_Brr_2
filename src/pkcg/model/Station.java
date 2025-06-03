package pkcg.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a railway station with multiple platforms.
 * 
 * Stations are the nodes in the railway network where trains arrive and depart.
 * Each station has a unique name and contains multiple platforms where trains can stop.
 */
public class Station {
    private String name;
    private int platformCount;
    private List<Platform> platforms;
    
    /**
     * Constructs a new Station with the specified name and number of platforms.
     * Automatically initializes the platforms with sequential numbering.
     * 
     * @param name The unique name of the station
     * @param platformCount The number of platforms at this station
     */
    public Station(String name, int platformCount) {
        this.name = name;
        this.platformCount = platformCount;
        this.platforms = new ArrayList<>();
        
        // Initialize platforms with sequential numbers
        for (int i = 1; i <= platformCount; i++) {
            platforms.add(new Platform(i));
        }
    }
    
    /**
     * Gets the name of this station.
     * 
     * @return The station name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Gets the list of all platforms at this station.
     * 
     * @return The list of platforms
     */
    public List<Platform> getPlatforms() {
        return platforms;
    }
    
    /**
     * Finds and returns a specific platform by its number.
     * 
     * @param number The platform number to find
     * @return The platform with the specified number, or null if not found
     */
    public Platform getPlatform(int number) {
        return platforms.stream()
            .filter(p -> p.getNumber() == number)
            .findFirst()
            .orElse(null);
    }
    
    /**
     * Returns a string representation of this Station object.
     * 
     * @return A string containing the station name and platform count
     */
    @Override
    public String toString() {
        return "Station{" +
                "name='" + name + '\'' +
                ", platforms=" + platformCount +
                '}';
    }
}