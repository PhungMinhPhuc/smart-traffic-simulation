package model.vehicle;

import model.vehicle.behavior.DriverBehavior;
import model.map.*;

public class Bicycle extends Vehicle {
    private static final String DEFAULT_SOUND = "Bell.wav";
    private static final double DEFAULT_WIDTH = 8.0;
    private static final double DEFAULT_LENGTH = 15.0;
    private static final double DEFAULT_MAX_SPEED = 20.0; 

    public Bicycle(Point position, Lane currentLane, DriverBehavior behavior) {
        super("Bicycle", DEFAULT_MAX_SPEED, DEFAULT_LENGTH, DEFAULT_WIDTH, DEFAULT_SOUND, behavior);
        this.isEmergency = false;
        this.position = position;
        this.currentLane = currentLane;
    }

    public Bicycle(Point position, Lane currentLane) {
        this(position, currentLane, new model.vehicle.behavior.CautiousDriver());
    }

    public String toString() {
        return "Bicycle [" + behavior.getBehaviorName() + "]";
    }
}