package node;

import point.TrafficPoint;
import vehicle.Vehicle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.HashMap;


public class Path {
    private String id;
    private TrafficPoint startPoint;
    private TrafficPoint endPoint;
    private List<Vehicle> vehicleList;
    private Map<Path, TrafficPoint> conflictPointList;
    private final double width = 10;

    public Path(String id, TrafficPoint startPoint, TrafficPoint endPoint) {
        this.id = id;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.vehicleList = new ArrayList<>();
        this.conflictPointList= new HashMap<>();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Path path = (Path) o;
        return Objects.equals(id, path.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public void addConflictPoint(Path conflictPath, TrafficPoint conflictPoint) {
        conflictPointList.put(conflictPath, conflictPoint);
    }

    public TrafficPoint findConflictPoint(Path otherPath) {
        // Calculate conflict point using Cramer
        TrafficPoint thisStart = this.getStartPoint();
        TrafficPoint thisEnd = this.getEndPoint();
        TrafficPoint otherStart = otherPath.getStartPoint();
        TrafficPoint otherEnd = otherPath.getEndPoint();

        double thisStartX = thisStart.getX();
        double thisStartY = thisStart.getY();
        double thisEndX = thisEnd.getX();
        double thisEndY = thisEnd.getY();

        double otherStartX = otherStart.getX();
        double otherStartY = otherStart.getY();
        double otherEndX = otherEnd.getX();
        double otherEndY = otherEnd.getY();

        // Main determinant
        double determinant = (thisStartX - thisEndX) * (otherStartY - otherEndY) - (thisStartY - thisEndY) * (otherStartX - otherEndX);

        if (Math.abs(determinant) < 1e-9) {
            return null;
        }

        // Determinant of each Path
        double thisDeterminant = thisStartX * thisEndY - thisStartY * thisEndX;
        double otherDeterminant = otherStartX * otherEndY - otherStartY * otherEndX;

        double conflictPointX =
                (thisDeterminant * (otherStartX - otherEndX) - (thisStartX - thisEndX) * otherDeterminant) / determinant;

        double conflictPointY =
                (thisDeterminant * (otherStartY - otherEndY) - (thisStartY - thisEndY) * otherDeterminant) / determinant;

        boolean isOnThisPath = isPointOnSegment(conflictPointX, conflictPointY, thisStartX, thisStartY, thisEndX, thisEndY);

        boolean isOnOtherPath = isPointOnSegment(conflictPointX, conflictPointY, otherStartX, otherStartY, otherEndX, otherEndY);

        if (isOnThisPath && isOnOtherPath) {
            return new TrafficPoint(conflictPointX, conflictPointY);
        }

        return null;
    }

    private boolean isPointOnSegment(double pointX, double pointY, double startX, double startY, double endX, double endY) {
        double epsilon = 1e-9;

        double minX = Math.min(startX, endX) - epsilon;
        double maxX = Math.max(startX, endX) + epsilon;
        double minY = Math.min(startY, endY) - epsilon;
        double maxY = Math.max(startY, endY) + epsilon;

        return pointX >= minX && pointX <= maxX && pointY >= minY && pointY <= maxY;
    }

    public void removeConflictPoint(Path path) {
        conflictPointList.remove(path);
    }

    public void addVehicle(Vehicle vehicle) {
        vehicleList.add(vehicle);
    }

    public void removeVehicle(Vehicle vehicle) {
        vehicleList.remove(vehicle);
    }

    public Map<Path, TrafficPoint> getConflictPointList() {
        return conflictPointList;
    }

    public TrafficPoint getStartPoint() {
        return startPoint;
    }

    public TrafficPoint getEndPoint() {
        return endPoint;
    }

    public void setStartPoint(TrafficPoint startPoint) {
        this.startPoint = startPoint;
    }

    public void setEndPoint(TrafficPoint endPoint) {
        this.endPoint = endPoint;
    }

    public double getWidth() {
        return width;
    }
}
