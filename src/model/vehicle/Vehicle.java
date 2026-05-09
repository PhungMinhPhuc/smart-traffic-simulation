package model.vehicle;

import generator.IdGenerator;
import model.node.Path;
import model.road.Lane;
import model.utility.TrafficPoint;
// import model.map.Vector2D;
import model.vehicle.behavior.DriverBehavior;
import java.util.Queue;
import java.util.LinkedList;

public abstract class Vehicle {
    protected String id;
    protected String type;
    protected TrafficPoint position;
    protected double speed;
    protected double acceleration;
    protected double maxSpeed;
    protected double length;
    protected double width;
    protected String sound;
    protected DriverBehavior behavior;
    
    // Environmental context
    protected Lane currentLane;
    protected Path currentPath;
    protected boolean isEmergency;
    
    // Path selection and navigation
    protected Queue<Path> plannedPath;  // Queue of paths the vehicle intends to follow

    public Vehicle(String type, double maxSpeed, double length, double width, String sound, DriverBehavior behavior) {
        this.id = IdGenerator.vehicleId(type);
        this.maxSpeed = maxSpeed;
        this.length = length;
        this.width = width;
        this.sound = sound;
        this.behavior = behavior;
        this.speed = 0;
        this.acceleration = 0;
        this.isEmergency = false;
        this.plannedPath = new LinkedList<>();
    }

    // Logic processing separated from Drawing (This method is called every frame to update the vehicle's state)
    public void update(double deltaTime) {
        // Sense the environment
        Vehicle ahead = getVehicleAhead();
        
        // Decide acceleration based on Behavior (Strategy Pattern)
        this.acceleration = behavior.decideAcceleration(this, ahead);
        applyPhysics(deltaTime);
        move(deltaTime); // Update Position
    }

    private void applyPhysics(double deltaTime) {
        speed += acceleration * deltaTime;
        // Clamp speed between 0 and maxSpeed
        if (speed < 0) speed = 0;
        if (speed > maxSpeed) speed = maxSpeed;
    }

    // Calculates the next position based on current segment (Lane or Path).
    private void move(double deltaTime) {
        TrafficPoint target = getTargetPoint();
        if (target != null) {
            this.position = this.position.moveTowards(target, speed * deltaTime);
        }
    }

    // Calculates the angle the vehicle is currently facing (GUI)
    public double getRotation() {
        TrafficPoint target = getTargetPoint();
        if (target != null) 
            return position.angleTo(target);
        else 
            return 0;
    }

    // Helper to identify what the vehicle is aiming for.
    public TrafficPoint getTargetPoint() {
        if (currentLane != null) return currentLane.getEndPoint();
        if (currentPath != null) return currentPath.getEndPoint();
        return null;
    }

    // Asks the current Lane or Path for the vehicle immediately in front.
    private Vehicle getVehicleAhead() {
        if (currentLane != null) return currentLane.getVehicleAhead(this);
        if (currentPath != null) return currentPath.getVehicleAhead(this);
        return null;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public TrafficPoint getPosition() {
        return position;
    }

    public void setPosition(TrafficPoint position) {
        this.position = position;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(double acceleration) {
        this.acceleration = acceleration;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public DriverBehavior getBehavior() {
        return behavior;
    }

    public void setBehavior(DriverBehavior behavior) {
        this.behavior = behavior;
    }

    public Lane getCurrentLane() {
        return currentLane;
    }

    public void setCurrentLane(Lane currentLane) {
        this.currentLane = currentLane;
    }

    public Path getCurrentPath() {
        return currentPath;
    }

    public void setCurrentPath(Path currentPath) {
        this.currentPath = currentPath;
    }

    public boolean isEmergency() {
        return isEmergency;
    }

    public void setEmergency(boolean isEmergency) {
        this.isEmergency = isEmergency;
    }
}