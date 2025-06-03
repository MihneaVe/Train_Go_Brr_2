package pkcg.model;

/**
 * Represents a platform in a railway station.
 * 
 * Platforms are numbered locations within a station where trains arrive and depart.
 * Each platform has a unique number within its station.
 */
public class Platform {
    private int number;
    
    /**
     * Constructs a new Platform with the specified number.
     * 
     * @param number The platform number within the station
     */
    public Platform(int number) {
        this.number = number;
    }
    
    /**
     * Gets the platform number.
     * 
     * @return The platform number
     */
    public int getNumber() {
        return number;
    }
    
    /**
     * Returns a string representation of this Platform object.
     * 
     * @return A string containing the platform number
     */
    @Override
    public String toString() {
        return "Platform{" +
                "number=" + number +
                '}';
    }
}