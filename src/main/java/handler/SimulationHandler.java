package main.java.handler;

import config.Constants;
import javafx.animation.AnimationTimer;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import model.map.TrafficMap;
import model.vehicle.Vehicle;
import render.VehicleRenderer;

public class SimulationHandler {
    private AnimationTimer vehicleTimer;
    private long lastFrameTimeNano = 0;
    private boolean simulationPaused = false;

    private TrafficMap trafficMap;
    private VehicleRenderer vehicleRenderer;
    private render.TrafficLightRenderer trafficLightRenderer;
    private Pane vehicleLayer;
    private Label instructionsLabel;

    public void initialize(TrafficMap trafficMap, VehicleRenderer vehicleRenderer, 
            render.TrafficLightRenderer trafficLightRenderer, Pane vehicleLayer,
            Label instructionsLabel) {
        this.trafficMap = trafficMap;
        this.vehicleRenderer = vehicleRenderer;
        this.trafficLightRenderer = trafficLightRenderer;
        this.vehicleLayer = vehicleLayer;
        this.instructionsLabel = instructionsLabel;

        startVehicleAnimation();
    }

    private void startVehicleAnimation() {
        vehicleTimer = new AnimationTimer() {
            private long lastVehicleAddTimeNano = 0;

            @Override
            public void handle(long now) {
                if (lastFrameTimeNano == 0) {
                    lastFrameTimeNano = now;
                    return;
                }

                double deltaTime = (now - lastFrameTimeNano) / Constants.NANOS_PER_SECOND;
                double deltaTimeSinceLastVehicleAdd = (now - lastVehicleAddTimeNano) / Constants.NANOS_PER_SECOND;
                lastFrameTimeNano = now;
                // Test
                if (deltaTimeSinceLastVehicleAdd >= 3) {
                    if (!trafficMap.getRoadList().isEmpty()) {
                        trafficMap.addDefaultVehicleToRoad(trafficMap.getRoadList().get(0), true);
                    }
                    lastVehicleAddTimeNano = now;
                }

                trafficMap.updateVehicles(deltaTime);
                renderVehicles();
            }
        };
        vehicleTimer.start();
    }

    public void renderVehicles() {
        vehicleLayer.getChildren().clear();
        // Render Vehicles
        for (Vehicle veh : trafficMap.getVehicleList()) {
            vehicleLayer.getChildren().add(vehicleRenderer.render(veh));
        }
        // Render Traffic Lights
        for (model.traffic.TrafficLight light : trafficMap.getTrafficLightList()) {
            vehicleLayer.getChildren().add(trafficLightRenderer.render(light));
        }
    }

    public void pauseSimulation() {
        if (vehicleTimer != null && !simulationPaused) {
            vehicleTimer.stop();
            simulationPaused = true;
            instructionsLabel.setText("Simulation paused");
        }
    }

    public void resumeSimulation() {
        if (vehicleTimer != null && simulationPaused) {
            simulationPaused = false;
            lastFrameTimeNano = 0;
            vehicleTimer.start();
            instructionsLabel.setText("");
        }
    }

    public boolean isPaused() {
        return simulationPaused;
    }
}
