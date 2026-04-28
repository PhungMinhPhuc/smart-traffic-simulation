package node;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import path.Path;
import road.Road;
import road.Way;
import road.Lane;


public abstract class Node {
    private Point2D centerPoint;
    private ArrayList<Path> pathList = new ArrayList<Path>();
    private ArrayList<Road> roadList = new ArrayList<Road>();
    private ArrayList<Point2D> entryPointList = new ArrayList<Point2D>();
    private ArrayList<Point2D> exitPointList = new ArrayList<Point2D>();
    private int id;
    private static int nodeQty = 0; 

    public Node(){
        centerPoint = new Point2D.Double(0.0,0.0);
        id = nodeQty;
        nodeQty++;
    }

    public Node(Point2D point){
        centerPoint = (Point2D)point.clone();
        id = nodeQty;
        nodeQty++;
    }

    //build the node from the road list, this method will be called after the road list is updated
    public void buildNode(){
        pathList.clear();
        entryPointList.clear();
        exitPointList.clear();
        buildAllPaths();
        buildAllEntryExitPoint();
        buildAllConflictPoints();
    }

    public void addRoad(Road road){
        //add road to the road list and then build the node again
        roadList.add(road);
        buildNode();
    }

    public void removeRoad(int roadId){
        for(Road road : roadList){
            if(road.getId() == roadId){
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
                                Path path = new Path(entryLane.getEndPoint(), exitLane.getStartPoint());
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
                Point2D conflictPoint = path1.findConflictPoint(path2);
                if(conflictPoint != null){
                    path1.addConflictPoint(path2, conflictPoint);
                    path2.addConflictPoint(path1, conflictPoint);
                }
            }
        }
    }

    public void buildAllEntryExitPoint(){
        for(Path path : pathList){
            entryPointList.add(path.getStartPoint());
            exitPointList.add(path.getEndPoint());
        }
    }

    public Point2D getCenterPoint() {
        return centerPoint;
    }

    public void setCenterPoint(Point2D centerPoint) {
        this.centerPoint = (Point2D)centerPoint.clone();
    }

    public int getId(){
        return id;
    }
}
