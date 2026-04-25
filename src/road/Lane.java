package road;

import point.Point;
import vehicle.Vehicle;

import java.util.ArrayList;
import java.util.List;

public class Lane {
    private String id;
    private List<Vehicle> vehicleList;
    private Point startPoint;
    private Point endPoint;

    public Lane(String id, Point startPoint, Point endPoint) {
        this.id = id;
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

    public String getId() {
        return id;
    }
}
