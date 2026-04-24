package path;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Path {
    private Point2D startPoint;
    private Point2D endPoint;
    private ArrayList<Point2D> conflictPointList;
    private double width;
    // private ArrayList<Vehicle> vehicleList;

    public Path(){
        startPoint = new Point2D.Double(0.0,0.0);
        endPoint = new Point2D.Double(0.0,0.0);
        conflictPointList = new ArrayList<>();
        width = 0.0;
    }

    public Path(Point2D startPoint, Point2D endPoint, float width){
        this.startPoint = (Point2D)startPoint.clone();
        this.endPoint = (Point2D)endPoint.clone();
        this.width = width;
        this.conflictPointList = new ArrayList<Point2D>();
    }

    public void addConflictPoint(Point2D point){
        conflictPointList.add(point);
    }

    public boolean removeConflictPoint(Point2D point){
        for(Point2D p : conflictPointList){
            if(p.equals(point)){
                conflictPointList.remove(point);
                return true;
            }
        }
        return false;
    }

    public Point2D getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(Point2D startPoint) {
        this.startPoint = startPoint;
    }

    public Point2D getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(Point2D endPoint) {
        this.endPoint = endPoint;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }
}
