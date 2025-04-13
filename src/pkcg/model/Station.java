package pkcg.model;

import java.util.ArrayList;
import java.util.List;

public class Station {
    private String name;
    private int platformCount;
    private List<Platform> platforms;
    
    public Station(String name, int platformCount) {
        this.name = name;
        this.platformCount = platformCount;
        this.platforms = new ArrayList<>();
        
        // Initialize platforms
        for (int i = 1; i <= platformCount; i++) {
            platforms.add(new Platform(i));
        }
    }
    
    public String getName() {
        return name;
    }
    
    public List<Platform> getPlatforms() {
        return platforms;
    }
    
    public Platform getPlatform(int number) {
        return platforms.stream()
            .filter(p -> p.getNumber() == number)
            .findFirst()
            .orElse(null);
    }
    
    @Override
    public String toString() {
        return "Station{" +
                "name='" + name + '\'' +
                ", platforms=" + platformCount +
                '}';
    }
}