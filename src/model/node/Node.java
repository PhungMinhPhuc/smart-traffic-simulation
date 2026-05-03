package model.node;

import model.map.TrafficPoint;
import model.map.Road;
import model.map.Way;
// import model.node.Path;
import model.map.Lane;
import config.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public abstract class Node {
    protected String id;
    protected TrafficPoint centerPoint;
    protected List<Road> roadList;
    protected List<Path> pathList;
    protected List<TrafficPoint> entryPoints; // Points where vehicles enter the junction from a Road
    protected List<TrafficPoint> exitPoints; // Points where vehicles leave the junction to enter a Road

    public Node(String id, TrafficPoint centerPoint) {
        this.id = id;
        this.centerPoint = centerPoint;
        this.roadList = new ArrayList<>();
        this.pathList = new ArrayList<>();
        this.entryPoints = new ArrayList<>();
        this.exitPoints = new ArrayList<>();
    }

    public void addRoad(Road road) {
        if (!roadList.contains(road)) {
            roadList.add(road);
        }
    }

    public void buildEntryExitPoints() {
        entryPoints.clear();
        exitPoints.clear();

        for (Road road : roadList) {
            Way incomingWay;
            Way outgoingWay;

            if (road.getStartNode().equals(this)) {
                incomingWay = road.getLeftWay();   // Vehicles coming back
                outgoingWay = road.getRightWay();  // Vehicles going away
            } else {
                incomingWay = road.getRightWay();  // Vehicles arriving
                outgoingWay = road.getLeftWay();   // Vehicles leaving
            }

            // Entry points are the end points of lanes in the incoming way
            for (Lane lane : incomingWay.getLaneList()) {
                entryPoints.add(lane.getEndPoint());
            }

            // Exit points are the start points of lanes in the outgoing way
            for (Lane lane : outgoingWay.getLaneList()) {
                exitPoints.add(lane.getStartPoint());
            }
        }
    }

    // Logic to create internal paths connecting every entry to every exit
    public void buildPaths() {
        pathList.clear();
        int pathCounter = 0;
        for (TrafficPoint entry : entryPoints) {
            for (TrafficPoint exit : exitPoints) {
                // Connect every entry point to every exit point except if they belong to the same road (preventing U-turns)
                if (entry.distanceTo(exit) > Constants.LANE_WIDTH * 0.2) { // Heuristic
                    Path path = new Path(id + "_P" + (pathCounter++), entry, exit, Constants.LANE_WIDTH);
                    pathList.add(path);
                }
            }
        }
    }

    // Calculates where internal paths cross each other.
    public void buildConflictPoints() {
        for (int i = 0; i < pathList.size(); i++) {
            Path pathA = pathList.get(i);
            for (int j = i + 1; j < pathList.size(); j++) {
                Path pathB = pathList.get(j);
                TrafficPoint intersect = pathA.findConflictPoint(pathB);
                
                if (intersect != null) {
                    pathA.addConflictPoint(pathB, intersect);
                    pathB.addConflictPoint(pathA, intersect);
                }
            }
        }
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TrafficPoint getCenterPoint() {
        return centerPoint;
    }

    public void setCenterPoint(TrafficPoint centerPoint) {
        this.centerPoint = centerPoint;
    }

    public List<Road> getRoadList() {
        return roadList;
    }

    public void setRoadList(List<Road> roadList) {
        this.roadList = roadList;
    }

    public List<Path> getPathList() {
        return pathList;
    }

    public void setPathList(List<Path> pathList) {
        this.pathList = pathList;
    }

    public List<TrafficPoint> getEntryPoints() {
        return entryPoints;
    }

    public void setEntryPoints(List<TrafficPoint> entryPoints) {
        this.entryPoints = entryPoints;
    }

    public List<TrafficPoint> getExitPoints() {
        return exitPoints;
    }

    public void setExitPoints(List<TrafficPoint> exitPoints) {
        this.exitPoints = exitPoints;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Node)) return false;
        Node node = (Node) o;
        return Objects.equals(id, node.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}