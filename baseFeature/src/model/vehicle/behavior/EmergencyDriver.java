package model.vehicle.behavior;

import model.vehicle.Vehicle;

public class EmergencyDriver extends DriverBehavior {

    public EmergencyDriver() {
        this.safeTimeGap = 0.5;
        this.minDistance = 2.0;
        this.accelerationValue = 3.0;
        this.brakingValue = -1.5;
        this.stopLineDistance = 10.0;
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
            // Chủ động lách sang làn trống bất kỳ để vượt lên tiếp
            if (shouldChangeLane(self, -1)) {
                self.changeLane(-1);
                return;
            } else if (shouldChangeLane(self, 1)) {
                self.changeLane(1);
                return;
            }
            self.applyAcceleration(brakingValue);
        }
        else if (self.getSpeed() > ahead.getSpeed()) {
            self.applyAcceleration(brakingValue);
        }
        else {
            self.applyAcceleration(accelerationValue);
        }
    }

    @Override
    protected void handleRedLight(Vehicle self, double distance) {
        // vượt đèn đỏ
        self.applyAcceleration(accelerationValue * 0.5);
    }

    @Override
    protected void handleEmergency(Vehicle self) {
        // không cần nhường
        self.applyAcceleration(accelerationValue);
    }

    @Override
    public String getBehaviorName() {
        return "Emergency";
    }
}