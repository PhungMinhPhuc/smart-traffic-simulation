package node;

import generator.IdGenerator;
import point.Point;
import road.Lane;
import road.Way;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class Node {
    protected String id;
    protected Point centerPoint;
    protected List<Path> pathList;
    protected List<Way> entryWayList;
    protected List<Way> exitWayList;
    protected int maxConnections;
    public static int idCounter = 0;

    public Node(Point centerPoint) {
        this.id = IdGenerator.NodeId(idCounter++);
        this.centerPoint = centerPoint;
        this.pathList = new ArrayList<>();
        this.entryWayList = new ArrayList<>();
        this.exitWayList = new ArrayList<>();
    }

    protected void addPathsBetween(Way entryWay, Way exitWay) {
        for (Lane entryLane : entryWay.getLaneList()) {
            for (Lane exitLane : exitWay.getLaneList()) {
                Path path = new Path(
                        IdGenerator.PathId(id, pathList.size()),
                        entryLane.getEndPoint(),
                        exitLane.getStartPoint()
                );
                addPath(path);
            }
        }
    }

    protected void addPathForEntryWay(Way entryWay) {
        for (Way exitWay : exitWayList) {
            addPathsBetween(entryWay, exitWay);
        }
    }

    protected void addPathForExitWay(Way exitWay) {
        for (Way entryWay : entryWayList) {
            addPathsBetween(entryWay, exitWay);
        }
    }

    // Avoid turning back to current road
    public void addWay(Way entryWay, Way exitWay) {
        if (entryWayList.size() == maxConnections) {
            return;
        }
        if (entryWayList.contains(entryWay)) {
            addPathForEntryWay(entryWay);
            entryWayList.add(entryWay);
        }
        if (exitWayList.contains(exitWay)) {
            addPathForExitWay(exitWay);
            exitWayList.add(exitWay);
        }
    }

    // Build conflict point list after adding a path
    public void buildConflictPointList(Path currentPath) {
        for (Path otherPath : pathList) {
            Point conflictPoint = currentPath.findConflictPoint(otherPath);
            if (conflictPoint != null) {
                currentPath.addConflictPoint(otherPath, conflictPoint);
                otherPath.addConflictPoint(currentPath, conflictPoint);
            }
        }
    }

    public void addPath(Path path) {
        pathList.add(path);
        buildConflictPointList(path);
    }

    public void removePath(Path path) {
        pathList.remove(path);
    }

    // override equals  hashCode to use containsKey of Map
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return Objects.equals(id, node.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public Point getCenterPoint() {
        return centerPoint;
    }

    public void setCenterPoint(Point centerPoint) {
        this.centerPoint = centerPoint;
    }




}
