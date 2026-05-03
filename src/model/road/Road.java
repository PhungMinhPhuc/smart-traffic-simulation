package model.road;

import config.Constants;
import model.node.TrafficNode;
import model.utility.TrafficPoint;
import model.utility.TrafficVector;

public class Road {
    private String id;
    private TrafficNode startNode;
    private TrafficNode endNode;
    private Way rightWay; // Direction: StartNode -> EndNode
    private Way leftWay;  // Direction: EndNode -> StartNode
    private double length;
    

    public Road(String id, TrafficNode start, TrafficNode end, int lanesPerWay) {
        this.id = id;
        this.startNode = start;
        this.endNode = end;

        TrafficPoint pStart = start.getCenterPoint();
        TrafficPoint pEnd = end.getCenterPoint();
        this.length = pStart.distanceTo(pEnd);

        // Calculate direction vector and its perpendicular (normal) vector
        TrafficVector direction = pEnd.subtract(pStart);
        TrafficVector normal = direction.getPerpendicular().normalize();

        double shiftDistance = lanesPerWay * Constants.LANE_WIDTH;

        // Create RightWay. Shift start and end points to the right
        TrafficPoint rightStart = pStart.add(normal.multiply(shiftDistance));
        TrafficPoint rightEnd = pEnd.add(normal.multiply(shiftDistance));
        this.rightWay = new Way(id + "_Right", rightStart, rightEnd, lanesPerWay);

        // Create LeftWay. Calculate direction from End to Start, then shift to its right.
        TrafficVector revDirection = pStart.subtract(pEnd);
        TrafficVector revNormal = revDirection.getPerpendicular().normalize();
        
        TrafficPoint leftStart = pEnd.add(revNormal.multiply(shiftDistance));
        TrafficPoint leftEnd = pStart.add(revNormal.multiply(shiftDistance));
        this.leftWay = new Way(id + "_Left", leftStart, leftEnd, lanesPerWay);
    }

    public void update() {
        // Updates the logic for both Ways (e.g., updating traffic lights) - Đầu chờ thôi, hiện tại thì chưa cần
    }

    //Getters
    public String getId() {
        return id;
    }

    public TrafficNode getStartNode() {
        return startNode;
    }

    public TrafficNode getEndNode() {
        return endNode;
    }

    public Way getRightWay() {
        return rightWay;
    }

    public Way getLeftWay() {
        return leftWay;
    }

    public double getLength() {
        return length;
    }
}