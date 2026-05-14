package model.vehicle;

import model.vehicle.behavior.DriverBehavior;
import model.utility.TrafficPoint;
import model.utility.TrafficVector;
import config.Constants;

public class Truck extends Vehicle {
    public Truck(TrafficPoint position, TrafficVector direction, DriverBehavior behavior) {
	   super("Truck", Constants.TRUCK_MAX_SPEED, Constants.TRUCK_LENGTH, Constants.TRUCK_WIDTH, Constants.TRUCK_SOUND,
			   position, direction, behavior);
   }

    public Truck(TrafficPoint position, TrafficVector direction) {
  	   this(position, direction, new model.vehicle.behavior.CautiousDriver());
    }

    public String toString() {
        return "Truck [" + behavior.getBehaviorName() + "]";
    }
}