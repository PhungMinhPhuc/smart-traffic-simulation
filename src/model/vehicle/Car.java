package model.vehicle;

import model.vehicle.behavior.DriverBehavior;
import model.utility.TrafficPoint;
import model.utility.TrafficVector;

public class Car extends Vehicle {
    private static final String DEFAULT_SOUND = "CarSound.wav";
    private static final double DEFAULT_WIDTH = 20.0;
    private static final double DEFAULT_LENGTH = 40.0;
    private static final double DEFAULT_MAX_SPEED = 100.0;
    
   public Car(TrafficPoint position, TrafficVector direction, DriverBehavior behavior) {
	   super("Car", DEFAULT_MAX_SPEED, DEFAULT_LENGTH, DEFAULT_WIDTH, DEFAULT_SOUND,
			   position, direction, behavior);
   }

   public Car(TrafficPoint position, TrafficVector direction) {
	   this(position, direction, new model.vehicle.behavior.NormalDriver());
   }
   
    public String toString() {
        return "Car [" + behavior.getBehaviorName() + "]";
    }
}