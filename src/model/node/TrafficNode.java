package model.node;

import generator.IdGenerator;
import model.road.Road;
import model.road.Way;
import model.road.Lane;
import model.utility.TrafficPoint;
import model.utility.TrafficVector;
import model.transition.IVehicleTransition;
import model.vehicle.Vehicle;
import config.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class TrafficNode implements IVehicleTransition {
    protected String id;
    protected TrafficPoint centerPoint;
    protected List<Road> roadList;
    protected List<Path> pathList;
    protected int pathCounter = 0;

    public TrafficNode(TrafficPoint point) {
        this.id = IdGenerator.nodeId();
        this.centerPoint = (TrafficPoint) point.clone();
        this.roadList = new ArrayList<>();
        this.pathList = new ArrayList<>();
    }

    /**
     * Determines which way of a road is the entry way (closer start to center)
     * vs the exit way (farther start from center).
     * Entry way = the way whose first lane starts FARTHER from this node's center
     * (vehicles arrive from outside).
     */
    private Way getEntryWay(Road road) {
        Way right = road.getRightWay();
        Way left = road.getLeftWay();
        double rightDist = centerPoint.distanceTo(right.getLaneList().get(0).getStartPoint());
        double leftDist = centerPoint.distanceTo(left.getLaneList().get(0).getStartPoint());
        return (rightDist > leftDist) ? right : left;
    }

    private Way getExitWay(Road road) {
        Way right = road.getRightWay();
        Way left = road.getLeftWay();
        double rightDist = centerPoint.distanceTo(right.getLaneList().get(0).getStartPoint());
        double leftDist = centerPoint.distanceTo(left.getLaneList().get(0).getStartPoint());
        return (rightDist > leftDist) ? left : right;
    }

    /**
     * Creates paths from every lane in the entryWay to every lane in the exitWay,
     * and adds them to this node's pathList.
     */
    private void createPaths(Way entryWay, Way exitWay) {
        for (Lane entryLane : entryWay.getLaneList()) {
            for (Lane exitLane : exitWay.getLaneList()) {
                Path path = new Path(
                        IdGenerator.pathId(id, pathList.size()),
                        entryLane.getEndPoint(),
                        exitLane.getStartPoint());
                pathList.add(path);
            }
        }
    }

    /**
     * Adds a new road to the node:
     * 1. Creates paths between the new road's entry way and all existing exit ways.
     * 2. Creates paths between all existing entry ways and the new road's exit way.
     * 3. Rebuilds conflict points for all paths.
     */
    public void addRoad(Road newRoad) {
        Way newEntryWay = getEntryWay(newRoad);
        Way newExitWay = getExitWay(newRoad);

        // New entry -> existing exits
        for (Road road : roadList) {
            createPaths(newEntryWay, getExitWay(road));
        }
        // Existing entries -> new exit
        for (Road road : roadList) {
            createPaths(getEntryWay(road), newExitWay);
        }

        roadList.add(newRoad);
        buildAllConflictPoints();
    }

    /**
     * Removes a road from the node:
     * 1. Removes all paths connected to the road's entry or exit lanes.
     * 2. Rebuilds conflict points for remaining paths.
     */
    public void removeRoad(Road roadToRemove) {
        if (!roadList.contains(roadToRemove)) {
            System.out.println("The road to be removed is not connected to this node");
            return;
        }

        Way entryWay = getEntryWay(roadToRemove);
        Way exitWay = getExitWay(roadToRemove);

        // Remove any path whose start matches an entry lane end,
        // or whose end matches an exit lane start
        pathList.removeIf(path -> entryWay.getLaneList().stream()
                .anyMatch(lane -> path.getStartPoint().equals(lane.getEndPoint()))
                || exitWay.getLaneList().stream()
                        .anyMatch(lane -> path.getEndPoint().equals(lane.getStartPoint())));

        roadList.remove(roadToRemove);
        buildAllConflictPoints();
    }

    public void buildAllConflictPoints() {
        for (Path path : pathList) {
            path.getConflictPointList().clear(); // clear existing conflict points before rebuilding
        }
        int length = pathList.size();
        for (int i = 0; i < length; i++) {
            for (int j = i + 1; j < length; j++) {
                Path path1 = pathList.get(i);
                Path path2 = pathList.get(j);
                TrafficPoint conflictPoint = path1.findConflictPoint(path2);
                if (conflictPoint != null) {
                    path1.addConflictPoint(path2, conflictPoint);
                    path2.addConflictPoint(path1, conflictPoint);
                }
            }
        }
    }

    public boolean containsPoint(TrafficPoint point) {
        // Check if the point is within a certain distance from the center point, if it
        // is, then it is considered as containing the point
        double distance = centerPoint.distanceTo(point);
        return distance <= Constants.JUNCTION_RADIUS;
    }

    // Find the lane that starts at a given point
    public Lane getLaneAtPoint(TrafficPoint point) {
        for (Road road : roadList) {
            // Check right way
            for (Lane lane : road.getRightWay().getLaneList()) {
                if (lane.getStartPoint().equals(point)) {
                    return lane;
                }
            }
            // Check left way
            for (Lane lane : road.getLeftWay().getLaneList()) {
                if (lane.getStartPoint().equals(point)) {
                    return lane;
                }
            }
        }
        return null;
    }

    @Override
    public void transitionVehicles() {
        // Transition all vehicles on all paths of this node
        for (Path path : pathList) {
            java.util.Iterator<Vehicle> vehicleIterator = path.getVehicleList().iterator();
            while (vehicleIterator.hasNext()) {
                Vehicle vehicle = vehicleIterator.next();
                if (vehicle.getPosition().distanceTo(path.getEndPoint()) < Constants.MIN_DISTANCE_TO_END_POINT) {
                    Lane nextLane = getLaneAtPoint(path.getEndPoint());
                    if (nextLane != null) {
                        vehicle.setPosition(path.getEndPoint().clone());
                        // Set vehicle direction to match the lane's direction
                        vehicle.setDirection(new TrafficVector(nextLane.getStartPoint(), nextLane.getEndPoint()));
                        vehicle.setCurrentLane(nextLane);
                        nextLane.addVehicle(vehicle);
                    }
                    vehicleIterator.remove();
                }
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof TrafficNode))
            return false;
        TrafficNode node = (TrafficNode) o;
        return Objects.equals(id, node.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // Getters and Setters
    public TrafficPoint getCenterPoint() {
        return centerPoint;
    }

    public void setCenterPoint(TrafficPoint centerPoint) {
        this.centerPoint = centerPoint.clone();
    }

    public List<Path> getPathList() {
        return pathList;
    }

    public List<Road> getRoadList() {
        return roadList;
    }

    public String getId() {
        return id;
    }
}