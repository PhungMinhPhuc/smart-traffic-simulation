package items.utility;

public class Point2D {

	private double x;
	private double y;
	
	public Point2D(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public Point2D clone() {
		return new Point2D(this.x, this.y);
	}
	
	public double distance(Point2D other) {
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
