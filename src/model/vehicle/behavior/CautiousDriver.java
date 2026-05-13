package model.vehicle.behavior;

import model.vehicle.Vehicle;

public class CautiousDriver extends DriverBehavior {
    public CautiousDriver() {
        this.speedRatio = 0.6;      
        this.safeTimeGap = 3.0;    
        this.accNormal = 0.8;       
        this.accStrong = 1.5;       
        this.brakeNormal = -1.2;    
        this.brakeStrong = -2.5;    
        this.sightDistance = 200.0; 
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