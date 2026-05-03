package model.node;

import java.util.ArrayList;
import generator.IdGenerator;
import model.road.Lane;
import model.road.Road;
import model.road.Way;
import model.utility.TrafficPoint;

public abstract class TrafficNode {
    protected TrafficPoint centerPoint;
    protected ArrayList<Path> pathList = new ArrayList<Path>();
    protected ArrayList<Road> roadList = new ArrayList<Road>();
    protected String id;

    public TrafficNode(TrafficPoint point){
        centerPoint = (TrafficPoint)point.clone();
        id = IdGenerator.nodeId();
    }

    //build the node from the road list, this method will be called after the road list is updated
    public void buildNode(){
        pathList.clear();
        buildAllPaths();
        buildAllConflictPoints();
    }

    public void addRoad(Road road){
        //add road to the road list and then build the node again
        roadList.add(road);
        buildNode();
    }

    public void removeRoad(String roadId){
        for(Road road : roadList){
            if(road.getId().equals(roadId)){
                roadList.remove(road);
                buildNode(); //if the road is removed, the node need to be built again
                return;
            }
        }
    }

    public void buildAllPaths(){
        ArrayList<Way> entryWays = new ArrayList<Way>();
        ArrayList<Way> exitWays = new ArrayList<Way>();
        for(Road road : roadList){
            Way rightWay = road.getRightWay();
            Way leftWay = road.getLeftWay();

            //decide wich way is entry way and which way is exit way by the distance from center point to start point of each way
            if(centerPoint.distance(rightWay.getLaneList().get(0).getStartPoint()) //Right way is entry way
                > centerPoint.distance(leftWay.getLaneList().get(0).getStartPoint())){
                    entryWays.add(rightWay);
                    exitWays.add(leftWay);
            } else {
                    entryWays.add(leftWay);
                    exitWays.add(rightWay);
            }

            //build path for each combination of entry way and exit way
            for(Way entryWay : entryWays){
                for(Way exitWay : exitWays){
                    //Only combine by path entry way and exit way if they are not on the same road
                    if(entryWay.getRoadId() != exitWay.getRoadId()){
                        for(Lane entryLane : entryWay.getLaneList()){
                            for(Lane exitLane : exitWay.getLaneList()){
                                Path path = new Path(
                                		IdGenerator.pathId(id,pathList.size()),
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

    public void buildAllConflictPoints(){
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
  

    public TrafficPoint getCenterPoint() {
        return centerPoint;
    }

    public void setCenterPoint(TrafficPoint centerPoint) {
        this.centerPoint = (TrafficPoint)centerPoint.clone();
    }

    public ArrayList<Path> getPathList() {
        return pathList;
    }

    public ArrayList<Road> getRoadList() {
        return roadList;
    }

    public String getId(){
        return id;
    }
}
