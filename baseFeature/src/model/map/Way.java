package model.map;

import model.traffic.LightState;
import model.traffic.TrafficLight;
import java.util.ArrayList;
import java.util.List;
import config.Constants;

// Represents one direction of travel on a road. Manages multiple parallel lanes and the traffic light for this direction.
public class Way {
    private String id;
    private List<Lane> laneList;
    private Point startPoint;
    private Point endPoint;
    private TrafficLight trafficLight; // Current state of the traffic light for this direction

    public Way(String id, Point start, Point end, int numberOfLanes) {
        this.id = id;
        this.startPoint = start;
        this.endPoint = end;
        this.laneList = new ArrayList<>();
        this.trafficLight = new TrafficLight(1); // Default = full timer
        generateLanes(numberOfLanes);
    }

    // Generates parallel lanes using perpendicular vector offsets. Lanes are indexed from 0 (innermost/left) to n-1 (outermost/right).
    public void generateLanes(int numberOfLanes) {
        Vector2D direction = endPoint.subtract(startPoint);
        Vector2D normal = direction.getPerpendicular().normalize();
        for (int i = 0; i < numberOfLanes; i++) {
            // Calculate the offset for the center of this specific lane. Formula: (i + 0.5) * width ensures the first lane is shifted by half-width
            double offsetDist = (i + 0.5) * Constants.LANE_WIDTH;
            Vector2D offsetVector = normal.multiply(offsetDist);

            // Shift both start and end points to create a parallel line
            Point laneStart = startPoint.add(offsetVector);
            Point laneEnd = endPoint.add(offsetVector);

            // Create the lane with an index and a reference back to this Way
            Lane lane = new Lane(id + "_L" + i, i, this, laneStart, laneEnd);
            laneList.add(lane);
        }
    }

    // Updates the traffic light state and notifies all lanes. In a Smart City, this is called by the TrafficController.
    public void setTrafficLight(TrafficLight newState) {
        this.trafficLight = newState;
        boolean redSignal = (newState.getCurrentState() == LightState.RED);
        
        // Push the red light status to all lanes so vehicles know to stop
        for (Lane lane : laneList) {
            lane.setRedLight(redSignal);
        }
    }

    // Getters
    public String getId() {
        return id;
    }

    public List<Lane> getLaneList() {
        return laneList;
    }

    public TrafficLight getTrafficLight() {
        return trafficLight;
    }

    public Point getStartPoint() {
        return startPoint;
    }

    public Point getEndPoint() {
        return endPoint;
    }
}