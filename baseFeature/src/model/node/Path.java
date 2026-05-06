package model.node;

import model.map.*;
import model.vehicle.Vehicle;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import config.Constants;

public class Path {
    private String id;
    private Point startPoint;
    private Point endPoint;
    private double width;
    private List<Vehicle> vehicleList; // List of vehicles currently performing a turn on this path
    private Map<Path, Point> conflictPointMap;

    public Path(String id, Point start, Point end, double width) {
        this.id = id;
        this.startPoint = start;
        this.endPoint = end;
        this.width = width;
        this.vehicleList = new ArrayList<>();
        this.conflictPointMap = new HashMap<>();
    }

    // Finds the intersection point between this path and another using Cramer's Rule.
    public Point findConflictPoint(Path other) {
        double x1 = this.startPoint.getX(), y1 = this.startPoint.getY();
        double x2 = this.endPoint.getX(),   y2 = this.endPoint.getY();
        
        double x3 = other.startPoint.getX(), y3 = other.startPoint.getY();
        double x4 = other.endPoint.getX(),   y4 = other.endPoint.getY();

        // Determinant of the two lines
        double denom = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);

        // If denominator is 0, paths are parallel
        if (Math.abs(denom) < 1e-9) return null;

        // Calculate intersection point coordinates
        double intersectX = ((x1 * y2 - y1 * x2) * (x3 - x4) - (x1 - x2) * (x3 * y4 - y3 * x4)) / denom;
        double intersectY = ((x1 * y2 - y1 * x2) * (y3 - y4) - (y1 - y2) * (x3 * y4 - y3 * x4)) / denom;

        // Verify if the intersection point lies within BOTH line segments
        if (isPointOnSegment(intersectX, intersectY, x1, y1, x2, y2) &&
            isPointOnSegment(intersectX, intersectY, x3, y3, x4, y4)) {
            return new Point(intersectX, intersectY);
        }

        return null;
    }

    // Checks if a calculated point is within the bounds of a segment.
    private boolean isPointOnSegment(double px, double py, double x1, double y1, double x2, double y2) {
        return px >= Math.min(x1, x2) - Constants.EPS && px <= Math.max(x1, x2) + Constants.EPS &&
               py >= Math.min(y1, y2) - Constants.EPS && py <= Math.max(y1, y2) + Constants.EPS;
    }

    // Calculates the distance from the start of this path to the conflict point with another path (for Time to Collision)
    public double getDistanceToConflict(Path otherPath) {
        Point conflictPoint = conflictPointMap.get(otherPath);
        if (conflictPoint == null) return Double.MAX_VALUE;
        return startPoint.distanceTo(conflictPoint);
    }

    // Finds the vehicle ahead on the same path to prevent rear-end collisions inside the junction.
    public Vehicle getVehicleAhead(Vehicle current) {
        int index = vehicleList.indexOf(current);
        if (index > 0) {
            return vehicleList.get(index - 1);
        }
        return null;
    }

    // Management Methods

    public void addConflictPoint(Path other, Point intersect) {
        conflictPointMap.put(other, intersect);
    }

    public void removeConflictPoint(Path path){
        conflictPointMap.remove(path);
    }

    public void addVehicle(Vehicle v) {
        if (!vehicleList.contains(v)) vehicleList.add(v);
    }

    public void removeVehicle(Vehicle v) {
        vehicleList.remove(v);
    }

    
    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Point getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(Point startPoint) {
        this.startPoint = startPoint;
    }

    public Point getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(Point endPoint) {
        this.endPoint = endPoint;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public List<Vehicle> getVehicleList() {
        return vehicleList;
    }

    public void setVehicleList(List<Vehicle> vehicleList) {
        this.vehicleList = vehicleList;
    }

    public Map<Path, Point> getConflictPointMap() {
        return conflictPointMap;
    }

    public void setConflictPointMap(Map<Path, Point> conflictPointMap) {
        this.conflictPointMap = conflictPointMap;
    }
}