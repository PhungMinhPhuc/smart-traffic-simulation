package model.vehicle.behavior;

public class NormalDriver extends DriverBehavior {
	 public NormalDriver() {
			super(0.8, 20.0, -20.0); //double maxSpeedRatio, double speedUpAcceleration, double brakeAccelearation
		}
   
    @Override
    public String getBehaviorName() {
    	return "Normal";
    }
}