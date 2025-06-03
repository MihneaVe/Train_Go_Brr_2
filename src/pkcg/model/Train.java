package pkcg.model;

/**
 * Represents a train in the railway system.
 * 
 * Trains are identified by a unique number, have a specific type (e.g., InterRegio, Regio),
 * and a passenger capacity that limits the number of tickets that can be sold.
 */
public class Train {
    private String number;
    private String type;
    private int capacity;
    
    /**
     * Constructs a new Train with the specified details.
     * 
     * @param number The unique identifier for this train
     * @param type The train type (e.g., InterRegio, Regio)
     * @param capacity The maximum number of passengers this train can accommodate
     */
    public Train(String number, String type, int capacity) {
        this.number = number;
        this.type = type;
        this.capacity = capacity;
    }
    
    /**
     * Gets the train number (identifier).
     * 
     * @return The train number
     */
    public String getNumber() {
        return number;
    }
    
    /**
     * Gets the train type.
     * 
     * @return The train type (e.g., InterRegio, Regio)
     */
    public String getType() {
        return type;
    }
    
    /**
     * Gets the passenger capacity of this train.
     * 
     * @return The maximum number of passengers
     */
    public int getCapacity() {
        return capacity;
    }
    
    /**
     * Returns a string representation of this Train object.
     * 
     * @return A string containing the train details
     */
    @Override
    public String toString() {
        return "Train{" +
                "number='" + number + '\'' +
                ", type='" + type + '\'' +
                ", capacity=" + capacity +
                '}';
    }
}