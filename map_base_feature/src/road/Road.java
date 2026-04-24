package road;

import java.awt.geom.Point2D;

public class Road {
    private Way rightWay = new Way();
    private Way leftWay = new Way();
    private Point2D startPoint;
    private Point2D endPoint;

    public Road(){
        startPoint = new Point2D.Double(0.0,0.0);
        endPoint = new Point2D.Double(0.0,0.0);
    }

    public Road(Point2D startPoint, Point2D endPoint) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
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
}
