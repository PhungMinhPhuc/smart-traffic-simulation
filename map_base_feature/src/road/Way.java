package road;

import java.util.ArrayList;
import road.Light.TrafficLight;
import java.awt.geom.Point2D;

public class Way {
    private ArrayList<Lane> laneList = new ArrayList<Lane>();
    private TrafficLight stateTrafficLight;
    private final int LANECOUNT;
    private int roadId;

    private Point2D rotateVector(Point2D vector, double radianAngle){
        double cos = Math.cos(radianAngle);
        double sin = Math.sin(radianAngle);
        return new Point2D.Double(cos * vector.getX() - sin * vector.getY(), sin * vector.getX() + cos * vector.getY());
    }

    private Point2D resizeVector(Point2D vector, double newLength){
        double currentLength = vector.distance(0.0, 0.0);
        if(currentLength == 0.0){
            return new Point2D.Double(0.0, 0.0);
        }
        double scale = newLength / currentLength;
        return new Point2D.Double(vector.getX() * scale, vector.getY() * scale);
    }

    public Way(TrafficLight lightState, int laneCount,boolean isRightWay,Point2D roadStartPoint, Point2D roadEndPoint, int roadId){
        this.stateTrafficLight = lightState;
        this.LANECOUNT = laneCount;
        this.roadId = roadId;
        //Create lane base on the lane count and the position of the road
        Point2D vectorRoad = new Point2D.Double(roadEndPoint.getX() - roadStartPoint.getX(), roadEndPoint.getY() - roadStartPoint.getY());
        if(isRightWay){
            Point2D vectorTinhTien = rotateVector(vectorRoad, Math.toRadians(-90));
            vectorTinhTien = resizeVector(vectorTinhTien, Lane.LANEWIDTH/2.0);
            for(int i = 0; i < laneCount; i++){
                int j = 2*i + 1;
                Point2D laneStartPoint = new Point2D.Double(roadStartPoint.getX() + vectorTinhTien.getX() * j, roadStartPoint.getY() + vectorTinhTien.getY() * j);
                Point2D laneEndPoint = new Point2D.Double(roadEndPoint.getX() + vectorTinhTien.getX() * j, roadEndPoint.getY() + vectorTinhTien.getY() * j);
                laneList.add(new Lane(laneStartPoint, laneEndPoint, i));
            }
        }
        else{
            Point2D vectorTinhTien = rotateVector(vectorRoad, Math.toRadians(90));
            vectorTinhTien = resizeVector(vectorTinhTien, Lane.LANEWIDTH/2.0);

            for(int i = 0; i < laneCount; i++){
                int j = 2*i + 1;
                Point2D laneStartPoint = new Point2D.Double(roadStartPoint.getX() + vectorTinhTien.getX() * j, roadStartPoint.getY() + vectorTinhTien.getY() * j);
                Point2D laneEndPoint = new Point2D.Double(roadEndPoint.getX() + vectorTinhTien.getX() * j, roadEndPoint.getY() + vectorTinhTien.getY() * j);
                laneList.add(new Lane(laneStartPoint, laneEndPoint, i));
            }
        }
    }

    public void setStateTrafficLight(TrafficLight stateTrafficLight) {
        this.stateTrafficLight = stateTrafficLight;
    }

    public TrafficLight getStateTrafficLight() {
        return stateTrafficLight;
    }

    public ArrayList<Lane> getLaneList() {
        return laneList;
    }

    public int getRoadId() {
        return roadId;
    }

}

