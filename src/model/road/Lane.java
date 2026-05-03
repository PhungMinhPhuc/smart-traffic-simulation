package model.map;

import model.vehicle.Vehicle;
import java.util.ArrayList;
import java.util.List;

public class Lane {
    private String id;
    private int index;
    private Way parentWay;
    private TrafficPoint startPoint;
    private TrafficPoint endPoint;
    
    // List of vehicles currently in this lane.  Usually ordered from the one closest to the exit to the one furthest away.
    private List<Vehicle> vehicleList;
    
    // Current status of the traffic light at the end of this lane
    private boolean isRedLight = false;

    public Lane(String id, int index, Way parentWay, TrafficPoint startPoint, TrafficPoint endPoint) {
        this.id = id;
        this.index = index;
        this.parentWay = parentWay;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.vehicleList = new ArrayList<>();
    }

    // Adds a vehicle to this lane when it enters the road.
    public void addVehicle(Vehicle vehicle) {
        if (!vehicleList.contains(vehicle)) {
            vehicleList.add(vehicle);
        }
    }

    // Removes a vehicle when it leaves the lane or enters a junction.
    public void removeVehicle(Vehicle vehicle) {
        vehicleList.remove(vehicle);
    }
    
        // Check if lane is free ahead
        public boolean isEmptyAhead(Vehicle self, double checkDistance) {
            for (Vehicle v : vehicleList) {
                if (v != self) {
                    double dist = v.getPosition().distanceTo(self.getPosition());
                    if (dist > 0 && dist < checkDistance) {
                        return false;
                    }
                }
            }
            return true;
        }

    // Finds the vehicle directly in front of the given vehicle (Keep Distance)
    public Vehicle getVehicleAhead(Vehicle current) {
        int index = vehicleList.indexOf(current);
        // If the vehicle is found and is not the first one (index 0 is the leader (closest to the endPoint))
        if (index > 0) {
            return vehicleList.get(index - 1);
        }
        return null;
    }

    // Offset = 1 (Right), offset = -1 (Left)
    public Lane getNeighborLane(int offset) {
        int neighborIndex = this.index + offset;
        List<Lane> allLanes = parentWay.getLaneList();
        
        if (neighborIndex >= 0 && neighborIndex < allLanes.size()) {
            return allLanes.get(neighborIndex);
        }
        return null;
    }

    // Returns the angle (direction) of the lane.
    public double getAngle() {
        return startPoint.angleTo(endPoint);
    }

    // Returns the total length of the lane.
    public double getLength() {
        return startPoint.distanceTo(endPoint);
    }
    
    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Way getParentWay() {
        return parentWay;
    }

    public void setParentWay(Way parentWay) {
        this.parentWay = parentWay;
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