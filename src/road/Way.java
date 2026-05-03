package road;

import generator.IdGenerator;
import utility.TrafficPoint;
import utility.TrafficVector;
import config.Constants;

import java.util.ArrayList;
import java.util.List;

public class Way {
    String wayId;
    private TrafficLightState stateTrafficLight;
    private List<Lane> laneList;
    private TrafficVector direction;

    public Way(String id, TrafficVector direction) {
        this.wayId = id;
        this.laneList = new ArrayList<>();
        this.stateTrafficLight = TrafficLightState.GREEN;
        this.direction = direction;
    }

    public void buildLanes(TrafficPoint leftStart, TrafficPoint leftEnd) {
        TrafficVector normalVector = direction.rotateVector(-Math.PI/2);

        for (int index = 0; index < Constants.LANES_PER_WAY; index++) {
            double offsetValue = (index + 0.5) * Constants.LANE_WIDTH;
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
