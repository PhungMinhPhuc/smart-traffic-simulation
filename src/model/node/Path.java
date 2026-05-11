package model.node;

import model.utility.TrafficPoint;
import model.utility.TrafficGeometry;
import model.vehicle.Vehicle;

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

        return TrafficGeometry.intersectsLines(thisStart, thisEnd, otherStart, otherEnd);
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
