package road;

import config.Constants;
import utility.TrafficPoint;
import utility.TrafficVector;
import vehicle.Vehicle;

import java.util.ArrayList;
import java.util.List;

public class Lane {
    private int index;
    private List<Vehicle> vehicleList;
    private TrafficPoint startPoint;
    private TrafficPoint endPoint;


    public Lane(int index, TrafficPoint startPoint, TrafficPoint endPoint) {
        this.index = index;
        this.vehicleList = new ArrayList<>();
        this.startPoint = startPoint.clone();
        this.endPoint = endPoint.clone();
    }

    public Vehicle getVehicleAtLaneEnd() {
        for (Vehicle vehicle : vehicleList) {
            if (vehicle.getPosition().distance(endPoint) <= Constants.CALCULATE_DISTANCE) {
                return vehicle;
            }
        }
        return null;
    }

    public void addVehicle(Vehicle vehicle) {
        vehicle.setDirection(startPoint, endPoint);
        vehicle.setPosition(startPoint);
        vehicleList.add(vehicle);
    }

    public void removeVehicle(Vehicle vehicle) {
        vehicleList.remove(vehicle);
    }

    public TrafficPoint getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(TrafficPoint startPoint) {
        this.startPoint = startPoint.clone();
    }

    public TrafficPoint getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(TrafficPoint endPoint) {
        this.endPoint = endPoint.clone();
    }

    public int getIndex() {
        return index;
    }
}
