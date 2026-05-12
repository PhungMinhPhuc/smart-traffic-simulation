package model.vehicle.behavior;

public class EmergencyDriver extends DriverBehavior{
	 public EmergencyDriver() {
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
	 public String getBehaviorName(){
		 return "Emergency";
	 }
}