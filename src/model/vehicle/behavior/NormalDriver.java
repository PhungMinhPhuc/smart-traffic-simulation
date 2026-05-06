package model.vehicle.behavior;

public class NormalDriver extends DriverBehavior {
    public NormalDriver() {
        this.speedRatio = 0.8;
        this.safeTimeGap = 2.0;
        this.accStrong = 2.0;
        this.accNormal = 1.0;
        this.brakeNormal = -1.0;
        this.brakeStrong = -2.0;
        this.sightDistance = 150.0;
        this.overtakeThreshold = 0.7;
    }
   
    @Override
    public String getBehaviorName() {
    	return "Normal";
    }
}