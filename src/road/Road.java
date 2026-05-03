package road;

import generator.IdGenerator;
import utility.TrafficPoint;
import utility.TrafficVector;

public class Road {
    private String roadId;
    private Way fowardWay;
    private Way reverseWay;
    private TrafficPoint startPoint;
    private TrafficPoint endPoint;
    private TrafficVector direction;
    public static int roadIdCounter = 0;

    public Road(TrafficPoint startPoint, TrafficPoint endPoint) {
        this.roadId = IdGenerator.roadId(roadIdCounter++);
        this.endPoint = endPoint;
        this.startPoint = startPoint;
        this.direction = new TrafficVector(startPoint, endPoint).normalize();
        this.fowardWay = new Way(IdGenerator.fowawrdWayId(roadId), direction);
        this.reverseWay = new Way(IdGenerator.reverseWayId(roadId), direction.rotateVector(Math.PI));
        buildWays();
    }

    public void buildWays() {
        fowardWay.buildLanes(startPoint, endPoint);
        reverseWay.buildLanes(endPoint, startPoint);
    }

    public Way getFowardWay() {
        return fowardWay;
    }

    public Way getReverseWay() {
        return reverseWay;
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
