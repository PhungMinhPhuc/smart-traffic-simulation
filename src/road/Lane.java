package road;

import point.TrafficPoint;
import vehicle.Vehicle;

import java.util.ArrayList;
import java.util.List;

public class Lane {
    private String laneId;
    private int index;
    private List<Vehicle> vehicleList;
    private TrafficPoint startPoint;
    private TrafficPoint endPoint;


    public Lane(String id, int index, TrafficPoint startPoint, TrafficPoint endPoint) {
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

    public TrafficPoint getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(TrafficPoint startPoint) {
        this.startPoint = startPoint;
    }

    public TrafficPoint getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(TrafficPoint endPoint) {
        this.endPoint = endPoint;
    }

    public int getIndex() {
        return index;
    }
}
