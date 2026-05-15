package main.java.handler;

import config.Constants;
import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Pane;
import model.map.TrafficMap;
import model.road.Road;
import model.vehicle.Vehicle;
import model.traffic.TrafficLight;
import render.IVehicleRenderer;

import java.util.*;

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
    private Spinner<Double> vehicleSpawnSpinner;
    private ToggleButton stopSpawnButton;
    private final Map<Vehicle, Group> vehicleNodeMap = new HashMap<>();
    private final Map<TrafficLight, Group> trafficLightNodeMap = new HashMap<>();

    public void initialize(TrafficMap trafficMap, IVehicleRenderer vehicleRenderer,
            render.TrafficLightRenderer trafficLightRenderer, Pane vehicleLayer, Pane lightLayer,
            Label instructionsLabel, Spinner<Double> vehicleSpawnSpinner, ToggleButton stopSpawnButton) {
        this.trafficMap = trafficMap;
        this.vehicleRenderer = vehicleRenderer;
        this.trafficLightRenderer = trafficLightRenderer;
        this.vehicleLayer = vehicleLayer;
        this.lightLayer = lightLayer;
        this.instructionsLabel = instructionsLabel;
        this.vehicleSpawnSpinner = vehicleSpawnSpinner;
        this.stopSpawnButton = stopSpawnButton;

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

                double spawnInterval = (vehicleSpawnSpinner != null) ? vehicleSpawnSpinner.getValue() : 3.0;
                boolean isSpawnStopped = (stopSpawnButton != null && stopSpawnButton.isSelected());

                if (deltaTimeSinceLastVehicleAdd >= spawnInterval && !isSpawnStopped) {
                    if (!trafficMap.getRoadList().isEmpty()) {
                        // Spawn on a random road
                        List<Road> roads = trafficMap.getRoadList();
                        Road randomRoad = roads.get(new Random().nextInt(roads.size()));
                        trafficMap.addDefaultVehicleToRoad(randomRoad, true);
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
        clearNodes();
    }

    public void clearNodes() {
        if (vehicleLayer != null) {
            vehicleLayer.getChildren().clear();
        }
        if (lightLayer != null) {
            lightLayer.getChildren().clear();
        }
        vehicleNodeMap.clear();
        trafficLightNodeMap.clear();
    }
}
