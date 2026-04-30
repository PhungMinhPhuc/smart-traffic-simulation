package items.node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import items.utility.Point2D;
import items.vehicle.Vehicle;

public class Path {
    private Point2D startPoint;
    private Point2D endPoint;      
    private Map<Path, Point2D> conflictPointList = new HashMap<Path, Point2D>(); //the key is the path that has conflict with this path, the value is the conflict point of the two paths
    private ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();
    private int id;
    private static int pathQty = 0;

    public Path(Point2D startPoint, Point2D endPoint){
        this.startPoint = startPoint.clone();
        this.endPoint = endPoint.clone();
        id = pathQty;
        pathQty++;
    }


	public Point2D findConflictPoint(Path otherPath) {
        // Calculate conflict point using Cramer
        Point2D thisStart = this.getStartPoint();
        Point2D thisEnd = this.getEndPoint();
        Point2D otherStart = otherPath.getStartPoint();
        Point2D otherEnd = otherPath.getEndPoint();

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
            return new Point2D(conflictPointX, conflictPointY);
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

    public void addConflictPoint(Path otherPath, Point2D conflictPoint) {
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

    public Point2D getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(Point2D startPoint) {
        this.startPoint = startPoint;
    }

    public Point2D getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(Point2D endPoint) {
        this.endPoint = endPoint;
    }

    public int getId(){
        return id;
    }
}