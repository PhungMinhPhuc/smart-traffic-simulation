package model.vehicle.behavior;

import model.vehicle.Vehicle;

public class NormalDriver extends DriverBehavior {

    public NormalDriver() {
        this.safeTimeGap = 2.0;
        this.minDistance = 5.0;
        this.accelerationValue = 1.5;
        this.brakingValue = -2.0;
        this.stopLineDistance = 40.0;
    }

    @Override
    protected void handleFreeWay(Vehicle self) {
        if (self.getSpeed() < self.getMaxSpeed()) {
            self.applyAcceleration(accelerationValue);
        } else {
            self.applyAcceleration(0);
        }
    }

    @Override
    protected void handleFollowVehicle(Vehicle self, Vehicle ahead) {
        double dist = distance(self, ahead);
        double safe = safeDistance(self);

        if (dist < safe) {
            // Xe trước chạy quá chậm thì vượt
            if (ahead.getSpeed() < self.getSpeed() * 0.7) {
                // thử vượt bên trái
                if (shouldChangeLane(self, -1)) {
                    self.changeLane(-1);
                    return;
                }
                // thử vượt bên phải
                else if (shouldChangeLane(self, 1)) {
                    self.changeLane(1);
                    return;
                }
            }
            // Không chuyển làn được thì phanh lại
            self.applyAcceleration(brakingValue);
        } 
        else {
            self.applyAcceleration(accelerationValue);
        }
    }

    @Override
    protected void handleRedLight(Vehicle self, double distance) {
        self.applyAcceleration(brakingToStop(self, distance));
    }

    @Override
    protected void handleEmergency(Vehicle self) {
        // Có xe ưu tiên thì nhường đường
        if (shouldChangeLane(self, 1)) {
            self.changeLane(1); // dạt sang phải
        } else if (shouldChangeLane(self, -1)) {
            self.changeLane(-1); // không phải thì sang trái
        } else {
            self.applyAcceleration(brakingValue); // Không chuyển làn được thì phanh lại
        }
    }

    @Override
    public String getBehaviorName() {
        return "Normal";
    }
}