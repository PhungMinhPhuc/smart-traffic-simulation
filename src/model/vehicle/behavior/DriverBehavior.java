package model.vehicle.behavior;

import model.vehicle.Vehicle;
import config.Constants;

public abstract class DriverBehavior {
    protected double maxSpeedRatio; // Percentage of max speed driver aims for (e.g. 0.8 is 80%)
    protected double speedUpAcceleration;
    protected double brakeAcceleration;

    public DriverBehavior(double maxSpeedRatio, double speedUpAcceleration, double brakeAcceleration) {
        // Example: maxSpeedRatio = 0.8 means the driver aims for 80% of the vehicle's
        // max speed
        this.maxSpeedRatio = maxSpeedRatio;
        this.speedUpAcceleration = speedUpAcceleration;
        this.brakeAcceleration = brakeAcceleration;
    }

    // This method calculates the driver's plan: how much to accelerate and which
    // lane to choose
    public void decide(Vehicle self, double distanceToVehicleAhead, double speedOfVehicleAhead,
            double distanceToLight, boolean isRed, boolean canRight, boolean canLeft,
            double distLeft, double distRight, boolean onEmergency, boolean isChangingLane) {

        // Calculate acceleration needed for different situations
        double cruiseAcc = calculateAccelerationForFreeLane(self, distanceToVehicleAhead);
        
        // "Complete change lane before check vehicle ahead"
        // If we are currently changing lanes, we ignore the car ahead to ensure we finish the move
        double aheadAcc = isChangingLane ? Double.MAX_VALUE : calculateAccelerationForVehicleAhead(self, distanceToVehicleAhead, speedOfVehicleAhead);
        
        double lightAcc = calculateAccelerationForRedLight(self, distanceToLight, isRed);

        // Use the most restrictive acceleration (minimum value) for safety
        double finalAcc = Math.min(cruiseAcc, Math.min(aheadAcc, lightAcc));
        self.applyAcceleration(finalAcc);

        // Lane choice: -1 means move left, 1 means move right, 0 means stay
        int laneChangeDirection = 0;

        if (onEmergency) {
            if (canLeft) {
                laneChangeDirection = -1; // Yield to left
            } else if (canRight) {
                laneChangeDirection = 1; // Yield to right
            }
        } else if (checkIfLaneChangeIsNeeded(self, distanceToVehicleAhead, speedOfVehicleAhead, distanceToLight, isRed)) {
            // "Scan" which lane is better.
            double currentDist = (distanceToVehicleAhead == -1) ? Double.MAX_VALUE : distanceToVehicleAhead;
            double bestDist = currentDist;
            
            // Only change if the adjacent lane is significantly better (e.g. at least 30 pixels more space)
            double improvementThreshold = 30.0;
            
            if (canLeft) {
                double scoreLeft = (distLeft == -1) ? Double.MAX_VALUE : distLeft;
                if (scoreLeft > bestDist + improvementThreshold) {
                    laneChangeDirection = -1;
                    bestDist = scoreLeft;
                }
            }
            if (canRight) {
                double scoreRight = (distRight == -1) ? Double.MAX_VALUE : distRight;
                if (scoreRight > bestDist + improvementThreshold) {
                    laneChangeDirection = 1;
                }
            }
        }

        self.setLaneChangeDirection(laneChangeDirection);
    }

    // Uses physical formula: v^2 = u^2 + 2as => a = (v_final^2 - v_initial^2) / (2
    // * distance)
    protected double calculateBrakeToStop(double currentSpeed, double distance) {
        if (distance <= Constants.MUST_STOP_DISTANCE) {
            return 0.0;
        }
        double requiredBraking = -(currentSpeed * currentSpeed) / (2 * distance);

        // Ensure braking is not weaker than comfort limit, but not stronger than
        // physical max
        return Math.max(brakeAcceleration, requiredBraking);
    }

    protected double calculateAccelerationForFreeLane(Vehicle self, double distanceToVehicleAhead) {
        // If there's a vehicle ahead (distance >= 0), this logic doesn't apply
        if (distanceToVehicleAhead >= 0) {
            return Double.MAX_VALUE;
        }

        double targetSpeed = self.getMaxSpeed() * this.maxSpeedRatio;
        if (self.getSpeed() < targetSpeed)
            return speedUpAcceleration;
        else if (self.getSpeed() > targetSpeed)
            return brakeAcceleration;
        return 0.0;
    }

    protected double calculateAccelerationForVehicleAhead(Vehicle self, double distanceToVehicleAhead,
            double speedOfVehicleAhead) {
        // If no vehicle ahead (distance < 0), this logic doesn't apply
        if (distanceToVehicleAhead < 0) {
            return Double.MAX_VALUE;
        }

        // Emergency stop if the gap is too small
        if (distanceToVehicleAhead <= Constants.MUST_STOP_DISTANCE) {
            self.setSpeed(0.0);
            self.applyAcceleration(0.0);
            return 0.0;
        }

        // Maintain safe distance
        if (distanceToVehicleAhead < Constants.SAFE_DISTANCE) {
            return brakeAcceleration;
        } else {
            return speedUpAcceleration;
        }
    }

    protected double calculateAccelerationForRedLight(Vehicle self, double distanceToLight, boolean isRed) {
        if (!isRed || distanceToLight > Constants.SAFE_DISTANCE) {
            return Double.MAX_VALUE;
        }

        // Stop at the line if very close
        if (distanceToLight <= Constants.MUST_STOP_DISTANCE) {
            self.setSpeed(0.0);
            self.applyAcceleration(0.0);
            return 0.0;
        }

        return this.calculateBrakeToStop(self.getSpeed(), distanceToLight);
    }

    protected boolean checkIfLaneChangeIsNeeded(Vehicle self, double distanceToVehicleAhead,
            double speedOfVehicleAhead, double distanceToLight, boolean isRed) {
        
        // Don't change lanes if stopped or stopping for a red light
        if (self.getSpeed() < 5.0) {
            return false;
        }

        if (isRed && distanceToLight < 120.0) {
            return false;
        }

        if (distanceToVehicleAhead > 0) {
            // Change lane if blocked and neighbor lane is faster
            if (distanceToVehicleAhead < Constants.SAFE_DISTANCE * 0.75 && speedOfVehicleAhead < self.getMaxSpeed() * this.getSpeedRatio()) {
                return true;
            }
        }
        return false;
    }

    protected boolean handleEmergency(boolean onEmergency) {
        return onEmergency;
    }

    public abstract String getBehaviorName();

    public double getSpeedRatio() {
        return maxSpeedRatio;
    }
}