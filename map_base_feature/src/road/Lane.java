package road;

import java.util.ArrayList;
import java.awt.geom.Point2D;
import vehicle.Vehicle;

public class Lane {
    private Point2D startPoint;
    private Point2D endPoint;
    public static final double LANEWIDTH = 3.5;
    private ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();
    private int positionInWay; //position of the lane in the way, start from 0

    public Lane(){
        startPoint =new Point2D.Double(0.0,0.0);
        endPoint =new Point2D.Double(0.0,0.0);
        positionInWay = 0;
    }

    public Lane(Point2D startPoint, Point2D endPoint,int positionInWay) {
        this.startPoint = (Point2D)startPoint.clone();
        this.endPoint = (Point2D)endPoint.clone();
        this.positionInWay = positionInWay;
    }

    public Point2D getStartPoint() {
        return startPoint;
    }
    public void setStartPoint(Point2D startPoint) {
        this.startPoint = (Point2D)startPoint.clone();
    }
    public Point2D getEndPoint() {
        return endPoint;
    }
    public void setEndPoint(Point2D endPoint) {
        this.endPoint = (Point2D)endPoint.clone();
    }
    public int getPositionInWay() {
        return positionInWay;
    }
}
