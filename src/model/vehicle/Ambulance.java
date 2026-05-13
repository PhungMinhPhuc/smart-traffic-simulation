package model.vehicle;

import model.vehicle.behavior.DriverBehavior;
import model.map.*;
import model.utility.TrafficPoint;
import model.utility.TrafficVector;

public class Ambulance extends Vehicle {
    private static final String DEFAULT_SOUND = "AmbulanceSirens.wav";
    private static final double DEFAULT_WIDTH = 22.0;
    private static final double DEFAULT_LENGTH = 45.0;
    private static final double DEFAULT_MAX_SPEED = 120.0;

    public Ambulance(TrafficPoint position, TrafficVector direction, DriverBehavior behavior) {
 	   super(DEFAULT_MAX_SPEED, DEFAULT_LENGTH, DEFAULT_WIDTH, DEFAULT_SOUND);
 	   this.type = "Car";
 	   this.isEmergency = false;
 	   this.position = position;
 	   this.direction = direction;
 	   this.behavior = behavior;
    }

    public Ambulance(TrafficPoint position, TrafficVector direction) {
 	   this(position, direction, new model.vehicle.behavior.EmergencyDriver());
    }
    public String toString() {
        return "Ambulance [" + behavior.getBehaviorName() + "]";
    }
}