package model.traffic;

import config.Constants;
import model.utility.TrafficPoint;

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

    public void update(double deltaTime) {
        internalTimer -= deltaTime;
        if (internalTimer <= 0) {
            changeColor();
        }
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

    // GUI
    public String getDisplayText() {
        if (displayMode == 0)
            return "";
        if (displayMode == 2 && internalTimer > 10)
            return "";
        return String.valueOf((int) Math.ceil(internalTimer));
    }

    public model.utility.TrafficPoint getPosition() {
        return position;
    }

    public double getRotation() {
        return rotation;
    }

    // Getters and Setters
    public LightState getCurrentState() {
        return currentState;
    }

    public void setState(LightState state) {
        this.currentState = state;
    }
}