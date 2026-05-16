package main.java;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import main.java.handler.MapEditorHandler;
import main.java.handler.SimulationHandler;
import main.java.handler.ZoomHandler;
import model.map.*;
import model.node.*;
import model.utility.*;
import render.*;
import config.*;

public class MainSceneController {
	// FXML elements
	@FXML
	Button addTrafficNodeButton;

	@FXML
	Button removeTrafficNodeButton;

	@FXML
	Button addRoadButton;

	@FXML
	Button removeRoadButton;

	@FXML
	Button pauseSimulationButton;

	@FXML
	ToggleButton stopSpawnButton;

	@FXML
	Button resumeSimulationButton;

	@FXML
	Button zoomInButton;

	@FXML
	Button zoomOutButton;

	@FXML
	Button resetZoomButton;

	@FXML
	Label instructionsLabel;

	@FXML
	ScrollPane trafficMapWrapper;

	@FXML
	Pane mapLayer;

	@FXML
	Pane lightLayer;

	@FXML
	Pane vehicleLayer;

	@FXML
	Group trafficMapContainer;

	@FXML
	Spinner<Integer> laneCountSpinner;

	@FXML
	Spinner<Double> vehicleSpawnSpinner;

	@FXML
	ToggleButton rectModeButton;

	@FXML
	ToggleButton imageModeButton;

	@FXML
	ToggleGroup renderModeGroup;

	@FXML
	ToggleGroup lightModeGroup;

	// Map and map renderer
	private final TrafficMapRenderer trafficMapRenderer = new TrafficMapRenderer();
	private final VehicleRenderer rectRenderer = new VehicleRenderer();
	private final ImageVehicleRenderer imageRenderer = new ImageVehicleRenderer();
	private final render.TrafficLightRenderer trafficLightRenderer = new render.TrafficLightRenderer();
	private final TrafficMap trafficMap = new TrafficMap();

	// Handlers
	private final ZoomHandler zoomHandler = new ZoomHandler();
	private final MapEditorHandler mapEditorHandler = new MapEditorHandler();
	private final SimulationHandler simulationHandler = new SimulationHandler();

	@FXML
	private void initialize() {
		// Create defaultMap
		createDefaultMap();

		// initializes base mapLayer and vehicleLayr
		renderTrafficMap();

		// Initialize Handlers
		zoomHandler.initialize(trafficMapWrapper, trafficMapContainer);
		mapEditorHandler.initialize(mapLayer, trafficMapWrapper, trafficMap, trafficMapRenderer,
				laneCountSpinner, instructionsLabel, addTrafficNodeButton,
				addRoadButton, removeTrafficNodeButton, removeRoadButton);
		simulationHandler.initialize(trafficMap, rectRenderer, trafficLightRenderer, vehicleLayer, lightLayer,
				instructionsLabel, vehicleSpawnSpinner, stopSpawnButton);

		// config scrollPane and Panes
		trafficMapWrapper.setPannable(true);
		vehicleLayer.setMouseTransparent(true);
		lightLayer.setMouseTransparent(true);

		// Center the scroll position after layout
		javafx.application.Platform.runLater(() -> {
			trafficMapWrapper.setHvalue(0.5);
			trafficMapWrapper.setVvalue(0.5);
		});

		// set initial instruction
		instructionsLabel.setWrapText(true);
		instructionsLabel.setText("Click the buttons above to add or remove traffic nodes and roads.");

		// Initialize lane count spinner
		laneCountSpinner.setValueFactory(
				new SpinnerValueFactory.IntegerSpinnerValueFactory(Constants.MIN_LANE_COUNT, Constants.MAX_LANE_COUNT,
						Constants.DEFAULT_LANE_COUNT));

		vehicleSpawnSpinner.setValueFactory(
				new SpinnerValueFactory.DoubleSpinnerValueFactory(0.5, 30.0, 3.0, 0.5));

		// Add mouse wheel zoom support
		trafficMapWrapper.setOnScroll(event -> {
			if (event.isControlDown()) {
				double zoomFactor = (event.getDeltaY() > 0) ? 1 + Constants.ZOOM_STEP : 1 / (1 + Constants.ZOOM_STEP);
				zoomHandler.zoomToPoint(zoomFactor, event.getX(), event.getY());
				event.consume();
			}
		});
	}

