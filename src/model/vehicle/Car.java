package model.vehicle;

import model.vehicle.behavior.DriverBehavior;
import model.utility.TrafficPoint;
import model.utility.TrafficVector;
import config.Constants;

public class Car extends Vehicle {
    public Car(TrafficPoint position, TrafficVector direction, DriverBehavior behavior) {
        super("Car", Constants.CAR_MAX_SPEED, Constants.CAR_LENGTH, Constants.CAR_WIDTH, Constants.CAR_SOUND,
                position, direction, behavior);
    }

    public Car(TrafficPoint position, TrafficVector direction) {
        this(position, direction, new model.vehicle.behavior.NormalDriver());
    }

    public String toString() {
        return "Car [" + behavior.getBehaviorName() + "]";
    }
}