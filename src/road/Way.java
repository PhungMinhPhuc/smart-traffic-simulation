package road;

import generator.IdGenerator;
import point.TrafficPoint;
import point.TrafficVector;

import java.util.ArrayList;
import java.util.List;

public class Way {
    String wayId;
    private TrafficLightState stateTrafficLight;
    private List<Lane> laneList;
    private TrafficVector direction;
    private final double LANE_WIDTH = 10;
    private final int LANES_PER_WAY = 3;

    public Way(String id, TrafficVector direction) {
        this.wayId = id;
        this.laneList = new ArrayList<>();
        this.stateTrafficLight = TrafficLightState.GREEN;
        this.direction = direction;
    }

    public void buildLanes(TrafficPoint leftStart, TrafficPoint leftEnd) {
        TrafficVector normalVector = direction.rotateRight90();

        for (int index = 0; index < LANES_PER_WAY; index++) {
            double offsetValue = (index + 0.5) * LANE_WIDTH;
            TrafficVector offset = normalVector.scale(offsetValue);

            TrafficPoint laneCenter = leftStart.moveBy(offset);
            TrafficPoint laneEnd = leftEnd.moveBy(offset);

            Lane lane = new Lane(IdGenerator.LaneId(wayId, index), index, laneCenter, laneEnd);

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

    public void setStateTrafficLight(TrafficLightState stateTrafficLight) {
        this.stateTrafficLight = stateTrafficLight;
    }

    public TrafficLightState getStateTrafficLight() {
        return stateTrafficLight;
    }
}
