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
    protected int pendingLaneChange = 0;

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

    public void update(double distToVehicleAhead, double speedVehicleAhead, double distToLight, 
    		boolean isRed, boolean canChangeToRight, boolean canChangeToLeft, boolean onEmergency, double deltaTime) {
        behavior.decide(this, distToVehicleAhead, speedVehicleAhead, distToLight, isRed, canChangeToRight, canChangeToLeft, onEmergency);
        
        applyPhysics(deltaTime);
    }
    
    public void applyAcceleration(double a) {
    	this.acceleration = a;
    }
    
    public void clearPendingLaneChange() {
        this.pendingLaneChange = 0;
    }
    
    private void applyPhysics(double deltaTime) {
    	//calculate speed 
        speed += acceleration * deltaTime;
        if (speed > maxSpeed * behavior.getSpeedRatio()) speed = maxSpeed * behavior.getSpeedRatio();
        else if (speed < 0) speed = 0;
        //move
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

	public int getPendingLaneChange() {
		return pendingLaneChange;
	}

	public void setPendingLaneChange(int pendingLaneChange) {
		this.pendingLaneChange = pendingLaneChange;
	}
}