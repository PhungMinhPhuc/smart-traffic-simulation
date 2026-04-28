package road;

import point.Point;
import point.Vector2D;
import vehicle.Vehicle;

import java.util.ArrayList;
import java.util.List;

public class Lane {
    private String laneId;
    private int index;
    private List<Vehicle> vehicleList;
    private Point startPoint;
    private Point endPoint;


    public Lane(String id, int index, Point startPoint, Point endPoint) {
        this.laneId = id;
        this.index = index;
        this.vehicleList = new ArrayList<>();
        this.startPoint = startPoint;
        this.endPoint = endPoint;
    }

    public void addVehicle(Vehicle vehicle) {
        vehicleList.add(vehicle);
    }

    public void removeVehicle(Vehicle vehicle) {
        vehicleList.remove(vehicle);
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

    public int getIndex() {
        return index;
    }
}
