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
    public static int idCounter = 0;

    public TrafficNode(TrafficPoint centerPoint) {
        this.id = IdGenerator.nodeId(idCounter++);
        this.centerPoint = centerPoint;
        this.pathList = new ArrayList<>();
        this.roadList = new ArrayList<>();
    }

    public void addRoad(Road road){
        //add road to the road list and then build the node again
        roadList.add(road);
        buildNode();
    }

    public void removeRoad(String roadId){
        for(Road road : roadList){
            if(road.getRoadId().equals(roadId)){
                roadList.remove(road);
                buildNode(); //if the road is removed, the node need to be built again
                return;
            }
        }
    }

    protected void buildNode(){
        pathList.clear();
        buildAllPaths();
        buildAllConflictPoints();
    }

    protected void buildAllPaths(){
        ArrayList<Way> entryWays = new ArrayList<Way>();
        ArrayList<Way> exitWays = new ArrayList<Way>();
        for(Road road : roadList){
            Way rightWay = road.getRightWay();
            Way leftWay = road.getLeftWay();

            //decide which way is entryway and which way is exit way by the distance from center point to start point of each way
            if(centerPoint.distance(rightWay.getLaneList().get(0).getStartPoint()) //Right way is entryway
                    > centerPoint.distance(leftWay.getLaneList().get(0).getStartPoint())){
                entryWays.add(rightWay);
                exitWays.add(leftWay);
            } else {
                entryWays.add(leftWay);
                exitWays.add(rightWay);
            }

            //build path for each combination of entryway and exit way
            for(Way entryWay : entryWays){
                for(Way exitWay : exitWays){
                    //Only combine by path entryway and exit way if they are not on the same road
                    if(entryWay.getRoadId() != exitWay.getRoadId()){
                        for(Lane entryLane : entryWay.getLaneList()){
                            for(Lane exitLane : exitWay.getLaneList()){
                                Path path = new Path(
                                        IdGenerator.pathId(id, pathList.size()),
                                        entryLane.getEndPoint(),
                                        exitLane.getStartPoint());
                                pathList.add(path);
                            }
                        }
                    }
                }
            }
        }
    }


    protected void buildAllConflictPoints(){
        int length = pathList.size();
        for(int i = 0; i < length; i++){
            for(int j = i + 1; j < length; j++){
                Path path1 = pathList.get(i);
                Path path2 = pathList.get(j);
                TrafficPoint conflictPoint = path1.findConflictPoint(path2);
                if(conflictPoint != null){
                    path1.addConflictPoint(path2, conflictPoint);
                    path2.addConflictPoint(path1, conflictPoint);
                }
            }
        }
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
