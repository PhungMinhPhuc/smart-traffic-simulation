package road;

import java.awt.geom.Point2D;

public class Lane {
    private Point2D startPoint;
    private Point2D endPoint;
    // private ArrayList<Vehicle> vehicleList;

    public Lane(){
        startPoint =new Point2D.Double(0.0,0.0);
        endPoint =new Point2D.Double(0.0,0.0);
    }

    public Lane(Point2D startPoint, Point2D endPoint) {
        this.startPoint = (Point2D)startPoint.clone();
        this.endPoint = (Point2D)endPoint.clone();
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
