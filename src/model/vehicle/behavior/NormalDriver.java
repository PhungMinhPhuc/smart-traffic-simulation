package model.vehicle.behavior;

import model.vehicle.Vehicle;

public class NormalDriver extends DriverBehavior {
    public NormalDriver() {
        super(0.8, 80.0, -80.0); // double maxSpeedRatio, double speedUpAcceleration, double brakeAccelearation
    }

    @Override
    public String getBehaviorName() {
        return "Normal";
    }
}