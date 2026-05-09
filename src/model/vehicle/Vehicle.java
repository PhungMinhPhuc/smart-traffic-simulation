package model.vehicle;

import model.utility.TrafficPoint;
import model.utility.TrafficVector;
import generator.IdGenerator;


//psudeo Vehicle class for testing, not used in the final version
public class Vehicle {

	private String id;
	private TrafficPoint position;
	private double speed;
	private double acceleration;
	private TrafficVector direction; 

	public Vehicle(TrafficPoint position,TrafficVector direction) {
		this.id = IdGenerator.vehicleId("Demo Vehicle");
		this.position = position;
		this.speed = 0.0;
		this.direction = direction.clone();
		this.acceleration = 4.0;
	}		
	
	public Vehicle() {
		this.id = IdGenerator.vehicleId("Demo Vehicle");
		this.position = new TrafficPoint(0.0, 0.0);
		this.speed = 0.0;
		this.acceleration = 2.0;
	}
	
	//"Move" the vehicle by updating its position and speed
	//based on current speed, acceleration, and direction
	// in a timeInterval passed as parameter
	public void move(double timeInterval) {
		this.position = this.direction.translatePoint(position, timeInterval*this.speed);
		this.speed += this.acceleration * timeInterval;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		Vehicle other = (Vehicle) obj;
		return id.equals(other.id);
	}

	public void setDirection(TrafficVector direction) {
		this.direction = direction.clone();
	}
	
	public TrafficVector getDirection() {
		return direction;
	}
	
	public String getId() {
		return id;
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
}
