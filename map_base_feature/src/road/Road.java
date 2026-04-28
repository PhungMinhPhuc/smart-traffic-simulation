package road;

import java.awt.geom.Point2D;
import road.Light.TrafficLight;

public class Road {
    private Way rightWay;
    private Way leftWay;
    private Point2D startPoint;
    private Point2D endPoint;
    private int id;
    private static int roadQty = 0;

    public Road(Point2D startPoint, Point2D endPoint, int laneCountPerWay, TrafficLight lightStateRightWay, TrafficLight lightStateLeftWay){
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.rightWay = new Way(lightStateRightWay, laneCountPerWay, true, startPoint, endPoint,id);
        this.leftWay = new Way(lightStateLeftWay, laneCountPerWay, false, startPoint, endPoint,id);
        this.id = roadQty;
        roadQty++;
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

    public int getId() {
        return id;
    }

    public Way getRightWay() {
        return rightWay;
    }

    public Way getLeftWay() {
        return leftWay;
    }
}
