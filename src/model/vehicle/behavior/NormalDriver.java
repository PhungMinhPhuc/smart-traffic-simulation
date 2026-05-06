package model.vehicle.behavior;

import model.vehicle.Vehicle;

public class NormalDriver extends DriverBehavior {
	    protected static final double SPEED_RATIO_NORMAL = 0.8;
	    protected static final double SAFE_TIME_GAP = 2.0; 
	    protected static final double ACC_NORMAL = 1.5;
	    protected static final double BRAKE_NORMAL = -1.0;
	    protected static final double BRAKE_STRONG = -2.5;
	    protected static final double DIST_BRAKE_ZONE = 40.0;

    public NormalDriver() {
    	this.sightDistance = 150;
    }
    
    @Override
    protected void handleFreeWay(Vehicle self) {
    	double targetSpeed = self.getMaxSpeed() * SPEED_RATIO_NORMAL;
    	
    	if (self.getSpeed() < targetSpeed) self.applyAcceleration(ACC_NORMAL);     
        else if (self.getSpeed() > targetSpeed) self.applyAcceleration(BRAKE_NORMAL);
        else self.applyAcceleration(0.0);
    }
    
    @Override
    protected void handleFollowVehicle(Vehicle self, Vehicle ahead) {
        double dist = self.getPosition().distanceTo(ahead.getPosition());
        
        double safetyGap = self.getSpeed() * SAFE_TIME_GAP;

        if (dist < safetyGap) {
            double brakeForce = (self.getSpeed() - ahead.getSpeed()) > 0 ? BRAKE_STRONG : BRAKE_NORMAL;
            self.applyAcceleration(brakeForce);
        } 
        else {
            double targetSpeed = Math.min(self.getMaxSpeed() * SPEED_RATIO_NORMAL, ahead.getSpeed());
            if (self.getSpeed() < targetSpeed) self.applyAcceleration(ACC_NORMAL);    
            else self.applyAcceleration(0.0);
        }
    }
    
    @Override
    protected void handleRedLight(Vehicle self, double distance) {
        if (distance > 40.0) {
            self.applyAcceleration(-2.0);
        } 
        else if (distance > 5.0) {
            self.applyAcceleration(-4.0);
        } 
        else {
            self.setSpeed(0);
            self.applyAcceleration(0);
        }
    }
    
    @Override
    protected void handleEmergency(Vehicle self) {
        self.changeLane(1); 
    }

    @Override
    public String getBehaviorName() {
        return "Normal";
    }
}