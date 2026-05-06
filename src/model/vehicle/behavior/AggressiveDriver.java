package model.vehicle.behavior;

import model.vehicle.Vehicle;

public class AggressiveDriver extends DriverBehavior {
    public AggressiveDriver() {
        this.speedRatio = 1.0;      
        this.safeTimeGap = 1;    
        this.accNormal = 2.0;       
        this.accStrong = 4.0;       
        this.brakeNormal = -2.0;    
        this.brakeStrong = -4.0;   
        this.sightDistance = 200.0;
        this.overtakeThreshold = 0.9;
    }

    @Override
    protected double handleRedLight(Vehicle self, double distance, boolean isRed) {
        return Double.MAX_VALUE;
    }

    @Override
    public String getBehaviorName() {
        return "Aggressive";
    }
}