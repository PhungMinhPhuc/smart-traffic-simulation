package road;

import java.util.ArrayList;
import java.util.List;

public class Way {
    private String id;
    private TrafficLightState stateTrafficLight;
    private List<Lane> laneList;

    public Way() {
        this.laneList = new ArrayList<>();
        this.stateTrafficLight = TrafficLightState.RED;
    }

    public Way(String id, TrafficLightState stateTrafficLight) {
        this.id = id;
        this.stateTrafficLight = stateTrafficLight;
        this.laneList = new ArrayList<>();
    }

    public void addLane(Lane lane) {
        laneList.add(lane);
    }

    public void removeLane(Lane lane) {
        laneList.remove(lane);
    }

    public List<Lane> getLaneList() {
        return laneList;
    }

    public void setStateTrafficLight(TrafficLightState stateTrafficLight) {
        this.stateTrafficLight = stateTrafficLight;
    }

    public TrafficLightState getStateTrafficLight() {
        return stateTrafficLight;
    }
}
