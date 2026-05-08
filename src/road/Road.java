package road;

import config.Constants;
import generator.IdGenerator;
import node.TrafficNode;
import utility.TrafficPoint;
import utility.TrafficVector;

public class Road {
    private String roadId;
    private TrafficNode startNode;
    private TrafficNode endNode;
    private Way rightWay;
    private Way leftWay;
    private TrafficPoint startPoint;
    private TrafficPoint endPoint;
    private TrafficVector direction;
    public static int roadIdCounter = 0;

    public Road(TrafficNode startNode, TrafficNode endNode) {
        this.roadId = IdGenerator.roadId(roadIdCounter++);
        this.startNode = startNode;
        this.endNode = endNode;
        this.direction = new TrafficVector(startNode.getCenterPoint(), endNode.getCenterPoint()).normalize();
        this.startPoint = startNode.getCenterPoint().moveBy(direction.scale(Constants.NODE_RADIUS));
        this.endPoint = startNode.getCenterPoint().moveBy(direction.scale(Constants.NODE_RADIUS).rotateVector(Math.PI));
        this.rightWay = new Way(roadId, direction);
        this.leftWay = new Way(roadId, direction.rotateVector(Math.PI));
        buildWays();
    }

    public void buildWays() {
        rightWay.buildLanes(startPoint, endPoint);
        leftWay.buildLanes(endPoint, startPoint);
    }

    public Way getRightWay() {
        return rightWay;
    }

    public Way getLeftWay() {
        return leftWay;
    }

    public String getRoadId() {
        return roadId;
    }

    public TrafficNode getStartNode() {
        return startNode;
    }

    public TrafficNode getEndNode() {
        return endNode;
    }

    public TrafficPoint getStartPoint() {
        return startPoint;
    }

    public TrafficPoint getEndPoint() {
        return endPoint;
    }

    public TrafficVector getDirection() {
        return direction;
    }
}
