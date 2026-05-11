package model.utility;

import config.Constants;

public class TrafficVector {
    public double x, y;

    public TrafficVector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public TrafficVector(TrafficPoint from, TrafficPoint to) {
        this.x = to.getX() - from.getX();
        this.y = to.getY() - from.getY();
    }

    public double length() {
        return Math.sqrt(x * x + y * y);
    }

    public TrafficVector normalize() {
        double len = length();
        if (len < Constants.EPS)
            return new TrafficVector(0, 0);
        return new TrafficVector(x / len, y / len);
    }

    public TrafficVector multiply(double scalar) {
        return new TrafficVector(x * scalar, y * scalar);
    }

    public TrafficVector getPerpendicular() {
        return new TrafficVector(-y, x);
    }

    public TrafficVector rotateVector(double radianAngle) {
        double cosAngle = Math.cos(radianAngle);
        double sinAngle = Math.sin(radianAngle);
        double newX = this.x * cosAngle - this.y * sinAngle;
        double newY = this.x * sinAngle + this.y * cosAngle;
        return new TrafficVector(newX, newY);
    }

    public TrafficVector scale(double newLength) {
        double currentLength = length();
        if (currentLength < Constants.EPS) {
            return new TrafficVector(0, 0);
        }
        double scale = newLength / currentLength;
        return new TrafficVector(this.x * scale, this.y * scale);
    }

    public TrafficPoint translatePoint(TrafficPoint point, double distance) {
        TrafficVector direction = new TrafficVector(this.x, this.y).normalize();
        TrafficVector resizedDirection = direction.scale(distance);
        return new TrafficPoint(point.getX() + resizedDirection.x, point.getY() + resizedDirection.y);
    }

    @Override
    public TrafficVector clone() {
        return new TrafficVector(this.x, this.y);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getAngle() {
        return Math.atan2(this.y, this.x);
    }
}
