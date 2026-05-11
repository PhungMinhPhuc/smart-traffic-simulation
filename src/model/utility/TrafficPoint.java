package model.utility;

import java.util.Objects;
import config.Constants;

public class TrafficPoint {
    private double x;
    private double y;

    public TrafficPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    // Point + Vector = New Point
    public TrafficPoint add(TrafficVector v) {
        return new TrafficPoint(this.x + v.x, this.y + v.y);
    }

    // Point - Point = Vector
    public TrafficVector subtract(TrafficPoint other) {
        return new TrafficVector(this.x - other.x, this.y - other.y);
    }

    // Calculate the distance to another point (collision + distance keeping)
    public double distanceTo(TrafficPoint other) {
        return Math.sqrt(Math.pow(this.x - other.x, 2) + Math.pow(this.y - other.y, 2));
    }

    // Calculate the angle to another point in Radians (rotate the vehicles img
    // towards its target)
    public double angleTo(TrafficPoint other) {
        return Math.atan2(other.y - this.y, other.x - this.x);
    }

    // Returns a new Point that is moved from the current point toward a target
    // point by a specific distance
    public TrafficPoint translatePoint(TrafficPoint target, double distance) {
        double currentDist = this.distanceTo(target);
        if (currentDist <= distance) {
            return new TrafficPoint(target.x, target.y);
        }

        double ratio = distance / currentDist;
        double newX = this.x + (target.x - this.x) * ratio;
        double newY = this.y + (target.y - this.y) * ratio;
        return new TrafficPoint(newX, newY);
    }

    // Returns a new Point offset perpendicularly to the direction toward a target
    // (parallel lanes)
    public TrafficPoint getOffsetPoint(TrafficPoint target, double lanewidth) {
        // Direction vector (dx, dy)
        double dx = target.x - this.x;
        double dy = target.y - this.y;

        // Length (magnitude)
        double length = Math.sqrt(dx * dx + dy * dy);

        // Handle edge case (start and target are the same point)
        if (length < Constants.EPS)
            return new TrafficPoint(this.x, this.y);

        // Normalize to get Unit Vector
        double uX = dx / length;
        double uY = dy / length;

        // The perpendicular unit vector is (-uY, uX) to shift by 'lanewidth'
        double offsetX = -uY * lanewidth;
        double offsetY = uX * lanewidth;

        return new TrafficPoint(this.x + offsetX, this.y + offsetY);
    }

    // Getters and Setters
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        TrafficPoint point = (TrafficPoint) o;
        // Use a small epsilon to handle double precision errors
        return Math.abs(point.x - x) < Constants.EPS && Math.abs(point.y - y) < Constants.EPS;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return String.format("(%.2f, %.2f)", x, y);
    }

    @Override
    public TrafficPoint clone() {
        return new TrafficPoint(this.x, this.y);
    }
}