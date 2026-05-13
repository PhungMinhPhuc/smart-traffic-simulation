package model.vehicle;

import model.vehicle.behavior.DriverBehavior;
import model.utility.TrafficPoint;
import model.utility.TrafficVector;

public class Bicycle extends Vehicle {
    private static final String DEFAULT_SOUND = "Bell.wav";
    private static final double DEFAULT_WIDTH = 8.0;
    private static final double DEFAULT_LENGTH = 15.0;
    private static final double DEFAULT_MAX_SPEED = 20.0; 

    public Bicycle(TrafficPoint position, TrafficVector direction, DriverBehavior behavior) {
 	   super(DEFAULT_MAX_SPEED, DEFAULT_LENGTH, DEFAULT_WIDTH, DEFAULT_SOUND);
 	   this.type = "Car";
 	   this.isEmergency = false;
 	   this.position = position;
 	   this.direction = direction;
 	   this.behavior = behavior;
    }

    public Bicycle(TrafficPoint position, TrafficVector direction) {
 	   this(position, direction, new model.vehicle.behavior.CautiousDriver());
    }

    public String toString() {
        return "Bicycle [" + behavior.getBehaviorName() + "]";
    }
}