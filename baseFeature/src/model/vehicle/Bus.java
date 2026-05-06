package model.vehicle;

import model.vehicle.behavior.DriverBehavior;

public class Bus extends Vehicle {
    private static final String DEFAULT_SOUND = "BusSound.wav";
    private static final double DEFAULT_WIDTH = 25.0;
    private static final double DEFAULT_LENGTH = 60.0;
    private static final double DEFAULT_MAX_SPEED = 60.0;

    public Bus(DriverBehavior behavior) {
        super("Bus", DEFAULT_MAX_SPEED, DEFAULT_LENGTH, DEFAULT_WIDTH, DEFAULT_SOUND, behavior);
        this.isEmergency = false;
    }

    @Override
    public String toString() {
        return "Bus [" + behavior.getBehaviorName() + "]";
    }
}