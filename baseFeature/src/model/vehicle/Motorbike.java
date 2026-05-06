package model.vehicle;

import model.vehicle.behavior.DriverBehavior;

public class Motorbike extends Vehicle {
    private static final String DEFAULT_SOUND = "MotorbikeSound.wav";
    private static final double DEFAULT_WIDTH = 10.0;
    private static final double DEFAULT_LENGTH = 20.0;
    private static final double DEFAULT_MAX_SPEED = 90.0;

    public Motorbike(DriverBehavior behavior) {
        super("Motorbike", DEFAULT_MAX_SPEED, DEFAULT_LENGTH, DEFAULT_WIDTH, DEFAULT_SOUND, behavior);
        this.isEmergency = false;
    }

    @Override
    public String toString() {
        return "Motorbike [" + behavior.getBehaviorName() + "]";
    }
}