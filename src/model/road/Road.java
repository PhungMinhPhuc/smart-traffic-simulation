package model.road;

import generator.IdGenerator;

import java.util.ArrayList;

import config.Constants;
import model.traffic.LightState;
import model.utility.TrafficPoint;
import model.utility.TrafficVector;
import model.vehicle.Vehicle;
import model.node.Path;
import model.node.TrafficNode;
import model.observer.ITrafficObsever;

public class Road implements ITrafficObsever{
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
    
	@Override
	public void checkVehicleLeaving() {
		//check right way
		for(Lane lane : rightWay.getLaneList()) {
			for(Vehicle vehicle : lane.getVehicleList()) {
				if(vehicle.getPosition().distance(lane.getEndPoint()) < 5) { //if vehicle get close enough to lane's endPoint
					//store connected Path for vehicle to enter afer leaving the road
					ArrayList<Path> connectedPaths = new ArrayList<Path>();
					for(Path path : this.getStartNode().getPathList()) { //Left way goes to Road's start node
						if(path.getStartPoint().equals(lane.getEndPoint())) {
							connectedPaths.add(path);
						}
					}
					
					//choose randomly one of the connected paths for the vehicle to enter, if there is no connected path, then the vehicle will be removed from the lane
					if(!connectedPaths.isEmpty()) {
						int randomIndex = (int)(Math.random() * connectedPaths.size());
						Path chosenPath = connectedPaths.get(randomIndex);
						lane.getVehicleList().remove(vehicle); //remove vehicle from the lane
						chosenPath.addVehicle(vehicle); //add vehicle to the chosen path
					} 
					else {
						lane.getVehicleList().remove(vehicle); //remove vehicle from the lane if there is no connected path for it to enter
					}
				}
			}
		}
		
		//check left way
		for(Lane lane : leftWay.getLaneList()) {
			for(Vehicle vehicle : lane.getVehicleList()) {
				if(vehicle.getPosition().distance(lane.getEndPoint()) < 1e-9) { //if vehicle get close enough to lane's endPoint
					//store connected Path for vehicle to enter afer leaving the road
					ArrayList<Path> connectedPaths = new ArrayList<Path>();
					for(Path path : this.getEndNode().getPathList()) { //Left way goes to Road's end node
						if(path.getStartPoint().equals(lane.getEndPoint())) {
							connectedPaths.add(path);
						}
					}
					
					//choose randomly one of the connected paths for the vehicle to enter, if there is no connected path, then the vehicle will be removed from the lane
					if(!connectedPaths.isEmpty()) {
						int randomIndex = (int)(Math.random() * connectedPaths.size());
						Path chosenPath = connectedPaths.get(randomIndex);
						chosenPath.addVehicle(vehicle); //add vehicle to the chosen path
						lane.getVehicleList().remove(vehicle); //remove vehicle from the lane
					} 
					else {
						lane.getVehicleList().remove(vehicle); //remove vehicle from the lane if there is no connected path for it to enter
					}
				}
			}
		}
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

