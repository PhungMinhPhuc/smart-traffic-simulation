package model.road;

import generator.IdGenerator;
import config.Constants;
import model.traffic.LightState;
import model.utility.TrafficPoint;

public class Road {
    private Way rightWay;
    private Way leftWay;
    private TrafficPoint startPoint;
    private TrafficPoint endPoint;
    private String id;

    public Road(TrafficPoint startPoint, TrafficPoint endPoint, int laneCountPerWay, LightState lightStateRightWay, LightState lightStateLeftWay){
        this.startPoint = startPoint;
        this.id = IdGenerator.roadId();
        this.rightWay = new Way(lightStateRightWay, laneCountPerWay, true, startPoint, endPoint,id); 
        this.leftWay = new Way(lightStateLeftWay, laneCountPerWay, false, startPoint, endPoint,id);
    }

    public Road(TrafficPoint startPoint, TrafficPoint endPoint){
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.id = IdGenerator.roadId();
        this.rightWay = new Way(LightState.GREEN, Constants.DEFAULT_LANE_COUNT, true, startPoint, endPoint, id);
        this.leftWay = new Way(LightState.GREEN, Constants.DEFAULT_LANE_COUNT, false, startPoint, endPoint, id);
    }
    
    //Check if this road conflicts with another road (i.e., they intersect)
    public boolean checkConflict(Road otherRoad) {
        TrafficPoint p1 = this.startPoint;
        TrafficPoint q1 = this.endPoint;
        TrafficPoint p2 = otherRoad.getStartPoint();
        TrafficPoint q2 = otherRoad.getEndPoint();

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
    private int orientation(TrafficPoint p, TrafficPoint q, TrafficPoint r) {
        double val = (q.getY() - p.getY()) * (r.getX() - q.getX()) -
                     (q.getX() - p.getX()) * (r.getY() - q.getY());

        if (Math.abs(val) < 1e-9) return 0; // Line up
        return (val > 0) ? 1 : 2;
    }

     // Check if point q lies on the segment pr
    private boolean onSegment(TrafficPoint p, TrafficPoint q, TrafficPoint r) {
        return q.getX() <= Math.max(p.getX(), r.getX()) && q.getX() >= Math.min(p.getX(), r.getX()) &&
               q.getY() <= Math.max(p.getY(), r.getY()) && q.getY() >= Math.min(p.getY(), r.getY());
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

    public String getId() {
        return id;
    }

    public Way getRightWay() {
        return rightWay;
    }

    public Way getLeftWay() {
        return leftWay;
    }
}

