package model.vehicle;

import model.vehicle.behavior.DriverBehavior;
import model.utility.TrafficPoint;
import model.utility.TrafficVector;
import config.Constants;

public class Car extends Vehicle {

    public Car() {
        super("Car", Constants.CAR_MAX_SPEED, Constants.CAR_LENGTH, Constants.CAR_WIDTH, Constants.CAR_SOUND);
        this.isEmergency = false;
    }

    public Car(TrafficPoint position, TrafficVector direction, DriverBehavior behavior) {
        super("Car", Constants.CAR_MAX_SPEED, Constants.CAR_LENGTH, Constants.CAR_WIDTH, Constants.CAR_SOUND);
        this.position = position;
        this.direction = direction.clone();
        this.speed = 1000.0;
        this.isEmergency = false;
        this.behavior = behavior;
    }

    @Override
    public String toString() {
        return "Car [" + behavior.getBehaviorName() + "]";
    }
}