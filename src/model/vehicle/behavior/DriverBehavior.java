package model.vehicle.behavior;

import model.vehicle.Vehicle;
import config.Constants;

public abstract class DriverBehavior {
	protected double maxSpeedRatio;       		// Tỉ lệ tốc độ mong muốn so với maxSpeed
    protected double speedUpAcceleration;        // Gia tốc tăng tốc bình thường
    protected double brakeAccelearation;      	// Gia tốc phanh bình thường
    
    public DriverBehavior(double speedRatio, double speedUpAcceleration, double brakeAccelearation){
    	this.maxSpeedRatio = speedRatio;
    	this.speedUpAcceleration = speedUpAcceleration;
    	this.brakeAccelearation = brakeAccelearation;
    }
    
    public void decide(Vehicle self, double distAhead, double speedAhead, double distLight, 
    		boolean isRed, boolean canRight, boolean canLeft, boolean onEmergency) {
        double freeWayAcceleration = handleFreeLane(self, distAhead);
        double aheadAcceleration = handleAheadVehicle(self, distAhead, speedAhead);
        double lightAcceleration = handleRedLight(self, distLight, isRed);
        double finalAccleration = Math.min(freeWayAcceleration, Math.min(aheadAcceleration, lightAcceleration));
        self.applyAcceleration(finalAccleration);
        
        int offset = 0;
        
        if (onEmergency) {
        	if (canLeft) offset = -1;
            else if (canRight) offset = 1;
        }
        else if (handleLaneChange(self, distAhead, speedAhead)) {
            if (canLeft) offset = -1;
            else if (canRight) offset = 1;
        }
        
        self.setPendingLaneChange(offset); 
    }
   
    protected double calculateBrakeToStop(double currentSpeed, double distance) {
        if (distance <= Constants.MUST_STOP_DISTANCE) return 0;
        double targetAcc = -(currentSpeed * currentSpeed) / (2 * distance);
        return Math.max(brakeAccelearation, targetAcc);
    }

    protected double handleFreeLane(Vehicle self, double distAhead) {
    	if (distAhead >= 0) return Double.MAX_VALUE;
        double targetSpeed = self.getMaxSpeed() * this.maxSpeedRatio;
        if (self.getSpeed() < targetSpeed) return speedUpAcceleration;
        else if (self.getSpeed() > targetSpeed) return brakeAccelearation;
        return 0.0;
    }
    
    protected double handleAheadVehicle(Vehicle self, double distAhead, double speedAhead) {
    	if (distAhead < 0) return Double.MAX_VALUE;
    	else if (distAhead <= Constants.MUST_STOP_DISTANCE) self.setSpeed(0.0);

        if (distAhead < Constants.SAFE_DISTANCE) return brakeAccelearation;
        else return speedUpAcceleration;
        
    }
    
    protected double handleRedLight(Vehicle self, double distLight, boolean isRed) {
        if (!isRed || distLight > Constants.SAFE_DISTANCE) return Double.MAX_VALUE;
        else if (distLight <= Constants.MUST_STOP_DISTANCE) self.setSpeed(0.0);
        return this.calculateBrakeToStop(self.getSpeed(), distLight);
    }
    
    protected boolean handleLaneChange(Vehicle self, double distAhead, double speedAhead) {
    	if (distAhead > 0) {
            if (distAhead < Constants.SAFE_DISTANCE
            		&& speedAhead < self.getSpeed())
            	return true;
            return false;
        }
    	return false;
    }
    
    protected boolean handleEmergency(boolean onEmergency) {
    	return onEmergency;
    }
    
    public abstract String getBehaviorName();

	public double getSpeedRatio() {
		return maxSpeedRatio;
	}
    
    
}