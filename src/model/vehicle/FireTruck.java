package model.vehicle;

import model.vehicle.behavior.DriverBehavior;
import model.map.*;

public class FireTruck extends Vehicle {
    private static final String DEFAULT_SOUND = "FireTruckSiren.wav";
    private static final double DEFAULT_WIDTH = 30.0; 
    private static final double DEFAULT_LENGTH = 85.0; 
    private static final double DEFAULT_MAX_SPEED = 110.0;

    public FireTruck(Point position, Lane currentLane, DriverBehavior behavior) {
        super("FireTruck", DEFAULT_MAX_SPEED, DEFAULT_LENGTH, DEFAULT_WIDTH, DEFAULT_SOUND, behavior);
        this.isEmergency = true; 
        this.position = position;
        this.currentLane = currentLane;
    }

    public FireTruck(Point position, Lane currentLane) {
        this(position, currentLane, new model.vehicle.behavior.AggressiveDriver());
    }

    public String toString() {
        return "FireTruck [" + behavior.getBehaviorName() + "]";
    }
}