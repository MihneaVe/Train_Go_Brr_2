package pkcg.model;

public class Platform {
    private int number;
    
    public Platform(int number) {
        this.number = number;
    }
    
    public int getNumber() {
        return number;
    }
    
    @Override
    public String toString() {
        return "Platform{" +
                "number=" + number +
                '}';
    }
}