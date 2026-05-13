package model.vehicle;

import model.vehicle.behavior.DriverBehavior;
import model.utility.TrafficPoint;
import model.utility.TrafficVector;

public class Bus extends Vehicle {
    private static final String DEFAULT_SOUND = "BusSound.wav";
    private static final double DEFAULT_WIDTH = 28.0;
    private static final double DEFAULT_LENGTH = 70.0;
    private static final double DEFAULT_MAX_SPEED = 70.0;

    public Bus(TrafficPoint position, TrafficVector direction, DriverBehavior behavior) {
 	   super(DEFAULT_MAX_SPEED, DEFAULT_LENGTH, DEFAULT_WIDTH, DEFAULT_SOUND);
 	   this.type = "Car";
 	   this.isEmergency = false;
 	   this.position = position;
 	   this.direction = direction;
 	   this.behavior = behavior;
    }

    public Bus(TrafficPoint position, TrafficVector direction) {
 	   this(position, direction, new model.vehicle.behavior.NormalDriver());
    }

    public String toString() {
        return "Bus [" + behavior.getBehaviorName() + "]";
    }
}