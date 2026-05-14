package model.vehicle.behavior;

import model.vehicle.Vehicle;

public class EmergencyDriver extends DriverBehavior{
	 public EmergencyDriver() {
		super(1, 20.0, -40.0); //double maxSpeedRatio, double speedUpAcceleration, double brakeAccelearation
	}

	 @Override
	 protected double handleFreeLane(Vehicle self, double distAhead) {
	    	if (distAhead >= 0) return Double.MAX_VALUE;
	        double targetSpeed = self.getMaxSpeed() * this.maxSpeedRatio;
	        if (self.getSpeed() < targetSpeed) return this.speedUpAcceleration;
	        else if (self.getSpeed() > targetSpeed) return this.brakeAccelearation;
	        return 0.0;
	    }
	 
	 @Override
	    protected double handleRedLight(Vehicle self, double distLight, boolean isRed) {
	        return Double.MAX_VALUE;
	    }
	 
	 @Override
	 public String getBehaviorName(){
		 return "Emergency";
	 }
}
