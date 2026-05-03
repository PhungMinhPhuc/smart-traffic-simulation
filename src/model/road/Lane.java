package model.road;

import java.util.ArrayList;

import model.utility.TrafficPoint;
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

    public void addVehicle(Vehicle vehicle){
        vehicleList.add(vehicle);
    }

    public void removeVehicle(int vehicleId){
        for(Vehicle vehicle : vehicleList){
            if(vehicle.getId() == vehicleId){
                vehicleList.remove(vehicle);
                return;
            }
        }
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
