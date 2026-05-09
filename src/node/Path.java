package node;

import utility.TrafficPoint;
import vehicle.Vehicle;
import config.Constants;

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

    public Path(String id, TrafficPoint startPoint, TrafficPoint endPoint) {
        this.id = id;
        this.startPoint = startPoint.clone();
        this.endPoint = endPoint.clone();
        this.vehicleList = new ArrayList<>();
        this.conflictPointList= new HashMap<>();
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

        if (Math.abs(determinant) < Constants.EPS) {
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
        double minX = Math.min(startX, endX) - Constants.EPS;
        double maxX = Math.max(startX, endX) + Constants.EPS;
        double minY = Math.min(startY, endY) - Constants.EPS;
        double maxY = Math.max(startY, endY) + Constants.EPS;

        return pointX >= minX && pointX <= maxX && pointY >= minY && pointY <= maxY;
    }

    public void removeConflictPoint(Path path) {
        conflictPointList.remove(path);
    }

    public void clearConflictPoints() {
        conflictPointList.clear();
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Path path = (Path) o;
        return Objects.equals(id, path.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public void addVehicle(Vehicle vehicle) {
        vehicle.setDirection(startPoint, endPoint);
        vehicle.setPosition(startPoint);
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

    public String getId() {
        return id;
    }

    public void setStartPoint(TrafficPoint startPoint) {
        this.startPoint = startPoint.clone();
    }

    public void setEndPoint(TrafficPoint endPoint) {
        this.endPoint = endPoint.clone();
    }

    public List<Vehicle> getVehicleList() {
        return vehicleList;
    }
}
