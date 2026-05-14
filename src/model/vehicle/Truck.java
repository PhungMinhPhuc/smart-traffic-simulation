package model.vehicle;

import model.vehicle.behavior.DriverBehavior;
import model.utility.TrafficPoint;
import model.utility.TrafficVector;

public class Truck extends Vehicle {
    private static final String DEFAULT_SOUND = "TruckSound.wav";
    private static final double DEFAULT_WIDTH = 30.0; 
    private static final double DEFAULT_LENGTH = 80.0; 
    private static final double DEFAULT_MAX_SPEED = 60.0;

    public Truck(TrafficPoint position, TrafficVector direction, DriverBehavior behavior) {
	   super("Truck", DEFAULT_MAX_SPEED, DEFAULT_LENGTH, DEFAULT_WIDTH, DEFAULT_SOUND,
			   position, direction, behavior);
   }

    public Truck(TrafficPoint position, TrafficVector direction) {
 	   this(position, direction, new model.vehicle.behavior.CautiousDriver());
    }

    public String toString() {
        return "Truck [" + behavior.getBehaviorName() + "]";
    }
}