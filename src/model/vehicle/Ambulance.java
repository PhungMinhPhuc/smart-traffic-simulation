package model.vehicle;

import model.vehicle.behavior.DriverBehavior;
import model.map.*;

public class Ambulance extends Vehicle {
    private static final String DEFAULT_SOUND = "AmbulanceSirens.wav";
    private static final double DEFAULT_WIDTH = 22.0;
    private static final double DEFAULT_LENGTH = 45.0;
    private static final double DEFAULT_MAX_SPEED = 120.0;

    public Ambulance(Point position, Lane currentLane, DriverBehavior behavior) {
        super("Ambulance", DEFAULT_MAX_SPEED, DEFAULT_LENGTH, DEFAULT_WIDTH, DEFAULT_SOUND, behavior);
        this.isEmergency = true; 
        this.position = position;
        this.currentLane = currentLane;
    }

    public Ambulance(Point position, Lane currentLane) {
        this(position, currentLane, new model.vehicle.behavior.AggressiveDriver());
    }

    public String toString() {
        return "Ambulance [" + behavior.getBehaviorName() + "]";
    }
}