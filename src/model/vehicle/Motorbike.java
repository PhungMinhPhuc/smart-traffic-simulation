package model.vehicle;

import model.vehicle.behavior.DriverBehavior;
import model.utility.TrafficPoint;
import model.utility.TrafficVector;
import config.Constants;

public class Motorbike extends Vehicle {
    public Motorbike(TrafficPoint position, TrafficVector direction, DriverBehavior behavior) {
	   super("Motorbike", Constants.MOTORBIKE_MAX_SPEED, Constants.MOTORBIKE_LENGTH, Constants.MOTORBIKE_WIDTH, Constants.MOTORBIKE_SOUND,
			   position, direction, behavior);
   }

    public Motorbike(TrafficPoint position, TrafficVector direction) {
  	   this(position, direction, new model.vehicle.behavior.NormalDriver());
    }

    public String toString() {
        return "Motorbike [" + behavior.getBehaviorName() + "]";
    }
}