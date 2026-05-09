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

    public void decide(Vehicle self, Vehicle ahead, double distToLight, boolean isRed) {
        double freeWayAcc = handleFreeWay(self);
        double followAcc = handleFollowVehicle(self, ahead);
        double lightAcc = handleRedLight(self, distToLight, isRed);
        double finalAcc = Math.min(freeWayAcc, Math.min(followAcc, lightAcc));
        handleLaneChange(self, ahead);
        self.applyAcceleration(finalAcc);
    }

    protected double calculateBrakeToStop(double currentSpeed, double distance) {
        if (distance <= 0.5) return 0;
        double targetAcc = -(currentSpeed * currentSpeed) / (2 * distance);
        return Math.max(brakeStrong, targetAcc);
    }
    
    protected void attemptLaneChange(Vehicle self) {
        if (self.getCurrentLane().getNeighborLane(-1) != null) self.changeLane(-1);
        else if (self.getCurrentLane().getNeighborLane(1) != null) self.changeLane(1);
    }

    protected double handleFreeWay(Vehicle self) {
        double targetSpeed = self.getMaxSpeed() * this.speedRatio;
        if (self.getSpeed() < targetSpeed) return this.accNormal;
        else if (self.getSpeed() > targetSpeed) return this.brakeNormal;
        return 0.0;
    }
    
    protected double handleFollowVehicle(Vehicle self, Vehicle ahead) {
    	if (ahead == null) return Double.MAX_VALUE;
    	
        double dist = self.getPosition().distanceTo(ahead.getPosition());
        double safetyGap = self.getSpeed() * this.safeTimeGap;

        if (dist < safetyGap)
            return (self.getSpeed() - ahead.getSpeed()) > 0 ? 
            		this.brakeStrong : this.brakeNormal;
        return this.accNormal;
    }
    
    protected double handleRedLight(Vehicle self, double distance, boolean isRed) {
        if (!isRed || distance > sightDistance) return Double.MAX_VALUE;
        return this.calculateBrakeToStop(self.getSpeed(), distance);
    }
    
    protected void handleLaneChange(Vehicle self, Vehicle ahead) {
    	if (ahead != null) {
            double dist = self.getPosition().distanceTo(ahead.getPosition());
            double targetSpeed = self.getMaxSpeed() * this.speedRatio;
            
            if (dist < targetSpeed * this.safeTimeGap && ahead.getSpeed() < targetSpeed * this.overtakeThreshold) {
                attemptLaneChange(self);
            }
        }
    }
    
    protected void handleEmergency(Vehicle self) {
    	attemptLaneChange(self);
    }
    
    public abstract String getBehaviorName();
}