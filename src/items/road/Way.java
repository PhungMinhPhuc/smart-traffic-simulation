package items.road;

import java.util.ArrayList;

import items.road.Light.TrafficLight;
import items.utility.Point2D;
import items.utility.Vector2D;

public class Way {
    private ArrayList<Lane> laneList = new ArrayList<Lane>();
    private TrafficLight stateTrafficLight;
    public final int LANECOUNT;
    private int roadId;


    public Way(TrafficLight lightState, int laneCount,boolean isRightWay,Point2D roadStartPoint, Point2D roadEndPoint, int roadId){
        this.stateTrafficLight = lightState;
        this.LANECOUNT = laneCount;
        this.roadId = roadId;
        //Create lanes base on the lane count and the position of the road
        Vector2D vectorRoad = new Vector2D(roadStartPoint, roadEndPoint);
        if(isRightWay){
           Vector2D translateVector = vectorRoad.rotateVector(Math.toRadians(-90));
            translateVector = translateVector.resizeVector(Lane.LANEWIDTH/2.0);
            for(int i = 0; i < laneCount; i++){
                int j = 2*i + 1;
              //Vectors of the right way's lanes have the opposite direction compared to the road
                Point2D laneStartPoint = new Point2D(roadEndPoint.getX() + translateVector.getX() * j, roadEndPoint.getY() + translateVector.getY() * j);
                Point2D laneEndPoint = new Point2D(roadStartPoint.getX() + translateVector.getX() * j, roadStartPoint.getY() + translateVector.getY() * j);
                laneList.add(new Lane(laneStartPoint, laneEndPoint, i));
            }
        }
        else{
            Vector2D translateVector = vectorRoad.rotateVector(Math.toRadians(90));
            translateVector = translateVector.resizeVector(Lane.LANEWIDTH/2.0);

            for(int i = 0; i < laneCount; i++){
                int j = 2*i + 1;
              //Vectors of the left way's lanes have the same direction to the road
                Point2D laneStartPoint = new Point2D(roadStartPoint.getX() + translateVector.getX() * j, roadStartPoint.getY() + translateVector.getY() * j);
                Point2D laneEndPoint = new Point2D(roadEndPoint.getX() + translateVector.getX() * j, roadEndPoint.getY() + translateVector.getY() * j);
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
