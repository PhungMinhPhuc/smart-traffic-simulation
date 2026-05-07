package model.vehicle;

import model.vehicle.behavior.DriverBehavior;
import model.map.*;

public class Motorbike extends Vehicle {
    private static final String DEFAULT_SOUND = "MotorbikeSound.wav";
    private static final double DEFAULT_WIDTH = 10.0;
    private static final double DEFAULT_LENGTH = 20.0;
    private static final double DEFAULT_MAX_SPEED = 80.0;

    public Motorbike(Point position, Lane currentLane, DriverBehavior behavior) {
        super("Motorbike", DEFAULT_MAX_SPEED, DEFAULT_LENGTH, DEFAULT_WIDTH, DEFAULT_SOUND, behavior);
        this.isEmergency = false;
        this.position = position;
        this.currentLane = currentLane;
    }

    public Motorbike(Point position, Lane currentLane) {
        this(position, currentLane, new model.vehicle.behavior.NormalDriver());
    }

    public String toString() {
        return "Motorbike [" + behavior.getBehaviorName() + "]";
    }
}