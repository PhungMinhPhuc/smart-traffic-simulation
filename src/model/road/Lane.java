package model.road;

import java.util.ArrayList;

import model.utility.TrafficPoint;
import model.utility.TrafficVector;
import model.vehicle.Vehicle;

public class Lane {
    private TrafficPoint startPoint;
    private TrafficPoint endPoint;
    private ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();
    private int positionInWay; //position of the lane in the way, start from 0

    public Lane(){
        startPoint =new TrafficPoint(0.0,0.0);
        endPoint =new TrafficPoint(0.0,0.0);
        positionInWay = 0;
    }

    public Lane(TrafficPoint startPoint, TrafficPoint endPoint,int positionInWay) {
        this.startPoint = startPoint.clone();
        this.endPoint = endPoint.clone();
        this.positionInWay = positionInWay;
    }

    
    //psudo code for adding and removing vehicle, not used in the final version
    public void addVehicle(){
        Vehicle vehicle = new Vehicle(startPoint.clone(), new TrafficVector(startPoint, endPoint));
        vehicleList.add(vehicle);
    }

    public void removeVehicle(String vehicleId){
        for(Vehicle vehicle : vehicleList){
            if(vehicle.getId().equals(vehicleId)){
                vehicleList.remove(vehicle);
                return;
            }
        }
    }

    public ArrayList<Vehicle> getVehicleList() {
		return vehicleList;
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
    public int getPositionInWay() {
        return positionInWay;
    }
}
