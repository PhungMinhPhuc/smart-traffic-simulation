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
    protected Lane currentLane;
    protected Path currentPath;
    protected boolean isEmergency;

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
    }

    public void update(double deltaTime) {
    	// Lấy thông tin từ lane
        Vehicle ahead = getVehicleAhead();
        double distToLight = position.distanceTo(currentLane.getEndPoint());
        boolean isRed = currentLane.isRedLight();
        
        // Behavior quyết định hành động 
        behavior.decide(this, ahead, distToLight, isRed);
        
        // Cập nhật giá trị vật lý, tọa độ xe
        applyPhysics(deltaTime);
        move(deltaTime);
    }
    
    public void applyAcceleration(double a) {
    	this.acceleration = a;
    }
    
    public void changeLane(int offset) {
    	//offset = 1 -> phải, offset = -1 -> trái
    	Lane neighbor = currentLane.getNeighborLane(offset);
    	if (neighbor != null) {
    		currentLane.removeVehicle(this);
    		this.currentLane = neighbor;
    		this.currentLane.addVehicle(this);
    	}
    }
    
    private void applyPhysics(double deltaTime) {
        speed += acceleration * deltaTime;
        if (speed > maxSpeed) speed = maxSpeed;
        else if (speed < 0) speed = 0;
     
    }

    private void move(double deltaTime) {
        Point target = getTargetPoint();
        if (target != null) {
            this.position = this.position.moveTowards(target, speed * deltaTime);
        }
    }

    public Point getTargetPoint() {
        if (currentLane != null) return currentLane.getEndPoint();
        if (currentPath != null) return currentPath.getEndPoint();
        return null;
    }

    private Vehicle getVehicleAhead() {
        if (currentLane != null) return currentLane.getVehicleAhead(this);
        if (currentPath != null) return currentPath.getVehicleAhead(this);
        return null;
    }

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