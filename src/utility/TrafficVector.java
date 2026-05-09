package utility;

public class TrafficVector {
    private double x;
    private double y;

    public TrafficVector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public TrafficVector (TrafficPoint start, TrafficPoint end) {
        this(end.getX() - start.getX(), end.getY() - start.getY());
    }

    @Override
    public TrafficVector clone() {
        return new TrafficVector(this.x, this.y);
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

    public TrafficVector rotateVector(double radianAngle) {
        double cosAngle = Math.cos(radianAngle);
        double sinAngle = Math.sin(radianAngle);
        double newX = this.x * cosAngle - this.y * sinAngle;
        double newY = this.x * sinAngle + this.y * cosAngle;
        return new TrafficVector(newX, newY);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
