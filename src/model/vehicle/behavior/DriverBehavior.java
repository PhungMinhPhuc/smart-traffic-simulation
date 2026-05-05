package model.vehicle.behavior;

import model.vehicle.Vehicle;
import model.traffic.LightState;

public interface DriverBehavior {
	public void decide(Vehicle self, Vehicle ahead, double dist, boolean isRead);
//    double decideAcceleration(Vehicle self, Vehicle ahead);
//    double onRedLight(Vehicle self, LightState state, double distanceToLight);
//    void onEmergency(Vehicle self, Vehicle emergencyVehicle);
//    boolean shouldChangeLane(Vehicle self, Vehicle ahead);
//    String getBehaviorName(); // For Basic GUI display, return The name of the behavior (e.g., "Normal", "Aggressive", "Emergency").
}