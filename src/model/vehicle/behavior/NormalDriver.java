package model.vehicle.behavior;

import config.Constants;

public class NormalDriver extends DriverBehavior {
    public NormalDriver() {
        super(Constants.NORMAL_SPEED_RATIO, Constants.NORMAL_ACCELERATION, Constants.NORMAL_BRAKING);
    }

    @Override
    public String getBehaviorName() {
        return "Normal";
    }
}