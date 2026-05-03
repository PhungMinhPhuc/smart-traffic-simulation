package model.utility;

public class TrafficPoint {

	private double x;
	private double y;
	
	public TrafficPoint(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public TrafficPoint clone() {
		return new TrafficPoint(this.x, this.y);
	}
	
	public double distance(TrafficPoint other) {
		double dx = this.x - other.x;
		double dy = this.y - other.y;
		return Math.sqrt(dx * dx + dy * dy);
	}
	
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}

}
