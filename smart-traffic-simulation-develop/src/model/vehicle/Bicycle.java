package model.vehicle;

import model.vehicle.behavior.DriverBehavior;
import model.utility.TrafficPoint;
import model.utility.TrafficVector;
import config.Constants;

public class Bicycle extends Vehicle {
    public Bicycle(TrafficPoint position, TrafficVector direction, DriverBehavior behavior) {
	   super("Bicycle", Constants.BICYCLE_MAX_SPEED, Constants.BICYCLE_LENGTH, Constants.BICYCLE_WIDTH, Constants.BICYCLE_SOUND, Constants.BICYCLE_COLOR,
			   position, direction, behavior);
   }

    public Bicycle(TrafficPoint position, TrafficVector direction) {
  	   this(position, direction, new model.vehicle.behavior.CautiousDriver());
    }

    public String toString() {
        return "Bicycle [" + behavior.getBehaviorName() + "]";
    }
}