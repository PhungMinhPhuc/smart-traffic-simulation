package model.vehicle;

import model.vehicle.behavior.DriverBehavior;

public class Car extends Vehicle {
    private static final String DEFAULT_SOUND = "CarSound.wav";
    private static final double DEFAULT_WIDTH = 20.0;
    private static final double DEFAULT_LENGTH = 40.0;
    private static final double DEFAULT_MAX_SPEED = 100.0;
    public Car(DriverBehavior behavior) {
        super("Car", DEFAULT_MAX_SPEED, DEFAULT_LENGTH, DEFAULT_WIDTH, DEFAULT_SOUND, behavior);
        this.isEmergency = false;
    }

    @Override
    public String toString() {
        return "Car [" + behavior.getBehaviorName() + "]";
    }
}