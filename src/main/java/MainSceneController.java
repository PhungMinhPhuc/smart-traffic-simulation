package main.java;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
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
	Button pauseSimulationButton;

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
	Pane vehicleLayer;

	@FXML
	Group trafficMapContainer;

	@FXML
	Spinner<Integer> laneCountSpinner;

	// Map and map renderer
	private final TrafficMapRenderer trafficMapRenderer = new TrafficMapRenderer();
	private final VehicleRenderer vehicleRenderer = new VehicleRenderer();
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
				addRoadButton, removeTrafficNodeButton);
		simulationHandler.initialize(trafficMap, vehicleRenderer, trafficLightRenderer, vehicleLayer, instructionsLabel);

		// config scrollPane and Panes
		trafficMapWrapper.setPannable(true);
		vehicleLayer.setMouseTransparent(true);

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
		TrafficNode node1 = new Junction(new TrafficPoint(25000, 24700));
		TrafficNode node2 = new Junction(new TrafficPoint(25000, 25300));
		trafficMap.addNode(node1);
		trafficMap.addNode(node2);
		trafficMap.addConnection(node1, node2, 3);
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
}
