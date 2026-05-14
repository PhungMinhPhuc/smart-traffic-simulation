package model.vehicle;

import generator.IdGenerator;
import model.utility.TrafficPoint;
import model.utility.TrafficVector;
import model.vehicle.behavior.DriverBehavior;

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
	protected boolean isEmergency;
	protected TrafficVector direction;
	protected int laneChangeDirection = 0; // -1: move left, 0: stay, 1: move right

	public Vehicle(String type, double maxSpeed, double length, double width, String sound,
			TrafficPoint position, TrafficVector direction, DriverBehavior behavior) {
		this.id = IdGenerator.vehicleId(type);
		this.type = type;
		this.maxSpeed = maxSpeed;
		this.length = length;
		this.width = width;
		this.sound = sound;
		this.position = position;
		this.direction = direction;
		this.behavior = behavior;
	}

	// This method updates the vehicle by first asking the driver to 'decide' a plan 
    // and then moving the vehicle physically using 'applyPhysics'
	public void update(double distanceToVehicleAhead, 
                       double speedOfVehicleAhead, 
                       double distanceToLight,
			           boolean isRed, 
                       boolean canRight, 
                       boolean canLeft, 
                       double distLeft,
                       double distRight,
                       boolean onEmergency, 
                       boolean isChangingLane,
                       double deltaTime) {
        
		behavior.decide(this, 
                        distanceToVehicleAhead, 
                        speedOfVehicleAhead, 
                        distanceToLight, 
                        isRed, 
                        canRight,
				        canLeft, 
                        distLeft,
                        distRight,
                        onEmergency,
                        isChangingLane);

		applyPhysics(deltaTime);
	}

	public void applyAcceleration(double acceleration) {
		this.acceleration = acceleration;
	}

	public void resetLaneChangeDirection() {
		this.laneChangeDirection = 0;
	}

	private void applyPhysics(double deltaTime) {
		speed += acceleration * deltaTime;
        
		double currentMaxSpeed = maxSpeed * behavior.getSpeedRatio();
		if (speed > currentMaxSpeed) {
			speed = currentMaxSpeed;
		} else if (speed < 0) {
			speed = 0;
		}
        
		position = direction.translatePoint(position, deltaTime * speed);
	}

	public String getId() {
		return id;
	}

	public String getType() {
		return type;
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

	public double getLength() {
		return length;
	}

	public double getWidth() {
		return width;
	}

	public String getSound() {
		return sound;
	}

	public DriverBehavior getBehavior() {
		return behavior;
	}

	public void setBehavior(DriverBehavior behavior) {
		this.behavior = behavior;
	}

	public boolean isEmergency() {
		return isEmergency;
	}

	public void setEmergency(boolean isEmergency) {
		this.isEmergency = isEmergency;
	}

	public TrafficVector getDirection() {
		return direction;
	}

	public void setDirection(TrafficVector direction) {
		this.direction = direction;
	}

	public int getLaneChangeDirection() {
		return laneChangeDirection;
	}

	public void setLaneChangeDirection(int laneChangeDirection) {
		this.laneChangeDirection = laneChangeDirection;
	}
}