package model.vehicle.behavior;

import model.vehicle.Vehicle;

public class CautiousDriver extends DriverBehavior {
	
	 public CautiousDriver() {
			super(0.6, 8.0, -12.0); //double maxSpeedRatio, double speedUpAcceleration, double brakeAccelearation
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