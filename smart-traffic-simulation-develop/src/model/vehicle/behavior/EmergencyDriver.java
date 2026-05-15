package model.vehicle.behavior;

import model.vehicle.Vehicle;
import config.Constants;

public class EmergencyDriver extends DriverBehavior {
	public EmergencyDriver() {
		super(Constants.EMERGENCY_SPEED_RATIO, Constants.EMERGENCY_ACCELERATION, Constants.EMERGENCY_BRAKING);
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
		// Emergency vehicles ignore red lights
		return Double.MAX_VALUE;
	}

	@Override
	public String getBehaviorName() {
		return "Emergency";
	}
}
