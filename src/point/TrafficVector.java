package point;

public class TrafficVector {
    private  double x;
    private double y;

    public TrafficVector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    // Create vector from 2 points
    public static TrafficVector fromPoints(TrafficPoint start, TrafficPoint end) {
        return new TrafficVector(end.getX() - start.getX(), end.getY() - start.getY());
    }

    public double length() {
        return Math.sqrt(x * x + y * y);
    }

    public TrafficVector normalize() {
        if (length() == 0) {
            throw new IllegalStateException("Zero length vector");
        }

        return new TrafficVector(x / length(), y / length());
    }

    public TrafficVector scale(double scale) {
        return new TrafficVector(x * scale, y * scale);
    }

    // Rotate 90 degrees to the right
    public TrafficVector rotateRight90() {
        return new TrafficVector(y, -x);
    }

    // Rotate 180 degrees
    public TrafficVector rotate180() {
        return new TrafficVector(-x, -y);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
