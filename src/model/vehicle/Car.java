package model.vehicle;

import model.vehicle.behavior.DriverBehavior;
import model.utility.TrafficPoint;
import model.utility.TrafficVector;

public class Car extends Vehicle {
    private static final String DEFAULT_SOUND = "CarSound.wav";
    private static final double DEFAULT_WIDTH = 30.0;
    private static final double DEFAULT_LENGTH = 15.0;
    private static final double DEFAULT_MAX_SPEED = 100.0;

    public Car() {
        super("Car", DEFAULT_MAX_SPEED, DEFAULT_LENGTH, DEFAULT_WIDTH, DEFAULT_SOUND);
        this.isEmergency = false;
    }

    public Car(TrafficPoint position, TrafficVector direction) {
        super("Car", DEFAULT_MAX_SPEED, DEFAULT_LENGTH, DEFAULT_WIDTH, DEFAULT_SOUND);
        this.position = position;
        this.direction = direction.clone();
        this.speed = 60.0;
        this.isEmergency = false;
    }

    @Override
    public String toString() {
        return "Car [" + behavior.getBehaviorName() + "]";
    }
}