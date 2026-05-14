package model.vehicle.behavior;

import model.vehicle.Vehicle;

public class AggressiveDriver extends DriverBehavior {
	public AggressiveDriver() {
		super(1, 40.0, -40.0); //double maxSpeedRatio, double speedUpAcceleration, double brakeAccelearation
	}

    @Override
    protected double handleFreeLane(Vehicle self, double distToVehicleAhead) {
    	if (distToVehicleAhead >= 0) return Double.MAX_VALUE;
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
    public boolean handleEmergency(boolean onEmergency) {
    	return false;
    }
    
    @Override
    public String getBehaviorName() {
        return "Aggressive";
    }
}