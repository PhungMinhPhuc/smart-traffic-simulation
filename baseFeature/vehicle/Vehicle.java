package baseFeature.vehicle;

import java.awt.Point;
import baseFeature.behavior.DriverBehavior;

public abstract class Vehicle {

    private Point coordinate;
    private double speed;
    private double acceleration;
    private double maxSpeed;
    private String sound;
    private double width;
    private double length;

    protected DriverBehavior driverBehavior;

    public Vehicle(Point coordinate, double width, double length, 
                   double maxSpeed, String sound, 
                   DriverBehavior driverBehavior) {
        
        this.coordinate = coordinate;
        this.width = width;
        this.length = length;
        this.maxSpeed = maxSpeed;
        this.sound = sound;
        this.driverBehavior = driverBehavior;
        this.speed = 0;
        this.acceleration = 0;
    }

    public Point getCoordinate() {
        return coordinate;
    }

    public double getSpeed() {
        return speed;
    }

    public double getAcceleration() {
        return acceleration;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public String getSound() {
        return sound;
    }

    public double getWidth() {
        return width;
    }

    public double getLength() {
        return length;
    }

    public DriverBehavior getDriverBehavior() {
        return driverBehavior;
    }
    

    public void setCoordinate(Point coordinate) {
        this.coordinate = coordinate;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setAcceleration(double acceleration) {
        this.acceleration = acceleration;
    }

    public void setMaxSpeed(double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public void setDriverBehavior(DriverBehavior driverBehavior) {
        this.driverBehavior = driverBehavior;
    }


    public abstract void makeSound();

    public void applyBehavior() {
        if (driverBehavior != null) {
            driverBehavior.changeLane(this);
            driverBehavior.changeSpeed(this);
        }
    }
}