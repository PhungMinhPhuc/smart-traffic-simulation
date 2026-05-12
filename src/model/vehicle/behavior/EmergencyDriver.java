package model.vehicle.behavior;

import model.vehicle.Vehicle;
import model.traffic.LightState;

public class EmergencyDriver implements DriverBehavior {
    private final double MIN_SAFE_DISTANCE = 15.0;
    private final double SAFE_DISTANCE = 60.0;
    private final double EMERGENCY_ACCELERATION = 2.5; 
    private final double EMERGENCY_BRAKING = -5.0; // Acceleration when braking

    @Override
    public double decideAcceleration(Vehicle self, Vehicle ahead) {
        double currentSpeed = self.getSpeed();

        // Ignore Traffic Lights

        // Handling Obstacles (Vehicles ahead)
        if (ahead != null) {
            double distance = self.getPosition().distanceTo(ahead.getPosition());
            
            if (distance < MIN_SAFE_DISTANCE) {
                return EMERGENCY_BRAKING;
            }
            else if (distance < SAFE_DISTANCE) {
                // If someone is in front, slow down slightly but keep pressure and wait for them to yield (move aside)
                return (ahead.getSpeed() - currentSpeed) * 0.5;
            }
        }

        // Maximum Pursuit: Try to reach max speed quickly
        if (currentSpeed < self.getMaxSpeed()) {
            return EMERGENCY_ACCELERATION;
        }

        return 0;
    }

    // Priority vehicles pass through red lights.
    @Override
    public double onRedLight(Vehicle self, LightState state, double distanceToLight) {
        return 0.5; // Small positive acceleration to keep moving
    }

    // Emergency vehicles don't yield to others; they are the priority.
    @Override
    public void onEmergency(Vehicle self, Vehicle otherEmergency) {
    }

    // Aggressive Lane Changing. Logic: If a lane is blocked, an ambulance should change lanes immediately
    @Override
    public boolean shouldChangeLane(Vehicle self, Vehicle ahead) {
        return (ahead != null);
    }

    @Override
    public String getBehaviorName() {
        return "Emergency";
    }
}