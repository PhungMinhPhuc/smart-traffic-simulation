package model.node;

import model.map.Point;
import model.map.Road;
import model.map.Way;
import model.traffic.TrafficLight;
import model.traffic.LightState;
import config.Constants;

public class Junction extends Node {
    
    private int currentGreenRoadIndex = 0;
    private double timer = 0;

    public Junction(String id, Point centerPoint) {
        super(id, centerPoint);
    }

    // Initializes the junction once all roads are connected.
    public void initializeJunction() {
        buildEntryExitPoints();
        buildPaths();
        buildConflictPoints();
        initializeLights();
    }

    // Sets all lights to RED initially.
    private void initializeLights() {
        for (Road road : roadList) {
            getIncomingWay(road).getTrafficLight().setState(LightState.RED);
        }
    }

    private Way getIncomingWay(Road road) {
        if (road.getStartNode().equals(this))
            return road.getLeftWay();
        else 
            return road.getRightWay();
    }

    // Handles the Traffic Light cycle. Rotates green lights among the connected roads (Automatic Control)
    public void update(double deltaTime) {
        timer += deltaTime;

        // Current road being managed
        Road activeRoad = roadList.get(currentGreenRoadIndex);
        TrafficLight activeLight = getIncomingWay(activeRoad).getTrafficLight();

        if (timer < Constants.GREEN_DURATION) {
            activeLight.setState(LightState.GREEN);
        } else if (timer < Constants.GREEN_DURATION + Constants.YELLOW_DURATION) {
            activeLight.setState(LightState.YELLOW);
        } else {
            // End of cycle for this road, move to the next one
            activeLight.setState(LightState.RED);
            timer = 0;
            currentGreenRoadIndex = (currentGreenRoadIndex + 1) % roadList.size();
        }
    }

    // Manual override for traffic lights (Click to change).
    public void manualSwitch(Road road) {
        Way incoming = getIncomingWay(road);
        incoming.getTrafficLight().changeColor();
    }
}