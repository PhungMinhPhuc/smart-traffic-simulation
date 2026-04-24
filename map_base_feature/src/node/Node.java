package node;
import java.awt.geom.Point2D;
import path.Path;
import road.Road;

import java.util.ArrayList;

public abstract class Node {
    private Point2D centerPoint;
    private ArrayList<Path> pathList = new ArrayList<Path>();
    private ArrayList<Road> roadList = new ArrayList<Road>();
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

    public boolean addPath(Path p){
        return pathList.add(p);
    }

    public boolean removePath(Path p){
        return removePath(p);
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
