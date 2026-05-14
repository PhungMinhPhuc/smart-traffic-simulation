package model.vehicle.behavior;

import model.vehicle.Vehicle;
import config.Constants;

public class AggressiveDriver extends DriverBehavior {
    public AggressiveDriver() {
        super(Constants.AGGRESSIVE_SPEED_RATIO, Constants.AGGRESSIVE_ACCELERATION, Constants.AGGRESSIVE_BRAKING);
    }

    @Override
    protected double calculateAccelerationForFreeLane(Vehicle self, double distanceToVehicleAhead) {
        // If lane is blocked, ignore this calculation
        if (distanceToVehicleAhead >= 0) {
            return Double.MAX_VALUE;
        }

        double targetSpeed = self.getMaxSpeed() * this.maxSpeedRatio;
        if (self.getSpeed() < targetSpeed)
            return this.speedUpAcceleration;
        else if (self.getSpeed() > targetSpeed)
            return this.brakeAcceleration;
        return 0.0;
    }

    @Override
    protected double calculateAccelerationForRedLight(Vehicle self, double distanceToLight, boolean isRed) {
        // Aggressive drivers ignore red lights
        return Double.MAX_VALUE;
    }

    @Override
    public boolean handleEmergency(boolean onEmergency) {
        return false;
    }

    @Override
    public String getBehaviorName() {
        return "Aggressive";
    }
}