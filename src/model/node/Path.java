package model.node;

import java.util.ArrayList;
import model.utility.TrafficPoint;
import model.vehicle.Vehicle;

import java.util.HashMap;
import java.util.Map;

public class Path {
    private TrafficPoint startPoint;
    private TrafficPoint endPoint;      
    private Map<Path, TrafficPoint> conflictPointList = new HashMap<Path, TrafficPoint>(); //the key is the path that has conflict with this path, the value is the conflict point of the two paths
    private ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();
    private String id;

    public Path(String id,TrafficPoint startPoint, TrafficPoint endPoint){
        this.startPoint = startPoint.clone();
        this.endPoint = endPoint.clone();
        this.id = id;
    }


	public TrafficPoint findConflictPoint(Path otherPath) {
        // Calculate conflict point using Cramer
        TrafficPoint thisStart = this.getStartPoint();
        TrafficPoint thisEnd = this.getEndPoint();
        TrafficPoint otherStart = otherPath.getStartPoint();
        TrafficPoint otherEnd = otherPath.getEndPoint();

        double thisStartX = thisStart.getX();
        double thisStartY = thisStart.getY();
        double thisEndX = thisEnd.getX();
        double thisEndY = thisEnd.getY();

        double otherStartX = otherStart.getX();
        double otherStartY = otherStart.getY();
        double otherEndX = otherEnd.getX();
        double otherEndY = otherEnd.getY();

        // Main determinant
        double determinant = (thisStartX - thisEndX) * (otherStartY - otherEndY) - (thisStartY - thisEndY) * (otherStartX - otherEndX);

        if (Math.abs(determinant) < 1e-9) {
            return null;
        }

        // Định thức của mỗi node.Path
        double thisDeterminant = thisStartX * thisEndY - thisStartY * thisEndX;
        double otherDeterminant = otherStartX * otherEndY - otherStartY * otherEndX;

        double conflictPointX =
                (thisDeterminant * (otherStartX - otherEndX) - (thisStartX - thisEndX) * otherDeterminant) / determinant;

        double conflictPointY =
                (thisDeterminant * (otherStartY - otherEndY) - (thisStartY - thisEndY) * otherDeterminant) / determinant;

        boolean isOnThisPath = isPointOnSegment(conflictPointX, conflictPointY, thisStartX, thisStartY, thisEndX, thisEndY);

        boolean isOnOtherPath = isPointOnSegment(conflictPointX, conflictPointY, otherStartX, otherStartY, otherEndX, otherEndY);

        if (isOnThisPath && isOnOtherPath) {
            return new TrafficPoint(conflictPointX, conflictPointY);
        }

        return null;
    }

    private boolean isPointOnSegment(double pointX, double pointY, double startX, double startY, double endX, double endY) {
        double epsilon = 1e-9;

        double minX = Math.min(startX, endX) - epsilon;
        double maxX = Math.max(startX, endX) + epsilon;
        double minY = Math.min(startY, endY) - epsilon;
        double maxY = Math.max(startY, endY) + epsilon;

        return pointX >= minX && pointX <= maxX && pointY >= minY && pointY <= maxY;
    }

    public void addConflictPoint(Path otherPath, TrafficPoint conflictPoint) {
        conflictPointList.put(otherPath, conflictPoint);
    }


    public void addVehicle(Vehicle v){
        vehicleList.add(v);
    }

    public void removeVehicle(int vehicleId){
        for(Vehicle v : vehicleList){
            if(v.getId() == vehicleId){
                vehicleList.remove(v);
                return;
            }
        }
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

    public String getId(){
        return id;
    }
}