package model.road;

import java.util.ArrayList;
import config.Constants;
import model.traffic.LightState;
import model.utility.TrafficPoint;
import model.utility.TrafficVector;

public class Way {
    private ArrayList<Lane> laneList = new ArrayList<Lane>();
    private LightState stateTrafficLight;
    public final int LANECOUNT;
    private String roadId;


    public Way(LightState lightState, int laneCount,boolean isRightWay,TrafficPoint roadStartPoint, TrafficPoint roadEndPoint, String roadId){
        this.stateTrafficLight = lightState;
        this.LANECOUNT = laneCount;
        this.roadId = roadId;
        //Create lanes base on the lane count and the position of the road
        TrafficVector vectorRoad = new TrafficVector(roadStartPoint, roadEndPoint);
        if(isRightWay){
           TrafficVector translateVector = vectorRoad.rotateVector(Math.toRadians(-90));
            translateVector = translateVector.resizeVector(Constants.LANE_WIDTH/2.0);
            for(int i = 0; i < laneCount; i++){
                int j = 2*i + 1;
              //Vectors of the right way's lanes have the opposite direction compared to the road
                TrafficPoint laneStartPoint = new TrafficPoint(roadEndPoint.getX() + translateVector.getX() * j, roadEndPoint.getY() + translateVector.getY() * j);
                TrafficPoint laneEndPoint = new TrafficPoint(roadStartPoint.getX() + translateVector.getX() * j, roadStartPoint.getY() + translateVector.getY() * j);
                laneList.add(new Lane(laneStartPoint, laneEndPoint, i));
            }
        }
        else{
            TrafficVector translateVector = vectorRoad.rotateVector(Math.toRadians(90));
            translateVector = translateVector.resizeVector(Constants.LANE_WIDTH/2.0);

            for(int i = 0; i < laneCount; i++){
                int j = 2*i + 1;
              //Vectors of the left way's lanes have the same direction to the road
                TrafficPoint laneStartPoint = new TrafficPoint(roadStartPoint.getX() + translateVector.getX() * j, roadStartPoint.getY() + translateVector.getY() * j);
                TrafficPoint laneEndPoint = new TrafficPoint(roadEndPoint.getX() + translateVector.getX() * j, roadEndPoint.getY() + translateVector.getY() * j);
                laneList.add(new Lane(laneStartPoint, laneEndPoint, i));
            }
        }
    }

    public void setStateTrafficLight(LightState stateTrafficLight) {
        this.stateTrafficLight = stateTrafficLight;
    }

    public LightState getStateTrafficLight() {
        return stateTrafficLight;
    }

    public ArrayList<Lane> getLaneList() {
        return laneList;
    }

    public String getRoadId() {
        return roadId;
    }

}
