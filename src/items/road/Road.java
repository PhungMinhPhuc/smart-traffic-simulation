package items.road;

import items.road.Light.TrafficLight;
import items.utility.Point2D;

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
        this.id = roadQty;
        roadQty++;
        this.rightWay = new Way(lightStateRightWay, laneCountPerWay, true, startPoint, endPoint,id);
        this.leftWay = new Way(lightStateLeftWay, laneCountPerWay, false, startPoint, endPoint,id);
    }

    public Road(Point2D startPoint, Point2D endPoint){
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.id = roadQty;
        roadQty++;
        this.rightWay = new Way(TrafficLight.GREEN, 2, true, startPoint, endPoint, id);
        this.leftWay = new Way(TrafficLight.GREEN, 2, false, startPoint, endPoint, id);
    }
    
    //Check if this road conflicts with another road (i.e., they intersect)
    public boolean checkConflict(Road otherRoad) {
        Point2D p1 = this.startPoint;
        Point2D q1 = this.endPoint;
        Point2D p2 = otherRoad.getStartPoint();
        Point2D q2 = otherRoad.getEndPoint();

        //4 orientation needed for general and special cases
        int o1 = orientation(p1, q1, p2);
        int o2 = orientation(p1, q1, q2);
        int o3 = orientation(p2, q2, p1);
        int o4 = orientation(p2, q2, q1);

        
        if (o1 != o2 && o3 != o4) {
            return true;
        }


        if (o1 == 0 && onSegment(p1, p2, q1)) return true;

        if (o2 == 0 && onSegment(p1, q2, q1)) return true;

        if (o3 == 0 && onSegment(p2, p1, q2)) return true;

        if (o4 == 0 && onSegment(p2, q1, q2)) return true;

        return false; // Không có điểm chung
    }

    	//Cal orientation of 3 points (p, q, r)
     	//0: Line up
     	//1: ClockWise
     	//2: CounterClockWise
    
    private int orientation(Point2D p, Point2D q, Point2D r) {
        double val = (q.getY() - p.getY()) * (r.getX() - q.getX()) -
                     (q.getX() - p.getX()) * (r.getY() - q.getY());

        if (Math.abs(val) < 1e-9) return 0; // Line up
        return (val > 0) ? 1 : 2;
    }

     // Check if point q lies on the segment pr
    private boolean onSegment(Point2D p, Point2D q, Point2D r) {
        return q.getX() <= Math.max(p.getX(), r.getX()) && q.getX() >= Math.min(p.getX(), r.getX()) &&
               q.getY() <= Math.max(p.getY(), r.getY()) && q.getY() >= Math.min(p.getY(), r.getY());
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

