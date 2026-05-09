package road;

import config.Constants;
import generator.IdGenerator;
import node.Path;
import node.TrafficNode;
import observer.ITrafficObserver;
import utility.TrafficPoint;
import utility.TrafficVector;
import vehicle.Vehicle;

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

    @Override
    public void checkVehicleLeaving() {
        List<Lane> laneList = getLaneList();
        for (Lane lane : laneList) {
            Vehicle vehicle = lane.getVehicleAtLaneEnd();
            if (vehicle != null) {
                // do sth with vehicle
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
        List<Lane> laneList = rightWay.getLaneList();
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
