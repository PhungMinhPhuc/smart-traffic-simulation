package model.vehicle;

import model.vehicle.behavior.DriverBehavior;
import model.utility.TrafficPoint;
import model.utility.TrafficVector;

public class FireTruck extends Vehicle {
    private static final String DEFAULT_SOUND = "FireTruckSiren.wav";
    private static final double DEFAULT_WIDTH = 30.0; 
    private static final double DEFAULT_LENGTH = 85.0; 
    private static final double DEFAULT_MAX_SPEED = 110.0;

    public FireTruck(TrafficPoint position, TrafficVector direction, DriverBehavior behavior) {
 	   super(DEFAULT_MAX_SPEED, DEFAULT_LENGTH, DEFAULT_WIDTH, DEFAULT_SOUND);
 	   this.type = "Car";
 	   this.isEmergency = false;
 	   this.position = position;
 	   this.direction = direction;
 	   this.behavior = behavior;
    }

    public FireTruck(TrafficPoint position, TrafficVector direction) {
 	   this(position, direction, new model.vehicle.behavior.EmergencyDriver());
    }

    public String toString() {
        return "FireTruck [" + behavior.getBehaviorName() + "]";
    }
}