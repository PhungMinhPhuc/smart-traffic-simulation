package road;

import generator.IdGenerator;
import point.Point;
import point.Vector2D;

public class Road {
    private String roadId;
    private Way fowardWay;
    private Way reverseWay;
    private Point startPoint;
    private Point endPoint;
    private Vector2D direction;
    public static int roadIdCounter = 0;

    public Road(Point startPoint, Point endPoint) {
        this.roadId = IdGenerator.roadId(roadIdCounter++);
        this.endPoint = endPoint;
        this.startPoint = startPoint;
        this.direction = Vector2D.fromPoints(startPoint, endPoint).normalize();
        this.fowardWay = new Way(IdGenerator.fowawrdWayId(roadId), direction);
        this.reverseWay = new Way(IdGenerator.reverseWayId(roadId), direction.rotate180());
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

    public Point getStartPoint() {
        return startPoint;
    }

    public Point getEndPoint() {
        return endPoint;
    }

    public Vector2D getDirection() {
        return direction;
    }
}
