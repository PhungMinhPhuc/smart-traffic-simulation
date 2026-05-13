package model.vehicle;

import model.vehicle.behavior.DriverBehavior;
import model.map.*;

public class Truck extends Vehicle {
    private static final String DEFAULT_SOUND = "TruckSound.wav";
    private static final double DEFAULT_WIDTH = 30.0; 
    private static final double DEFAULT_LENGTH = 80.0; 
    private static final double DEFAULT_MAX_SPEED = 60.0;

    public Truck(Point position, Lane currentLane, DriverBehavior behavior) {
        super("Truck", DEFAULT_MAX_SPEED, DEFAULT_LENGTH, DEFAULT_WIDTH, DEFAULT_SOUND, behavior);
        this.isEmergency = false;
        this.position = position;
        this.currentLane = currentLane;
    }

    public Truck(Point position, Lane currentLane) {
        this(position, currentLane, new model.vehicle.behavior.CautiousDriver());
    }

    public String toString() {
        return "Truck [" + behavior.getBehaviorName() + "]";
    }
}