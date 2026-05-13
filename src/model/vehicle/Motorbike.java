package model.vehicle;

import model.vehicle.behavior.DriverBehavior;
import model.utility.TrafficPoint;
import model.utility.TrafficVector;

public class Motorbike extends Vehicle {
    private static final String DEFAULT_SOUND = "MotorbikeSound.wav";
    private static final double DEFAULT_WIDTH = 10.0;
    private static final double DEFAULT_LENGTH = 20.0;
    private static final double DEFAULT_MAX_SPEED = 80.0;

    public Motorbike(TrafficPoint position, TrafficVector direction, DriverBehavior behavior) {
	   super("Car", DEFAULT_MAX_SPEED, DEFAULT_LENGTH, DEFAULT_WIDTH, DEFAULT_SOUND,
			   position, direction, behavior);
   }

    public Motorbike(TrafficPoint position, TrafficVector direction) {
 	   this(position, direction, new model.vehicle.behavior.NormalDriver());
    }

    public String toString() {
        return "Motorbike [" + behavior.getBehaviorName() + "]";
    }
}