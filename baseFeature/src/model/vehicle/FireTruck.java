package model.vehicle;

import model.vehicle.behavior.DriverBehavior;

public class FireTruck extends Vehicle {
    private static final String DEFAULT_SOUND = "FireTruckSound.wav";
    private static final double DEFAULT_WIDTH = 25.0;
    private static final double DEFAULT_LENGTH = 55.0;
    private static final double DEFAULT_MAX_SPEED = 110.0;

    public FireTruck(DriverBehavior behavior) {
        super("FireTruck", DEFAULT_MAX_SPEED, DEFAULT_LENGTH, DEFAULT_WIDTH, DEFAULT_SOUND, behavior);
        this.isEmergency = true;
    }

    @Override
    public String toString() {
        return "FireTruck [" + behavior.getBehaviorName() + "]";
    }
}