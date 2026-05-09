package model.road;

import config.Constants;
import generator.IdGenerator;
import model.node.Path;
import model.node.TrafficNode;
import model.observer.ITrafficObserver;
import model.utility.TrafficPoint;
import model.utility.TrafficVector;
import model.vehicle.Vehicle;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Road implements ITrafficObserver {
    private String roadId;
    private TrafficNode startNode;
    private TrafficNode endNode;
    private Way rightWay;
    private Way leftWay;
    private TrafficPoint startPoint;
    private TrafficPoint endPoint;
    private TrafficVector direction;

    public Road(TrafficNode startNode, TrafficNode endNode) {
        this.roadId = IdGenerator.roadId();
        this.startNode = startNode;
        this.endNode = endNode;
        this.direction = new TrafficVector(startNode.getCenterPoint(), endNode.getCenterPoint()).normalize();
        this.startPoint = startNode.getCenterPoint().moveBy(direction.scale(Constants.JUNCTION_RADIUS));
        this.endPoint = endNode.getCenterPoint().moveBy(direction.scale(Constants.JUNCTION_RADIUS).rotateVector(Math.PI));
        this.rightWay = new Way(roadId, direction);
        this.leftWay = new Way(roadId, direction.rotateVector(Math.PI));
        buildWays();
    }

    public void buildWays() {
        rightWay.buildLanes(startPoint, endPoint);
        leftWay.buildLanes(endPoint, startPoint);
    }

    @Override
    public void checkVehicleLeaving() {
        List<Lane> laneList = getLaneList();
        for (Lane lane : laneList) {
            Vehicle vehicle = lane.getVehicleAtLaneEnd();
            if (vehicle != null) {
                // do sth with model.vehicle
                lane.removeVehicle(vehicle);
                TrafficNode incomingNode = lane.getEndPoint().distance(startPoint) < lane.getEndPoint().distance(endPoint)
                                            ? startNode : endNode;
                List<Path> connectedPathList = new ArrayList<>();
                for (Path path : incomingNode.getPathList()) {
                    if (path.getStartPoint().equals(lane.getEndPoint())) {
                        connectedPathList.add(path);
                    }
                }
                if (!connectedPathList.isEmpty()) {
                    int randomIndex = (int)(Math.random() * connectedPathList.size());
                    Path chosenPath = connectedPathList.get(randomIndex);
                    chosenPath.addVehicle(vehicle);
                }
            }
        }
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
        return false;
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
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Road road = (Road) o;
        return Objects.equals(roadId, road.getRoadId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(roadId);
    }

    public List<Lane> getLaneList() {
        List<Lane> laneList = new ArrayList<>();
        laneList.addAll(rightWay.getLaneList());
        laneList.addAll(leftWay.getLaneList());
        return laneList;
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
