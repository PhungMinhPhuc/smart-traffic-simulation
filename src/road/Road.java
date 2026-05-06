package road;

import generator.IdGenerator;
import utility.TrafficPoint;
import utility.TrafficVector;

public class Road {
    private String roadId;
    private Way rightWay;
    private Way leftWay;
    private TrafficPoint startPoint;
    private TrafficPoint endPoint;
    private TrafficVector direction;
    public static int roadIdCounter = 0;

    public Road(TrafficPoint startPoint, TrafficPoint endPoint) {
        this.roadId = IdGenerator.roadId(roadIdCounter++);
        this.endPoint = endPoint;
        this.startPoint = startPoint;
        this.direction = new TrafficVector(startPoint, endPoint).normalize();
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
