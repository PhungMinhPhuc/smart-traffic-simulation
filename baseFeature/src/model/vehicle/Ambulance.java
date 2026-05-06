package model.vehicle;

import model.vehicle.behavior.DriverBehavior;

public class Ambulance extends Vehicle {
    private static final String DEFAULT_SOUND = "AmbulanceSound.wav";
    private static final double DEFAULT_WIDTH = 20.0;
    private static final double DEFAULT_LENGTH = 45.0;
    private static final double DEFAULT_MAX_SPEED = 120.0;

    public Ambulance(DriverBehavior behavior) {
        super("Ambulance", DEFAULT_MAX_SPEED, DEFAULT_LENGTH, DEFAULT_WIDTH, DEFAULT_SOUND, behavior);
        this.isEmergency = true;
    }

    @Override
    public String toString() {
        return "Ambulance [" + behavior.getBehaviorName() + "]";
    }
}