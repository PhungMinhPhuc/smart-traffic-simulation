package main.java.handler;

import config.Constants;
import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import model.map.TrafficMap;
import model.vehicle.Vehicle;
import model.traffic.TrafficLight;
import render.IVehicleRenderer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SimulationHandler {
    private AnimationTimer vehicleTimer;
    private long lastFrameTimeNano = 0;
    private boolean simulationPaused = false;

    private TrafficMap trafficMap;
    private IVehicleRenderer vehicleRenderer;
    private render.TrafficLightRenderer trafficLightRenderer;
    private Pane vehicleLayer;
    private Pane lightLayer;
    private Label instructionsLabel;
    private final Map<Vehicle, Group> vehicleNodeMap = new HashMap<>();
    private final Map<TrafficLight, Group> trafficLightNodeMap = new HashMap<>();

    public void initialize(TrafficMap trafficMap, IVehicleRenderer vehicleRenderer,
            render.TrafficLightRenderer trafficLightRenderer, Pane vehicleLayer, Pane lightLayer,
            Label instructionsLabel) {
        this.trafficMap = trafficMap;
        this.vehicleRenderer = vehicleRenderer;
        this.trafficLightRenderer = trafficLightRenderer;
        this.vehicleLayer = vehicleLayer;
        this.lightLayer = lightLayer;
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
                syncSceneNodes();
            }
        };
        vehicleTimer.start();
    }

    public void syncSceneNodes() {
        Set<Vehicle> activeVehicles = new HashSet<>();
        for (Vehicle veh : trafficMap.getVehicleList()) {
            activeVehicles.add(veh);

            Group node = vehicleNodeMap.get(veh);
            if (node == null) {
                node = vehicleRenderer.createNode(veh);
                vehicleNodeMap.put(veh, node);
                vehicleLayer.getChildren().add(node);
            } else {
                vehicleRenderer.updateNode(node, veh);
            }
        }

        vehicleNodeMap.entrySet().removeIf(entry -> {
            if (!activeVehicles.contains(entry.getKey())) {
                vehicleLayer.getChildren().remove(entry.getValue());
                return true;
            }
            return false;
        });

        Set<TrafficLight> activeLights = new HashSet<>();
        for (TrafficLight light : trafficMap.getTrafficLightList()) {
            activeLights.add(light);

            Group node = trafficLightNodeMap.get(light);
            if (node == null) {
                node = trafficLightRenderer.createNode(light);
                trafficLightNodeMap.put(light, node);
                lightLayer.getChildren().add(node);
            } else {
                trafficLightRenderer.updateNode(node, light);
            }
        }

        trafficLightNodeMap.entrySet().removeIf(entry -> {
            if (!activeLights.contains(entry.getKey())) {
                lightLayer.getChildren().remove(entry.getValue());
                return true;
            }
            return false;
        });
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

    public void setVehicleRenderer(IVehicleRenderer renderer) {
        this.vehicleRenderer = renderer;
        // Clear existing nodes to force re-render with new renderer
        if (vehicleLayer != null) {
            for (Group node : vehicleNodeMap.values()) {
                vehicleLayer.getChildren().remove(node);
            }
        }
        vehicleNodeMap.clear();
    }
}
