package model.road;

import java.util.*;

import config.*;
import generator.*;
import model.traffic.*;
import model.transition.*;
import model.utility.*;
import model.vehicle.*;
import model.node.*;

public class Road implements IVehicleTransition {
    private Way rightWay;
    private Way leftWay;
    private TrafficPoint startPoint;
    private TrafficPoint endPoint;
    private TrafficNode startNode;
    private TrafficNode endNode;
    private String id;

    /**
     * helper class for sequentially lane change process
     * avoiding multiple lane changes for the same vehicle in one transition step, 
     * which can cause issues like skipping lanes or oscillating between lanes
     */
    private static class LaneChangeRequest {
        final Way way;
        final Lane fromLane;
        final Vehicle vehicle;
        final int offset;

        LaneChangeRequest(Way way, Lane fromLane, Vehicle vehicle, int offset) {
            this.way = way;
            this.fromLane = fromLane;
            this.vehicle = vehicle;
            this.offset = offset;
        }
    }

    public Road(TrafficNode startNode, TrafficNode endNode, int laneCountPerWay, LightState lightStateRightWay,
            LightState lightStateLeftWay) {
        this.startNode = startNode;
        this.endNode = endNode;

        // Calculate distance from center where this road should start
        double startDistance = startNode.computeRoadStartDistance(laneCountPerWay);
        double endDistance = endNode.computeRoadStartDistance(laneCountPerWay);

        // Calculate road start and end point by the center point of start and end node,
        // and the direction vector from start node to end node
        TrafficVector directionVector = new TrafficVector(startNode.getCenterPoint(), endNode.getCenterPoint());
        this.startPoint = directionVector.translatePoint(startNode.getCenterPoint(), startDistance);
        this.endPoint = directionVector.translatePoint(endNode.getCenterPoint(), -endDistance);

        this.id = IdGenerator.roadId();
        this.rightWay = new Way(lightStateRightWay, laneCountPerWay, true, startPoint, endPoint, id);
        this.leftWay = new Way(lightStateLeftWay, laneCountPerWay, false, startPoint, endPoint, id);
    }

    public Road(TrafficNode startNode, TrafficNode endNode) {
        this(startNode, endNode, Constants.DEFAULT_LANE_COUNT);
    }

    public Road(TrafficNode startNode, TrafficNode endNode, int laneCountPerWay) {
        this.startNode = startNode;
        this.endNode = endNode;

        // Calculate distance from center where this road should start
        double startDistance = startNode.computeRoadStartDistance(laneCountPerWay);
        double endDistance = endNode.computeRoadStartDistance(laneCountPerWay);

        // Calculate road start and end point by the center point of start and end node,
        // and the direction vector from start node to end node
        TrafficVector directionVector = new TrafficVector(startNode.getCenterPoint(), endNode.getCenterPoint());
        this.startPoint = directionVector.translatePoint(startNode.getCenterPoint(), startDistance);
        this.endPoint = directionVector.translatePoint(endNode.getCenterPoint(), -endDistance);
        this.id = IdGenerator.roadId();

        // Default traffic light state: GREEN for both ways
        this.rightWay = new Way(LightState.GREEN, laneCountPerWay, true, startPoint, endPoint, id);
        this.leftWay = new Way(LightState.GREEN, laneCountPerWay, false, startPoint, endPoint, id);
    }

    // Check if this road conflicts with another road (i.e., they intersect)
    public boolean checkConflict(Road otherRoad) {
        TrafficPoint p1 = this.startPoint;
        TrafficPoint q1 = this.endPoint;
        TrafficPoint p2 = otherRoad.getStartPoint();
        TrafficPoint q2 = otherRoad.getEndPoint();
        double halfWidthThisRoad = Constants.LANE_WIDTH * this.getRightWay().getLaneList().size();
        double halfWidthOtherRoad = Constants.LANE_WIDTH * otherRoad.getRightWay().getLaneList().size();

        return TrafficGeometry.intersectsRectangles(p1, q1, halfWidthThisRoad, p2, q2, halfWidthOtherRoad);
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
    
    //remove vehicle from current lane and add to target lane, 
    //then update vehicle's position by translating it perpendicular to the lane direction
    private void changeLaneVehicle(Way currentWay,Lane currentLane,Vehicle vehicle,int laneIndexOffset) {
    	//laneIndexOffset: -1 for changing to left lane, +1 for changing to right lane
    	TrafficVector laneDirection = new TrafficVector(currentLane.getStartPoint(), currentLane.getEndPoint()).normalize();
    	TrafficVector perpendicularVector = laneDirection.rotateVector(Math.toRadians(90));
    	
    	int newLaneIndex = currentLane.getIndex() + laneIndexOffset;
    	if(newLaneIndex >= 0 && newLaneIndex < currentWay.getLaneList().size()) {
    		Lane targetLane = currentWay.getLaneList().get(newLaneIndex);
    		currentLane.removeVehicle(vehicle);
			targetLane.addVehicle(vehicle);
	    	vehicle.setPosition(perpendicularVector.translatePoint(vehicle.getPosition(),Constants.LANE_WIDTH * laneIndexOffset));
		}
    }
    
    //process lane change requests sequentially 
    private void checkLaneChange(Way way) {
    	//collect lane change requests from all vehicles first to avoid modifying the lane's vehicle list while iterating
        List<LaneChangeRequest> requests = new ArrayList<>();
        for (Lane lane : way.getLaneList()) {
            for (Vehicle vehicle : lane.getVehicleList()) {
                int offset = vehicle.getPendingLaneChange();
                if (offset != 0) {
                    requests.add(new LaneChangeRequest(way, lane, vehicle, offset));
                }
                vehicle.clearPendingLaneChange();
            }
        }
        //stores vehicles that have already changed lane in this transition
        Set<Vehicle> moved = new HashSet<>();
        //process lane change requests sequentially, if a vehicle has already changed lane in this step, skip any further lane change request for that vehicle to prevent multiple lane changes in one step
        for (LaneChangeRequest req : requests) {
            if (moved.contains(req.vehicle)) {
                continue;
            }
            if (req.way.canVehicleChangeLane(req.vehicle, req.fromLane, req.offset)) {
                changeLaneVehicle(req.way, req.fromLane, req.vehicle, req.offset);
                moved.add(req.vehicle);
            }
        }
    }

    @Override
    public void transitionVehicles() {
        checkLaneVehicles(rightWay, this.getStartNode());
        checkLaneVehicles(leftWay, this.getEndNode());
        checkLaneChange(rightWay);
        checkLaneChange(leftWay);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
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