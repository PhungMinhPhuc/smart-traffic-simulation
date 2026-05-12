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
        double halfWidthRoad = Constants.LANE_WIDTH * Constants.DEFAULT_LANE_COUNT;

        return TrafficGeometry.intersectsRectangles(p1, q1, halfWidthRoad, p2, q2, halfWidthRoad);
    }

    private void checkLaneVehicles(Way way, TrafficNode targetNode) {
        for (Lane lane : way.getLaneList()) {
            for (Iterator<Vehicle> vehicleIterator = lane.getVehicleList().iterator(); vehicleIterator.hasNext();) {
                Vehicle vehicle = vehicleIterator.next();
                if (vehicle.getPosition().distanceTo(lane.getEndPoint()) < Constants.MIN_DISTANCE_TO_END_POINT) {
                    Path chosenPath = ((Junction) targetNode).getRandomPathFromPoint(lane.getEndPoint());

                    if (chosenPath != null) {
                        vehicle.setCurrentLane(null); // Clear lane context — vehicle is now on a path
                        vehicle.setPosition(chosenPath.getStartPoint().clone());
                        vehicle.setDirection(new TrafficVector(chosenPath.getStartPoint(), chosenPath.getEndPoint()));
                        chosenPath.addVehicle(vehicle);
                    }
                    vehicleIterator.remove();
                }
            }
        }
    }

    @Override
    public void transitionVehicles() {
        checkLaneVehicles(rightWay, this.getStartNode());
        checkLaneVehicles(leftWay, this.getEndNode());
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