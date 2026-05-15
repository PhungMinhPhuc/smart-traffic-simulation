package model.vehicle;

import model.vehicle.behavior.DriverBehavior;
import model.utility.TrafficPoint;
import model.utility.TrafficVector;
import config.Constants;

public class Bus extends Vehicle {
    public Bus(TrafficPoint position, TrafficVector direction, DriverBehavior behavior) {
	   super("Bus", Constants.BUS_MAX_SPEED, Constants.BUS_LENGTH, Constants.BUS_WIDTH, Constants.BUS_SOUND, Constants.BUS_COLOR,
			   position, direction, behavior);
   }

    public Bus(TrafficPoint position, TrafficVector direction) {
  	   this(position, direction, new model.vehicle.behavior.NormalDriver());
    }

    public String toString() {
        return "Bus [" + behavior.getBehaviorName() + "]";
    }
}