package model.road;

import config.Constants;
import generator.IdGenerator;
import model.node.Path;
import model.node.TrafficNode;
import model.traffic_update.ITrafficUpdate;
import model.utility.TrafficPoint;
import model.utility.TrafficVector;
import model.utility.TrafficGeometry;
import model.vehicle.Vehicle;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Road implements ITrafficUpdate {
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
        double halfWidthRoad = Constants.LANE_WIDTH * Constants.DEFAULT_LANE_COUNT;

        return TrafficGeometry.intersectsRectangles(p1, q1, halfWidthRoad, p2, q2, halfWidthRoad);
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
