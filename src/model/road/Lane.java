package model.road;

import model.utility.TrafficPoint;
import model.vehicle.Vehicle;
import java.util.ArrayList;
import java.util.List;

public class Lane {
    private int index;
    private TrafficPoint startPoint;
    private TrafficPoint endPoint;
    private List<Vehicle> vehicleList;
    private boolean isRedLight = false;

    public Lane(int index, TrafficPoint startPoint, TrafficPoint endPoint) {
        this.index = index;
        this.startPoint = startPoint.clone();
        this.endPoint = endPoint.clone();
        this.vehicleList = new ArrayList<>();
    }

    // Adds a vehicle to this lane when it enters the road.
    public void addVehicle(Vehicle vehicle) {
        if (!vehicleList.contains(vehicle)) {
            vehicleList.add(vehicle);
            vehicle.setDirection(new model.utility.TrafficVector(startPoint, endPoint));
            vehicle.setPosition(startPoint.clone());
        }
    }

    // Removes a vehicle when it leaves the lane or enters a junction.
    public void removeVehicle(Vehicle vehicle) {
        vehicleList.remove(vehicle);
    }

    // Finds the vehicle directly in front of the given vehicle (Keep Distance)
    public Vehicle getVehicleAhead(Vehicle current) {
        int index = vehicleList.indexOf(current);
        // If the vehicle is found and is not the first one (index 0 is the leader
        // (closest to the endPoint))
        if (index > 0) {
            return vehicleList.get(index - 1);
        }
        return null;
    }

    // Getters and Setters
    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
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

    public List<Vehicle> getVehicleList() {
        return vehicleList;
    }

    public void setVehicleList(List<Vehicle> vehicleList) {
        this.vehicleList = vehicleList;
    }

    public boolean isRedLight() {
        return isRedLight;
    }

    public void setRedLight(boolean isRedLight) {
        this.isRedLight = isRedLight;
    }
}