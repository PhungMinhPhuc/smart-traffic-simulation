package model.vehicle.behavior;

import model.vehicle.Vehicle;
import model.traffic.LightState;

public class AggressiveDriver implements DriverBehavior {
    private final double SAFE_DISTANCE = 100.0;
    private final double AGGRESSIVE_ACCELERATION = 2.0;
    private final double ALREADY_STOPPED = 5.0;
    private final double RISKY_TIME_GAP = 0.5;
    private final double MIN_STOP_DISTANCE = 10.0;
    private final double LATE_BRAKING_DISTANCE = 50.0;
    private final double RED_LIGHT_RISK = 0.5; // 50% chance to run red

    @Override
    public double decideAcceleration(Vehicle self, Vehicle ahead) {
        double currentSpeed = self.getSpeed();
        if (self.getCurrentLane() != null && self.getCurrentLane().isRedLight()) {
            double distanceToLight = self.getPosition().distanceTo(self.getCurrentLane().getEndPoint());
            
            if (distanceToLight < LATE_BRAKING_DISTANCE) {
                return handleRedLightRisk(self, distanceToLight);
            }
        }

        // Handle Vehicle Ahead (Tailgating)
        if (ahead != null) {
            double distance = self.getPosition().distanceTo(ahead.getPosition());
            double riskySafeDist = (currentSpeed * RISKY_TIME_GAP) + MIN_STOP_DISTANCE;

            if (distance < riskySafeDist) {
                // Brake only enough to not hit them, but stay very close
                return (ahead.getSpeed() - currentSpeed) * 1.2;
            }
        }

        // Try to hit Max Speed as fast as possible
        if (currentSpeed < self.getMaxSpeed()) {
            return AGGRESSIVE_ACCELERATION;
        }

        return 0;
    }

    private double handleRedLightRisk(Vehicle self, double distanceToLight) {
        if (Math.random() < RED_LIGHT_RISK) {
            return AGGRESSIVE_ACCELERATION; // ignore red light
        }
        return calculateHardBraking(self, distanceToLight); // brake normally
    }

    private double calculateHardBraking(Vehicle self, double distance) {
        if (distance < ALREADY_STOPPED) return 0;
        double required = -(Math.pow(self.getSpeed(), 2) / (2 * distance));
        return Math.max(required, -5.0); 
    }

    // Even aggressive drivers must yield to ambulances
    @Override
    public void onEmergency(Vehicle self, Vehicle emergencyVehicle) {
        double dist = self.getPosition().distanceTo(emergencyVehicle.getPosition());
        if (dist < SAFE_DISTANCE) {
            self.setAcceleration(-4.0);
        }
    }

    // Aggressive drivers change lanes if the car in front is even slightly slower than their desired max speed.
    @Override
    public boolean shouldChangeLane(Vehicle self, Vehicle ahead) {
        if (ahead == null) return false;
        double distance = self.getPosition().distanceTo(ahead.getPosition());
        return (distance < SAFE_DISTANCE && ahead.getSpeed() < self.getMaxSpeed() * 0.95);
    }

    @Override
    public double onRedLight(Vehicle self, LightState state, double distanceToLight) {
        return handleRedLightRisk(self, distanceToLight);
    }

    @Override
    public String getBehaviorName() {
        return "Aggressive";
    }
}