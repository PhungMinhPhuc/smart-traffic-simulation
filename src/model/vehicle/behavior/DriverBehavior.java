package model.vehicle.behavior;

import model.vehicle.Vehicle;

public abstract class DriverBehavior {
    protected double speedRatio;     
    protected double safeTimeGap;    
    protected double accNormal;      
    protected double accStrong;      
    protected double brakeNormal;   
    protected double brakeStrong;    
    protected double sightDistance; 
    protected double overtakeThreshold;

    public void decide(Vehicle self, double distToVehicleAhead, double speedVehicleAhead, double distToLight, 
    		boolean isRed, boolean canChangeToRight, boolean canChangeToLeft) {
        double freeWayAcc = handleFreeWay(self, distToVehicleAhead);
        double followAcc = handleFollowVehicle(self, distToVehicleAhead);
        double lightAcc = handleRedLight(self, distToLight, isRed);
        double finalAcc = Math.min(freeWayAcc, Math.min(followAcc, lightAcc));
        self.applyAcceleration(finalAcc);
    }
    
    protected double calculateBrakeToStop(double currentSpeed, double distance) {
        if (distance <= 0.5) return 0;
        double targetAcc = -(currentSpeed * currentSpeed) / (2 * distance);
        return Math.max(brakeStrong, targetAcc);
    }
    
    protected int attemptLaneChange(boolean canChangeToRight, boolean canChangeToLeft) {
        if (canChangeToRight) return 1;
        else if (canChangeToLeft) return -1;
        else return 0;
    }

    protected double handleFreeWay(Vehicle self, double distToVehicleAhead) {
    	if (distToVehicleAhead >= 0) return Double.MAX_VALUE;
        double targetSpeed = self.getMaxSpeed() * this.speedRatio;
        if (self.getSpeed() < targetSpeed) return this.accNormal;
        else if (self.getSpeed() > targetSpeed) return this.brakeNormal;
        return 0.0;
    }
    
    protected double handleFollowVehicle(Vehicle self, double distToVehicleAhead) {
    	if (distToVehicleAhead < 0) return Double.MAX_VALUE;
    	
        double safetyGap = self.getSpeed() * this.safeTimeGap;

        if (distToVehicleAhead < safetyGap / 2) return this.brakeStrong;
        else if (distToVehicleAhead < safetyGap) return this.brakeNormal;
        else return this.accNormal;
        
    }
    
    protected double handleRedLight(Vehicle self, double distance, boolean isRed) {
        if (!isRed || distance > sightDistance) return Double.MAX_VALUE;
        return this.calculateBrakeToStop(self.getSpeed(), distance);
    }
    
    protected int handleLaneChange(Vehicle self, double distTovehicleAhead) {
    	if (distTovehicleAhead > 0) {
            double targetSpeed = self.getMaxSpeed() * this.speedRatio;
            
            if (distTovehicleAhead < targetSpeed * this.safeTimeGap && ahead.getSpeed() < targetSpeed * this.overtakeThreshold) {
                return attemptLaneChange;
            }
        }
    }
    
    protected void handleEmergency(Vehicle self) {
    	attemptLaneChange(self);
    }
    
    public abstract String getBehaviorName();

	public double getSpeedRatio() {
		return speedRatio;
	}
    
    
}