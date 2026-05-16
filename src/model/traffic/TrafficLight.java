package model.traffic;

import config.Constants;
import model.utility.TrafficPoint;
import model.road.Lane;
import java.util.ArrayList;

public class TrafficLight {
    private LightState currentState;
    private double internalTimer;
    private int displayMode; // 0: No timer, 1: Full timer, 2: Only show < 10s
    private TrafficPoint position;
    private double rotation; // In degrees

    public TrafficLight(TrafficPoint position, int displayMode, double rotation) {
        this.position = position;
        this.currentState = LightState.RED;
        this.internalTimer = Constants.RED_DURATION;
        this.displayMode = displayMode;
        this.rotation = rotation;
    }

    public void update(double deltaTime, ArrayList<Lane> controlledLanes) {
        internalTimer -= deltaTime;
        if (internalTimer <= 0) {
            changeColor();
        }
        syncLanes(controlledLanes);
    }

    // Logic: Red -> Green -> Yellow -> Red
    public void changeColor() {
        if (currentState == LightState.RED) {
            setState(LightState.GREEN);
            internalTimer = Constants.GREEN_DURATION;
        } else if (currentState == LightState.GREEN) {
            setState(LightState.YELLOW);
            internalTimer = Constants.YELLOW_DURATION;
        } else if (currentState == LightState.YELLOW) {
            setState(LightState.RED);
            internalTimer = Constants.RED_DURATION;
        }
    }

    public void setLightState(LightState state, double duration, ArrayList<Lane> controlledLanes) {
        this.currentState = state;
        this.internalTimer = duration;
        syncLanes(controlledLanes);
    }

    public void syncLanes(ArrayList<Lane> controlledLanes) {
        if (controlledLanes == null)
            return;
        boolean shouldStop = (currentState == LightState.RED || currentState == LightState.YELLOW);
        for (Lane lane : controlledLanes) {
            lane.setRedLight(shouldStop);
        }
    }

    // GUI
    public String getDisplayText() {
        if (displayMode == 0)
            return "";
        if (displayMode == 2 && internalTimer > 8)
            return "";
        return String.valueOf((int) Math.ceil(internalTimer));
    }

    // Getters and Setters
    public TrafficPoint getPosition() {
        return position;
    }

    public double getRotation() {
        return rotation;
    }

    public void setPosition(TrafficPoint position) {
        this.position = position;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
    }

    public LightState getCurrentState() {
        return currentState;
    }

    public void setState(LightState state) {
        this.currentState = state;
    }

    public void setDisplayMode(int displayMode) {
        this.displayMode = displayMode;
    }
}