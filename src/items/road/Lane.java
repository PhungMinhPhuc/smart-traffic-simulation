package items.road;

import java.util.ArrayList;

import items.utility.Point2D;
import items.vehicle.Vehicle;

public class Lane {
    private Point2D startPoint;
    private Point2D endPoint;
    public static final double LANEWIDTH = 15.0;
    private ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();
    private int positionInWay; //position of the lane in the way, start from 0

    public Lane(){
        startPoint =new Point2D(0.0,0.0);
        endPoint =new Point2D(0.0,0.0);
        positionInWay = 0;
    }

    public Lane(Point2D startPoint, Point2D endPoint,int positionInWay) {
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

    public Point2D getStartPoint() {
        return startPoint;
    }
    public void setStartPoint(Point2D startPoint) {
        this.startPoint = startPoint.clone();
    }
    public Point2D getEndPoint() {
        return endPoint;
    }
    public void setEndPoint(Point2D endPoint) {
        this.endPoint = endPoint.clone();
    }
    public int getPositionInWay() {
        return positionInWay;
    }
}
