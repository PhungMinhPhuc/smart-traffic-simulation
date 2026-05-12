package model.vehicle;

import generator.IdGenerator;
import model.node.Path;
import model.road.Lane;
import model.utility.TrafficPoint;
import model.utility.TrafficVector;
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
    protected TrafficVector direction;

    // Environmental context
    protected Lane currentLane;
    protected boolean isEmergency;

    // Lane change state management
    protected Lane targetLane; // Lane vehicle is changing to
    protected boolean isChangingLane; // Whether vehicle is in the process of changing lanes
    protected double laneChangeProgress; // 0 to 1: progress of lane change
    protected TrafficPoint laneChangeStartPosition; // Starting position when lane change began
    protected double laneChangeDuration; // Time (in seconds) for the lane change to complete
    protected double laneChangeElapsed; // Time elapsed since lane change started
    protected static final double LANE_CHANGE_DURATION = 1.5; // Default: 1.5 seconds to change lanes

    // Path selection and navigation
    protected Queue<Path> plannedPath; // Queue of paths the vehicle intends to follow

    public Vehicle(String type, double maxSpeed, double length, double width, String sound) {
        this.id = IdGenerator.vehicleId(type);
        this.maxSpeed = maxSpeed;
        this.length = length;
        this.width = width;
        this.sound = sound;
        this.speed = 0;
        this.acceleration = 0;
        this.isEmergency = false;
        this.plannedPath = new LinkedList<>();

        // Initialize lane change state
        this.targetLane = null;
        this.isChangingLane = false;
        this.laneChangeProgress = 0;
        this.laneChangeStartPosition = null;
        this.laneChangeDuration = LANE_CHANGE_DURATION;
        this.laneChangeElapsed = 0;
    }

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

        // Initialize lane change state
        this.targetLane = null;
        this.isChangingLane = false;
        this.laneChangeProgress = 0;
        this.laneChangeStartPosition = null;
        this.laneChangeDuration = LANE_CHANGE_DURATION;
        this.laneChangeElapsed = 0;
    }

    // Logic processing separated from Drawing (This method is called every frame to
    // update the vehicle's state)
    public void update(double deltaTime) {
        // Update lane change progress if in the middle of changing lanes
        if (isChangingLane) {
            updateLaneChange(deltaTime);
        }

        // Sense the environment
        Vehicle ahead = getVehicleAhead();

        // Decide acceleration based on Behavior (Strategy Pattern)
        if (behavior != null) {
            this.acceleration = behavior.decideAcceleration(this, ahead);
        }
        applyPhysics(deltaTime);
        move(deltaTime); // Update Position
    }

    private void applyPhysics(double deltaTime) {
        speed += acceleration * deltaTime;
        // Clamp speed between 0 and maxSpeed
        if (speed < 0)
            speed = 0;
        if (speed > maxSpeed)
            speed = maxSpeed;
    }

    // Calculates the next position based on current segment (Lane or Path).
    public void move(double deltaTime) {
        TrafficPoint target = getTargetPoint();
        if (target != null) {
            this.position = this.position.translatePoint(target, speed * deltaTime);
        } else if (direction != null) {
            // No lane context (e.g., on a junction path) — move along direction vector
            this.position = direction.translatePoint(this.position, speed * deltaTime);
        }
    }

    // Initiates a lane change to the target lane
    public void initiateLaneChange(Lane newLane) {
        if (newLane == null || newLane == currentLane)
            return;

        this.targetLane = newLane;
        this.isChangingLane = true;
        this.laneChangeProgress = 0;
        this.laneChangeElapsed = 0;
        this.laneChangeStartPosition = this.position.clone();
    }

    // Updates the lane change progress
    private void updateLaneChange(double deltaTime) {
        laneChangeElapsed += deltaTime;
        laneChangeProgress = Math.min(1.0, laneChangeElapsed / laneChangeDuration);

        // When lane change is complete
        if (laneChangeProgress >= 1.0) {
            completeLaneChange();
        }
    }

    // Completes the lane change - switches the current lane
    private void completeLaneChange() {
        if (currentLane != null) {
            currentLane.removeVehicle(this);
        }
        currentLane = targetLane;
        targetLane = null;
        isChangingLane = false;
        laneChangeProgress = 0;
        laneChangeElapsed = 0;
        laneChangeStartPosition = null;

        if (currentLane != null) {
            currentLane.addVehicle(this);
        }
    }

    // Calculates the angle the vehicle is currently facing (GUI)
    public double getRotation() {
        TrafficPoint target = getTargetPoint();
        if (target != null)
            return position.angleTo(target);
        if (direction != null)
            return direction.getAngle();
        return 0;
    }

    // Helper to identify what the vehicle is aiming for.
    public TrafficPoint getTargetPoint() {
        // If vehicle is changing lanes, return interpolated point between current and
        // target lane
        if (isChangingLane && targetLane != null && currentLane != null) {
            TrafficPoint currentLaneEnd = currentLane.getEndPoint();
            TrafficPoint targetLaneEnd = targetLane.getEndPoint();

            // Linear interpolation between the two lane end points
            double x = currentLaneEnd.getX() + (targetLaneEnd.getX() - currentLaneEnd.getX()) * laneChangeProgress;
            double y = currentLaneEnd.getY() + (targetLaneEnd.getY() - currentLaneEnd.getY()) * laneChangeProgress;

            return new TrafficPoint(x, y);
        }

        // Normal case: not changing lanes
        if (currentLane != null)
            return currentLane.getEndPoint();
        return null;
    }

    // Asks the current Lane or Path for the vehicle immediately in front.
    private Vehicle getVehicleAhead() {
        if (currentLane != null)
            return currentLane.getVehicleAhead(this);
        return null;
    }

    // Attempts to change lanes if the driver decides to overtake
    // Returns true if lane change was initiated, false otherwise
    public boolean tryChangeLane(Lane newLane) {
        // Can only change lanes if not already changing and if the new lane is valid
        if (isChangingLane || newLane == null || newLane == currentLane) {
            return false;
        }

        // Check if new lane is reasonably close (only adjacent lanes allowed)
        if (currentLane == null)
            return false;

        initiateLaneChange(newLane);
        return true;
    }

    // Gets the adjacent lane (useful for overtaking maneuvers)
    // direction: -1 for left lane, 1 for right lane (relative to vehicle direction)
    public Lane getAdjacentLane(int direction) {
        if (currentLane == null)
            return null;
        // This would require access to the Way object which contains all lanes
        // For now, return null - this can be implemented when the vehicle has access to
        // the road/way
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

    public TrafficVector getDirection() {
        return direction;
    }

    public void setDirection(TrafficVector direction) {
        this.direction = direction;
    }

    public void setDirection(TrafficPoint startPoint, TrafficPoint endPoint) {
        this.direction = new TrafficVector(startPoint, endPoint);
    }

    public TrafficPoint getLaneChangeStartPosition() {
        return laneChangeStartPosition;
    }

    public void setLaneChangeStartPosition(TrafficPoint laneChangeStartPosition) {
        this.laneChangeStartPosition = laneChangeStartPosition;
    }

    public void setLaneChangeDuration(double laneChangeDuration) {
        this.laneChangeDuration = laneChangeDuration;
    }

    public double getLaneChangeElapsed() {
        return laneChangeElapsed;
    }

    public void setLaneChangeElapsed(double laneChangeElapsed) {
        this.laneChangeElapsed = laneChangeElapsed;
    }

    public Queue<Path> getPlannedPath() {
        return plannedPath;
    }

    public void setPlannedPath(Queue<Path> plannedPath) {
        this.plannedPath = plannedPath;
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

    public boolean isEmergency() {
        return isEmergency;
    }

    public void setEmergency(boolean isEmergency) {
        this.isEmergency = isEmergency;
    }

    // Lane change getters and setters
    public Lane getTargetLane() {
        return targetLane;
    }

    public void setTargetLane(Lane targetLane) {
        this.targetLane = targetLane;
    }

    public boolean isChangingLane() {
        return isChangingLane;
    }

    public void setChangingLane(boolean isChangingLane) {
        this.isChangingLane = isChangingLane;
    }

    public double getLaneChangeProgress() {
        return laneChangeProgress;
    }

    public void setLaneChangeProgress(double progress) {
        this.laneChangeProgress = Math.max(0, Math.min(1, progress));
    }
}