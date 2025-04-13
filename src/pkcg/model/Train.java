package pkcg.model;

public class Train {
    private String number;
    private String type;
    private int capacity;
    
    public Train(String number, String type, int capacity) {
        this.number = number;
        this.type = type;
        this.capacity = capacity;
    }
    
    public String getNumber() {
        return number;
    }
    
    public String getType() {
        return type;
    }
    
    public int getCapacity() {
        return capacity;
    }
    
    @Override
    public String toString() {
        return "Train{" +
                "number='" + number + '\'' +
                ", type='" + type + '\'' +
                ", capacity=" + capacity +
                '}';
    }
}