	private void createDefaultMap() {
		// Define coordinates for a 3x3 grid
		double c1 = 24600, c2 = 25000, c3 = 25400;
		double r1 = 24600, r2 = 25000, r3 = 25400;

		// Create nodes
		TrafficNode n11 = new Junction(new TrafficPoint(c1, r1));
		TrafficNode n12 = new Junction(new TrafficPoint(c2, r1));
		TrafficNode n13 = new Junction(new TrafficPoint(c3, r1));
		TrafficNode n21 = new Junction(new TrafficPoint(c1, r2));
		TrafficNode n22 = new Junction(new TrafficPoint(c2, r2)); // Center
		TrafficNode n23 = new Junction(new TrafficPoint(c3, r2));
		TrafficNode n31 = new Junction(new TrafficPoint(c1, r3));
		TrafficNode n32 = new Junction(new TrafficPoint(c2, r3));
		TrafficNode n33 = new Junction(new TrafficPoint(c3, r3));

		trafficMap.addNode(n11);
		trafficMap.addNode(n12);
		trafficMap.addNode(n13);
		trafficMap.addNode(n21);
		trafficMap.addNode(n22);
		trafficMap.addNode(n23);
		trafficMap.addNode(n31);
		trafficMap.addNode(n32);
		trafficMap.addNode(n33);

		// Horizontal Connections
		trafficMap.addConnection(n11, n12, 2);
		trafficMap.addConnection(n12, n13, 2);
		trafficMap.addConnection(n21, n22, 3); // Main road
		trafficMap.addConnection(n22, n23, 3); // Main road
		trafficMap.addConnection(n31, n32, 2);
		trafficMap.addConnection(n32, n33, 2);

		// Vertical Connections
		trafficMap.addConnection(n11, n21, 2);
		trafficMap.addConnection(n21, n31, 2);
		trafficMap.addConnection(n12, n22, 4); // Main road
		trafficMap.addConnection(n22, n32, 4); // Main road
		trafficMap.addConnection(n13, n23, 1);
		trafficMap.addConnection(n23, n33, 1);
		
		renderTrafficMap();
	}

	@FXML
	public void clearMap(ActionEvent event) {
		trafficMap.clear();
		renderTrafficMap();
		simulationHandler.clearNodes();
		instructionsLabel.setText("Map cleared");
	}

	@FXML
	public void resetToDefaultMap(ActionEvent event) {
		trafficMap.clear();
		simulationHandler.clearNodes();
		createDefaultMap();
		renderTrafficMap();
		instructionsLabel.setText("Default map restored");
	}

	private void renderTrafficMap() {
		mapLayer.getChildren().clear();
		mapLayer.getChildren().add(trafficMapRenderer.render(trafficMap));
		trafficMapWrapper.setContent(trafficMapContainer);
	}

	@FXML
	public void addNewNode(ActionEvent event) {
		mapEditorHandler.addNewNode(event);
	}

	@FXML
	public void addNewRoad(ActionEvent event) {
		mapEditorHandler.addNewRoad(event);
	}

	@FXML
	public void removeNode(ActionEvent event) {
		mapEditorHandler.removeNode(event);
	}

	@FXML
	public void removeRoad(ActionEvent event) {
		mapEditorHandler.removeRoad(event);
	}

	@FXML
	public void pauseSimulation(ActionEvent event) {
		mapEditorHandler.deactivateCurrentMode();
		simulationHandler.pauseSimulation();
	}

	@FXML
	public void resumeSimulation(ActionEvent event) {
		mapEditorHandler.deactivateCurrentMode();
		simulationHandler.resumeSimulation();
	}

	@FXML
	public void zoomIn(ActionEvent event) {
		zoomHandler.zoomIn(event);
	}

	@FXML
	public void zoomOut(ActionEvent event) {
		zoomHandler.zoomOut(event);
	}

	@FXML
	public void resetZoom(ActionEvent event) {
		zoomHandler.resetZoom(event);
	}

	@FXML
	public void toggleSpawn(ActionEvent event) {
		if (stopSpawnButton.isSelected()) {
			stopSpawnButton.setText("Start Spawn");
			instructionsLabel.setText("Vehicle generation stopped");
		} else {
			stopSpawnButton.setText("Stop Spawn");
			instructionsLabel.setText("Vehicle generation resumed");
		}
	}

	@FXML
	public void switchToRectangleMode(ActionEvent event) {
		simulationHandler.setVehicleRenderer(rectRenderer);
		instructionsLabel.setText("Switched to Rectangle Mode");
	}

	@FXML
	public void switchToImageMode(ActionEvent event) {
		simulationHandler.setVehicleRenderer(imageRenderer);
		instructionsLabel.setText("Switched to Image Mode");
	}

	@FXML
	public void switchLightOff(ActionEvent event) {
		trafficMap.setTrafficLightDisplayMode(0);
		instructionsLabel.setText("Traffic Light: No Countdown");
	}

	@FXML
	public void switchLightFull(ActionEvent event) {
		trafficMap.setTrafficLightDisplayMode(1);
		instructionsLabel.setText("Traffic Light: Full Countdown");
	}

	@FXML
	public void switchLightThreshold(ActionEvent event) {
		trafficMap.setTrafficLightDisplayMode(2);
		instructionsLabel.setText("Traffic Light: Countdown < 8s");
	}
}
