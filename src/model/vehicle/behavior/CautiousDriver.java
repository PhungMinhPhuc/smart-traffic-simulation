package model.vehicle.behavior;

import model.vehicle.Vehicle;
import config.Constants;

public class CautiousDriver extends DriverBehavior {
    public CautiousDriver() {
        super(Constants.CAUTIOUS_SPEED_RATIO, Constants.CAUTIOUS_ACCELERATION, Constants.CAUTIOUS_BRAKING);
    }

    @Override
    protected boolean checkIfLaneChangeIsNeeded(Vehicle self, double distanceToVehicleAhead, double speedOfVehicleAhead) {
        // Cautious drivers never change lanes to overtake
        return false;
    }

    @Override
    public String getBehaviorName() {
        return "Cautious";
    }
}