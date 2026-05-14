package model.vehicle;

import model.vehicle.behavior.DriverBehavior;
import model.utility.TrafficPoint;
import model.utility.TrafficVector;
import config.Constants;

public class Ambulance extends Vehicle {
    public Ambulance(TrafficPoint position, TrafficVector direction, DriverBehavior behavior) {
	   super("Ambulance", Constants.AMBULANCE_MAX_SPEED, Constants.AMBULANCE_LENGTH, Constants.AMBULANCE_WIDTH, Constants.AMBULANCE_SOUND,
			   position, direction, behavior);
   }

    public Ambulance(TrafficPoint position, TrafficVector direction) {
  	   this(position, direction, new model.vehicle.behavior.EmergencyDriver());
    }
    
    public String toString() {
        return "Ambulance [" + behavior.getBehaviorName() + "]";
    }
}