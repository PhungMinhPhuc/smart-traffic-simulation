package node;

import point.Point;
import road.Lane;
import road.Road;
import road.Way;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class Node {
    private String id;
    private Point centerPoint;
    private List<Path> pathList;
    private List<Point> entryPointList;
    private List<Point> exitPointList;
    private List<Road> roadList;

    public Node(String id, Point centerPoint) {
        this.id = id;
        this.centerPoint = centerPoint;
        this.pathList = new ArrayList<>();
        this.entryPointList = new ArrayList<>();
        this.exitPointList = new ArrayList<>();
        this.roadList = new ArrayList<>();
    }

    // build road connect to other nodes
    public void addConnection(Node other) {
        this.roadList.add(new Road(this, other));
    }

    // build entrys and exits
    public void buildEntryExitPoint() {
        entryPointList.clear();
        exitPointList.clear();
        for (Road road : roadList) {
            Way entryWay;
            Way exitWay;
            if (road.getStartNode().equals(this)) {
                entryWay = road.getLeftWay();
                exitWay = road.getRightWay();
            } else {
                entryWay = road.getRightWay();
                exitWay = road.getLeftWay();
            }
            for (Lane lane : exitWay.getLaneList()) {
                exitPointList.add(lane.getStartPoint());
            }
            for (Lane lane : entryWay.getLaneList()) {
                entryPointList.add(lane.getEndPoint());
            }
        }
    }

    // build all path when create node
    public void buildPath() {
        int idx = 0;
        for (Point entry : entryPointList) {
            for (Point exit : exitPointList) {
                Path path =  new Path(String.valueOf(idx), entry, exit, 10);
                addPath(path);
                idx++;
            }
        }
    }

    public void addPath(Path path) {
        pathList.add(path);
    }

    public void removePath(Path path) {
        pathList.remove(path);
    }

    // Build conflict point after build path
    public void buildConflictPoint() {

        for (int i = 0; i < pathList.size(); i++) {
            Path currentPath = pathList.get(i);
            for (int j = i+1; j < pathList.size(); j++) {
                Path otherPath = pathList.get(j);
                Point conflictPoint = currentPath.findConflictPoint(otherPath);

                // Thêm conflict point vào danh sách của 2 path nếu tồn tại
                if (conflictPoint != null) {
                    currentPath.addConflictPoint(otherPath, conflictPoint);
                    otherPath.addConflictPoint(currentPath, conflictPoint);
                }
            }
        }
    }

    // Build conflict point when add path
    public void buildConflictPoint(Path path) {
        for (Path otherPath : pathList) {
            Point conflictPoint = path.findConflictPoint(otherPath);
            if (conflictPoint != null) {
                otherPath.addConflictPoint(path, conflictPoint);
                path.addConflictPoint(otherPath, conflictPoint);
            }
        }
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
