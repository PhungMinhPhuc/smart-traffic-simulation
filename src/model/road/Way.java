package model.road;

import model.traffic.LightState;
import model.utility.TrafficPoint;
import model.utility.TrafficVector;
import config.Constants;

import java.util.ArrayList;
import java.util.List;

public class Way {
    private String roadId;
    private LightState stateTrafficLight;
    private List<Lane> laneList;
    private TrafficVector direction;

    public Way(String roadId, TrafficVector direction) {
        this.roadId = roadId;
        this.laneList = new ArrayList<>();
        this.stateTrafficLight = LightState.GREEN;
        this.direction = direction.clone();
    }

    public void buildLanes(TrafficPoint leftStart, TrafficPoint leftEnd) {
        TrafficVector normalVector = direction.rotateVector(Math.PI/2);

        for (int index = 0; index < Constants.DEFAULT_LANE_COUNT; index++) {
            double offsetValue = (index + 0.5) * Constants.LANE_WIDTH;
            TrafficVector offset = normalVector.scale(offsetValue);

            TrafficPoint laneStart = leftStart.moveBy(offset);
            TrafficPoint laneEnd = leftEnd.moveBy(offset);

            Lane lane = new Lane(index, laneStart, laneEnd);

            laneList.add(lane);
        }
    }

    public void addLane(Lane lane) {
        laneList.addLast(lane);
    }

    public void removeLane(Lane lane) {
        laneList.remove(lane);
    }

    public List<Lane> getLaneList() {
        return laneList;
    }

    public String getRoadId() {
        return roadId;
    }

    public void setStateTrafficLight(LightState stateTrafficLight) {
        this.stateTrafficLight = stateTrafficLight;
    }

    public LightState getStateTrafficLight() {
        return stateTrafficLight;
    }
}
