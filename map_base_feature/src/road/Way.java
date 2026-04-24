package road;

import java.util.ArrayList;
import road.Light.TrafficLight;
import java.awt.geom.Point2D;

public class Way {
    private ArrayList<Lane> laneList;
    private TrafficLight stateTrafficLight;
    
    public Way(){
        laneList = new ArrayList<Lane>();
    }

    public boolean addLane(Lane l){
        return laneList.add(l);
    }

    public boolean removeLane(Point2D startPoint, Point2D endPoint){
        for(Lane l : laneList){
            if(l.getStartPoint().equals(startPoint) && l.getEndPoint().equals(endPoint)){
                laneList.remove(l);
                return true;
            }
        }
        return false;
    }

    public void setStateTrafficLight(TrafficLight stateTrafficLight) {
        this.stateTrafficLight = stateTrafficLight;
    }

    public TrafficLight getStateTrafficLight() {
        return stateTrafficLight;
    }
}
