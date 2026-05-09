package model.vehicle;

import generator.IdGenerator;
import model.map.Point;
import model.map.Lane;
import model.node.Path;
// import model.map.Vector2D;
import model.vehicle.behavior.DriverBehavior;

public abstract class Vehicle {
    protected String id;
    protected String type;
    protected Point position;
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
    protected boolean isEmergency = false;

    public Vehicle(String type, double maxSpeed, double length, double width, String sound, Point position) {
        this.id = IdGenerator.vehicleId(type);
        this.maxSpeed = maxSpeed;
        this.length = length;
        this.width = width;
        this.sound = sound;
        this.type = type;
        this.position = position;
        this.speed = 0;
        this.acceleration = 0;
    }

    // Logic processing separated from Drawing (This method is called every frame to update the vehicle's state)
    public void update(double deltaTime) {
        // Sense the environment
        Vehicle ahead = getVehicleAhead();
        double distToLight = position.distanceTo(currentLane.getEndPoint());
        boolean isRed = currentLane.isRedLight();

        this.acceleration = 0;
        
        // Decide acceleration based on Behavior (Strategy Pattern)
        behavior.decide(this, ahead, distToLight, isRed);

        applyPhysics(deltaTime);
        move(deltaTime); // Update Position
    }

    public void applyAcceleration(double a){
        this.acceleration = a;
    }

    public void changeLane(int offset){
        //offset = -1 -> left , offset = 1 -> right
        Lane neighbor = currentLane.getNeighborLane(offset);

        if(neighbor != null){
            currentLane.removeVehicle(this);
            this.currentLane = neighbor;
            currentLane.addVehicle(this);
        }
    }

    private void applyPhysics(double deltaTime) {
        speed += acceleration * deltaTime;
        // Clamp speed between 0 and maxSpeed
        if (speed < 0) speed = 0;
        if (speed > maxSpeed) speed = maxSpeed;
    }

    // Calculates the next position based on current segment (Lane or Path).
    private void move(double deltaTime) {
        Point target = getTargetPoint();
        if (target != null) {
            this.position = this.position.moveTowards(target, speed * deltaTime);
        }
    }

    // Calculates the angle the vehicle is currently facing (GUI)
    public double getRotation() {
        Point target = getTargetPoint();
        if (target != null) 
            return position.angleTo(target);
        else 
            return 0;
    }

    // Helper to identify what the vehicle is aiming for.
    public Point getTargetPoint() {
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

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
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

    public String getType() {
        return type;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public double getLength() {
        return length;
    }

    public double getWidth() {
        return width;
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

    public abstract void makeSound();
}