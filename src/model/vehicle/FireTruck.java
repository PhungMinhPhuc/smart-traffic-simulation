package model.vehicle;

import model.vehicle.behavior.DriverBehavior;
import model.utility.TrafficPoint;
import model.utility.TrafficVector;
import config.Constants;

public class FireTruck extends Vehicle {
    public FireTruck(TrafficPoint position, TrafficVector direction, DriverBehavior behavior) {
	   super("FireTruck", Constants.FIRE_TRUCK_MAX_SPEED, Constants.FIRE_TRUCK_LENGTH, Constants.FIRE_TRUCK_WIDTH, Constants.FIRE_TRUCK_SOUND,
			   position, direction, behavior);
   }

    public FireTruck(TrafficPoint position, TrafficVector direction) {
  	   this(position, direction, new model.vehicle.behavior.EmergencyDriver());
    }

    public String toString() {
        return "FireTruck [" + behavior.getBehaviorName() + "]";
    }
}