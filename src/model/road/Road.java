package model.road;

import generator.IdGenerator;
import config.Constants;
import model.traffic.*;
import model.utility.TrafficPoint;
import model.utility.TrafficVector;
import model.vehicle.*;
import model.node.Junction;
import model.node.TrafficNode;
import model.node.Path;
import java.util.Iterator;

public class Road {
	private Way rightWay;
    private Way leftWay;
    private TrafficPoint startPoint;
    private TrafficPoint endPoint;
    private TrafficNode startNode;
    private TrafficNode endNode;
    private String id;

    public Road(TrafficNode startNode, TrafficNode endNode, int laneCountPerWay, LightState lightStateRightWay, LightState lightStateLeftWay){
        this.startNode = startNode;
        this.endNode = endNode;
        
        //calculate road start and end point by the center point of start and end node, 
        //and the direction vector from start node to end node
        TrafficVector directionVector = new TrafficVector(startNode.getCenterPoint(), endNode.getCenterPoint());
        this.startPoint = directionVector.translatePoint(startNode.getCenterPoint(),Constants.JUNCTION_RADIUS);
        this.endPoint = directionVector.translatePoint(endNode.getCenterPoint(),-Constants.JUNCTION_RADIUS);
        
        this.id = IdGenerator.roadId();
        this.rightWay = new Way(lightStateRightWay, laneCountPerWay, true, startPoint, endPoint,id); 
        this.leftWay = new Way(lightStateLeftWay, laneCountPerWay, false, startPoint, endPoint,id);
    }

    public Road(TrafficNode startNode, TrafficNode endNode){
        this.startNode = startNode;
        this.endNode = endNode;
        
        //calculate road start and end point by the center point of start and end node, 
        //and the direction vector from start node to end node
        TrafficVector directionVector = new TrafficVector(startNode.getCenterPoint(), endNode.getCenterPoint());
        this.startPoint = directionVector.translatePoint(startNode.getCenterPoint(),Constants.JUNCTION_RADIUS);
        this.endPoint = directionVector.translatePoint(endNode.getCenterPoint(),-Constants.JUNCTION_RADIUS);
        this.id = IdGenerator.roadId();
        
        //default setting for Road constructor with default lane count and traffic light state
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
    
    private void checkLaneVehicles(Way way, TrafficNode targetNode) {
        for (Lane lane : way.getLaneList()) {
            for (Iterator<Vehicle> vehicleIterator = lane.getVehicleList().iterator(); vehicleIterator.hasNext();) {
                Vehicle vehicle = vehicleIterator.next();
                if (vehicle.getPosition().distanceTo(lane.getEndPoint()) < Constants.MIN_DISTANCE_TO_END_POINT) {
                    Path chosenPath = ((Junction) targetNode).getRandomPathFromPoint(lane.getEndPoint());

                    if (chosenPath != null) {
                        vehicle.setPosition(chosenPath.getStartPoint().clone());
                        vehicle.setDirection(new TrafficVector(chosenPath.getStartPoint(), chosenPath.getEndPoint()));
                        chosenPath.addVehicle(vehicle);
                    }
                    vehicleIterator.remove();
                }
            }
        }
    }

    public void checkVehicleLeaving() {
        checkLaneVehicles(rightWay, this.getStartNode());
        checkLaneVehicles(leftWay, this.getEndNode());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Road other = (Road) obj;
        return id.equals(other.id);
    }

    public TrafficNode getStartNode() {
		return startNode;
	}

	public void setStartNode(TrafficNode startNode) {
		this.startNode = startNode;
	}

	public TrafficNode getEndNode() {
		return endNode;
	}

	public void setEndNode(TrafficNode endNode) {
		this.endNode = endNode;
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