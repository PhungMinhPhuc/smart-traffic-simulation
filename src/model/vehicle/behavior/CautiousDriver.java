package model.vehicle.behavior;

import model.vehicle.Vehicle;

public class CautiousDriver extends DriverBehavior {
    public CautiousDriver() {
        this.maxSpeedRatio = 0.6;      
        this.speedUpAcceleration = 0.8;            
        this.brakeAccelearation = -1.2;     
    }

    @Override
    protected boolean handleLaneChange(Vehicle self, double DistAhead, double speedAhead) {
    	return false;
    }

    @Override
    public String getBehaviorName() {
        return "Cautious";
    }
}