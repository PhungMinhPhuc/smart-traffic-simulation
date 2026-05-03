package model.vehicle.behavior;

import model.vehicle.Vehicle;
import model.traffic.LightState;

public class AggressiveDriver implements DriverBehavior {
    // Xe này sẽ bám gần xe trước hơn, tăng tốc nhanh hơn
    private final double SAFE_TIME_GAP = 0.5;       // Cách xe trước 0.5s
    private final double MIN_STOP_DISTANCE = 5.0;   // Dừng sát
    private final double BRAKING_STRENGTH = -8.0;   // Phanh gấp 
    private final double STOP_LINE_SAFE_DISTANCE = 40.0; // Sát vạch hơn
    private final double AGGRESSIVE_ACCELERATION = 4.0;  

    @Override
    public double decideAcceleration(Vehicle self, Vehicle ahead) {
        double currentSpeed = self.getSpeed();
        
        if (self.getCurrentLane() != null && self.getCurrentLane().isRedLight()) {
            double distToLight = self.getPosition().distanceTo(self.getCurrentLane().getEndPoint());
            if (distToLight < STOP_LINE_SAFE_DISTANCE) {
                return calculateHardBraking(self, distToLight);
            }
        }

        if (ahead != null) {
            double distance = self.getPosition().distanceTo(ahead.getPosition());
            double dynamicSafeDist = (currentSpeed * SAFE_TIME_GAP) + MIN_STOP_DISTANCE;
            if (distance < dynamicSafeDist) {
                return (ahead.getSpeed() - currentSpeed) * 1.2 + BRAKING_STRENGTH * (1 - distance / dynamicSafeDist);
            }
        }

        double speedDiff = self.getMaxSpeed() - currentSpeed;
        return Math.min(AGGRESSIVE_ACCELERATION, speedDiff * 0.8);
    }

    private double calculateHardBraking(Vehicle self, double distance) {
        if (distance < 2.0) return 0;
        double requiredBraking = -(Math.pow(self.getSpeed(), 2) / (2 * distance));
        return Math.max(requiredBraking, BRAKING_STRENGTH);
    }

    @Override
    public double onRedLight(Vehicle self, LightState state, double distanceToLight) {
        return calculateHardBraking(self, distanceToLight);
    }

    @Override
    public void onEmergency(Vehicle self, Vehicle emergencyVehicle) {
        double dist = self.getPosition().distanceTo(emergencyVehicle.getPosition());
        if (dist < 50.0) {
            self.setAcceleration(BRAKING_STRENGTH * 0.5); 
        }
    }

    @Override
    public boolean shouldChangeLane(Vehicle self, Vehicle ahead) {
        if (ahead == null) return false;
        double distance = self.getPosition().distanceTo(ahead.getPosition());
        if (distance < 100.0 && ahead.getSpeed() < self.getMaxSpeed() * 0.95) {
            return true; 
        }
        return false;
    }

    @Override
    public String getBehaviorName() {
        return "Aggressive";
    }
}