package model.map;

import java.util.Objects;
import config.Constants;

public class Point {
    private double x;
    private double y;

    public Point(double x, double y){
        this.x = x;
        this.y = y;
    }

    // Point + Vector = New Point
    public Point add(Vector2D v) {
        return new Point(this.x + v.dx, this.y + v.dy);
    }

    // Point - Point = Vector
    public Vector2D subtract(Point other) {
        return new Vector2D(this.x - other.x, this.y - other.y);
    }

    // Calculate the distance to another point (collision + distance keeping)
    public double distanceTo(Point other){
        return Math.sqrt(Math.pow(this.x - other.x, 2) + Math.pow(this.y - other.y, 2));
    } 

    // Calculate the angle to another point in Radians (rotate the vehicles img towards its target)
    public double angleTo(Point other){
        return Math.atan2(other.y - this.y, other.x - this.x);
    }

    // Returns a new Point that is moved from the current point toward a target point by a specific distance
    public Point moveTowards(Point target, double distance) {
        double currentDist = this.distanceTo(target);
        if (currentDist <= distance) {
            return new Point(target.x, target.y);
        }
            
        double ratio = distance / currentDist;
        double newX = this.x + (target.x - this.x) * ratio;
        double newY = this.y + (target.y - this.y) * ratio;
        return new Point(newX, newY);
    }

    // Returns a new Point offset perpendicularly to the direction toward a target (parallel lanes)
    public Point getOffsetPoint(Point target, double lanewidth) {
        // Direction vector (dx, dy)
        double dx = target.x - this.x;
        double dy = target.y - this.y;

        // Length (magnitude)
        double length = Math.sqrt(dx * dx + dy * dy);

        // Handle edge case (start and target are the same point)
        if (length < Constants.EPS) return new Point(this.x, this.y);

        // Normalize to get Unit Vector
        double uX = dx / length;
        double uY = dy / length;

        // The perpendicular unit vector is (-uY, uX) to shift by 'lanewidth'
        double offsetX = -uY * lanewidth;
        double offsetY = uX * lanewidth;

        return new Point(this.x + offsetX, this.y + offsetY);
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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
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
}