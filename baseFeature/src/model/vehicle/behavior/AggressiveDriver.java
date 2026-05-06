package model.vehicle.behavior;

import model.vehicle.Vehicle;

public class AggressiveDriver extends DriverBehavior {

    public AggressiveDriver() {
        this.safeTimeGap = 1.0;
        this.minDistance = 3.0;
        this.accelerationValue = 2.5;
        this.brakingValue = -3.5;
        this.stopLineDistance = 25.0;
    }

    @Override
    protected void handleFreeWay(Vehicle self) {
        self.applyAcceleration(accelerationValue);
    }

    @Override
    protected void handleFollowVehicle(Vehicle self, Vehicle ahead) {
        double dist = distance(self, ahead);
        double safe = safeDistance(self);

        if (dist < safe) {
            // Luôn tìm cách vượt
            if (shouldChangeLane(self, -1)) {
                self.changeLane(-1);
                return;
            } else if (shouldChangeLane(self, 1)) {
                self.changeLane(1);
                return;
            }
            // không vượt được thì phanh
            self.applyAcceleration(brakingValue);
        } 
        else {
            self.applyAcceleration(accelerationValue);
        }
    }

    @Override
    protected void handleRedLight(Vehicle self, double distance) {
        if (distance > 10) {
            self.applyAcceleration(brakingValue); // sát vạch đèn đỏ mới phanh
        } 
        else {
            self.setSpeed(0);
            self.applyAcceleration(0);
        }
    }

    @Override
    protected void handleEmergency(Vehicle self) {
        // tránh xe cứu thương
        if (shouldChangeLane(self, 1)) {
            self.changeLane(1);
        } else if (shouldChangeLane(self, -1)) {
            self.changeLane(-1);
        }
    }

    @Override
    public String getBehaviorName() {
        return "Aggressive";
    }
}