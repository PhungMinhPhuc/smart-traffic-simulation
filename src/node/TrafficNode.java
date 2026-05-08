package node;

import generator.IdGenerator;
import road.Road;
import utility.TrafficPoint;
import road.Lane;
import road.Way;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TrafficNode {
    protected String id;
    protected TrafficPoint centerPoint;
    protected List<Path> pathList;
    protected List<Road> roadList;
    private int pathCounter;
    public static int idCounter = 0;

    public TrafficNode(TrafficPoint centerPoint) {
        this.id = IdGenerator.nodeId(idCounter++);
        this.centerPoint = centerPoint;
        this.pathList = new ArrayList<>();
        this.roadList = new ArrayList<>();
        this.pathCounter = 0;
    }

    public void addRoad(Road road){
        buildPaths(road);
        roadList.add(road);
    }

    public void removeRoad(String roadId){
        for(Road road : roadList){
            if(road.getRoadId().equals(roadId)){
                roadList.remove(road);
                return;
            }
        }
    }

    // build paths when add new road
    protected void buildPaths(Road road) {
        Way roadEntryWay = getEntryWay(road);
        Way roadExitWay = getExitWay(road);
        ArrayList<Path> newPaths = new ArrayList<>();

        // build paths between new road and each existing road
        for (Road otherRoad : roadList) {
            Way otherEntryWay = getEntryWay(otherRoad);
            Way otherExitWay = getExitWay(otherRoad);

            addPathsBetweenWays(roadEntryWay, otherExitWay, newPaths);
            addPathsBetweenWays(otherEntryWay, roadExitWay, newPaths);
        }

        // build conflict points after building path
        buildConflictPoints(newPaths);
    }

    // new paths will be added into pathList in this methods
    private void addPathsBetweenWays(Way entryWay, Way exitWay, List<Path> newPaths) {
        if (entryWay.getRoadId().equals(exitWay.getRoadId())) {
            return;
        }

        for (Lane entryLane : entryWay.getLaneList()) {
            for (Lane exitLane : exitWay.getLaneList()) {

                // check if path exists
                if (findPath(entryLane.getEndPoint(), exitLane.getStartPoint()) != null) {
                    continue;
                }

                Path path = new Path(
                        IdGenerator.pathId(id, pathCounter++),
                        entryLane.getEndPoint(),
                        exitLane.getStartPoint()
                );

                pathList.add(path);

                if (newPaths != null) {
                    newPaths.add(path);
                }
            }
        }
    }

    private void buildConflictPoints(List<Path> newPaths) {
        for (Path newPath : newPaths) {
            for (Path existingPath : pathList) {
                if (newPath.equals(existingPath)) {
                    continue;
                }

                TrafficPoint conflictPoint = newPath.findConflictPoint(existingPath);
                if (conflictPoint != null) {
                    newPath.addConflictPoint(existingPath, conflictPoint);
                    existingPath.addConflictPoint(newPath, conflictPoint);
                }
            }
        }
    }

    private Path findPath(TrafficPoint startPoint, TrafficPoint endPoint) {
        for (Path path : pathList) {
            if (path.getStartPoint().equals(startPoint) && path.getEndPoint().equals(endPoint)) {
                return path;
            }
        }

        return null;
    }

    private Way getEntryWay(Road road) {
        Way rightWay = road.getRightWay();
        Way leftWay = road.getLeftWay();

        if (centerPoint.distance(rightWay.getLaneList().get(0).getStartPoint())
                > centerPoint.distance(leftWay.getLaneList().get(0).getStartPoint())) {
            return rightWay;
        }

        return leftWay;
    }

    private Way getExitWay(Road road) {
        Way rightWay = road.getRightWay();
        Way leftWay = road.getLeftWay();
        Way entryWay = getEntryWay(road);

        if (entryWay == rightWay) {
            return leftWay;
        }

        return rightWay;
    }

    public void removePath(Path path) {
        pathList.remove(path);
    }

    // override equals  hashCode to use containsKey of Map
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TrafficNode node = (TrafficNode) o;
        return Objects.equals(id, node.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public TrafficPoint getCenterPoint() {
        return centerPoint;
    }

    public void setCenterPoint(TrafficPoint centerPoint) {
        this.centerPoint = centerPoint;
    }




}
