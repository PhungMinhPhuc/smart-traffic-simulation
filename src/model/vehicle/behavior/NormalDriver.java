package model.vehicle.behavior;

import model.vehicle.Vehicle;
import model.traffic.LightState;

public class NormalDriver implements DriverBehavior {
    private final double SAFE_TIME_GAP = 1.5; // Seconds of gap to the car ahead
    private final double MIN_STOP_DISTANCE = 40.0; // Minimum pixels to keep when stopped
    private final double BRAKING_STRENGTH = -20.0; // Increased to allow stopping within 400px
    private final double STOP_LINE_SAFE_DISTANCE = 400.0;
    private final double SAFE_DISTANCE = 150.0;
    // private final double ALREADY_STOPPED = 5.0;
    private final double NORMAL_ACCELERATION = 20.0;

    @Override
    public double decideAcceleration(Vehicle self, Vehicle ahead) {
        double currentSpeed = self.getSpeed();
        double targetAcceleration = NORMAL_ACCELERATION; // Default: try to speed up

        // Check for Traffic Light (if on a Lane)
        if (self.getCurrentLane() != null && self.getCurrentLane().isRedLight()) {
            double distToLight = self.getPosition().distanceTo(self.getCurrentLane().getEndPoint());

            // If we are already at or past the MIN_STOP_DISTANCE, stop completely
            if (distToLight <= MIN_STOP_DISTANCE) {
                return (currentSpeed > 0.1) ? BRAKING_STRENGTH : 0;
            }

            // If the light is red/yellow and we are within range, calculate braking
            if (distToLight < STOP_LINE_SAFE_DISTANCE) {
                return calculateBraking(self, distToLight);
            }
        }

        // Check for Vehicle Ahead
        if (ahead != null) {
            double distance = self.getPosition().distanceTo(ahead.getPosition());
            // Safe distance increases with speed: Distance = (Speed * Time) + Buffer
            double dynamicSafeDist = (currentSpeed * SAFE_TIME_GAP) + MIN_STOP_DISTANCE;

            if (distance < dynamicSafeDist) {
                // If the car ahead is slower or we are too close, slow down
                return (ahead.getSpeed() - currentSpeed) * 0.8 + BRAKING_STRENGTH * (1 - distance / dynamicSafeDist);
            }
        }

        // Free road logic: Smoothly reach Max Speed
        double speedDiff = self.getMaxSpeed() - currentSpeed;
        return Math.min(targetAcceleration, speedDiff * 0.5);
    }

    // Logic: Stop exactly at MIN_STOP_DISTANCE before the red light.
    private double calculateBraking(Vehicle self, double distanceToLight) {
        double availableDistance = distanceToLight - MIN_STOP_DISTANCE;

        if (availableDistance <= 0.5)
            return BRAKING_STRENGTH;

        // V^2 = 2as => a = V^2 / 2s
        double requiredBraking = -(Math.pow(self.getSpeed(), 2) / (2 * availableDistance));
        return Math.max(requiredBraking, BRAKING_STRENGTH);
    }

    // This is handled inside decideAcceleration for consistency
    @Override
    public double onRedLight(Vehicle self, LightState state, double distanceToLight) {
        return calculateBraking(self, distanceToLight);
    }

    // Yield to Emergency Vehicles. Logic: Slow down significantly or change lane to
    // allow the ambulance to pass.
    @Override
    public void onEmergency(Vehicle self, Vehicle emergencyVehicle) {

    }

    // Overtaking. Logic: If a car is slow in front, check if the neighbor lane is
    // better
    @Override
    public boolean shouldChangeLane(Vehicle self, Vehicle ahead) {
        if (ahead == null)
            return false;

        double distance = self.getPosition().distanceTo(ahead.getPosition());
        if (distance < SAFE_DISTANCE && ahead.getSpeed() < self.getMaxSpeed() * 0.7) {
            return true; // Wants to overtake
        }
        return false;
    }

    @Override
    public String getBehaviorName() {
        return "Normal";
    }
}