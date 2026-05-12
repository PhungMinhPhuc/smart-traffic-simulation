package main.java;

import config.Constants;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import model.map.*;
import model.node.*;
import model.utility.*;
import model.vehicle.*;
import render.*;

public class MainSceneController {
	// FXML elements
	@FXML
	Button addTrafficNodeButton;
	@FXML
	Button removeTrafficNodeButton;
	@FXML
	Button addRoadButton;
	@FXML
	Label instructionsLabel; // For displaying instructions to the user when they are adding or removing
								// nodes and roads
	@FXML
	ScrollPane trafficMapWrapper; // Create view port of the map
	@FXML
	Pane mapLayer; // Pane to draw the static map on, will be placed inside the
	@FXML
	Pane vehicleLayer; // Pane to draw dynamic Vehicle
	@FXML
	Group trafficMapContainer;
	@FXML
	Spinner<Integer> laneCountSpinner;

	// Map and map renderer
	private static TrafficMapRenderer trafficMapRenderer = new TrafficMapRenderer();
	private static VehicleRenderer vehicleRenderer = new VehicleRenderer();
	private static TrafficMap trafficMap = new TrafficMap();

	// Mode enum for toggling between different editing modes
	private enum EditMode {
		NONE, ADD_NODE, ADD_ROAD, REMOVE_NODE
	}

	private EditMode currentMode = EditMode.NONE;

	// Normal fields for functionalities
	Line previewLine = null;
	TrafficNode startNode = null;
	TrafficNode endNode = null;

	// Fields for pause/resume simulation
	private AnimationTimer vehicleTimer;
	private long lastFrameTimeNano = 0;
	private boolean simulationPaused = false;

	@FXML
	private void initialize() { // auto run when the scene is loaded
		// Create defaultMap
		createDefaultMap();

		// initializes base mapLayer and vehicleLayr
		renderTrafficMap();
		renderDefaultVehicles();

		// config scrollPane and Panes
		trafficMapWrapper.setPannable(true); // allow panning by dragging the mouse on the scroll pane
		vehicleLayer.setMouseTransparent(true); // set vehicleLayer to not recognize mouse events

		// Center the scroll position after layout
		javafx.application.Platform.runLater(() -> {
			trafficMapWrapper.setHvalue(0.5);
			trafficMapWrapper.setVvalue(0.5);
		});

		// set initial instruction
		instructionsLabel.setWrapText(true);
		DisplayInstruction("Click the buttons above to add or remove traffic nodes and roads.");

		// Initialize lane count spinner (min 1, max 10, default 3)
		laneCountSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 3));

		// start animation to move vehicles
		startVehicleAnimation();

		// Add mouse wheel zoom support (Zoom to cursor)
		trafficMapWrapper.setOnScroll(event -> {
			if (event.isControlDown()) {
				double zoomFactor = (event.getDeltaY() > 0) ? 1.1 : 1 / 1.1;
				zoomToPoint(zoomFactor, event.getX(), event.getY());
				event.consume();
			}
		});
	}

	private void zoomToPoint(double factor, double mouseX, double mouseY) {
		double oldZoom = currentZoom;
		double newZoom = Math.max(MIN_ZOOM, Math.min(MAX_ZOOM, currentZoom * factor));

		if (newZoom == oldZoom)
			return;

		// Get mouse position relative to the Group before scaling
		Point2D mouseInContent = trafficMapContainer.sceneToLocal(trafficMapWrapper.localToScene(mouseX, mouseY));

		currentZoom = newZoom;

		// Apply scale to the Group
		trafficMapContainer.setScaleX(currentZoom);
		trafficMapContainer.setScaleY(currentZoom);

		// Force update of ScrollPane layout
		trafficMapWrapper.layout();

		javafx.geometry.Bounds viewportBounds = trafficMapWrapper.getViewportBounds();
		javafx.scene.layout.Region content = (javafx.scene.layout.Region) trafficMapWrapper.getContent();

		double viewportWidth = viewportBounds.getWidth();
		double viewportHeight = viewportBounds.getHeight();
		double contentWidth = content.getWidth();
		double contentHeight = content.getHeight();

		// Get the bounds of the Group relative to the StackPane
		javafx.geometry.Bounds groupBounds = trafficMapContainer.getBoundsInParent();

		// Calculate the point we want to keep fixed in StackPane coordinates
		double targetXInStack = groupBounds.getMinX() + mouseInContent.getX() * currentZoom;
		double targetYInStack = groupBounds.getMinY() + mouseInContent.getY() * currentZoom;

		// Calculate mouse position relative to the viewport's top-left corner
		double mouseXInViewport = mouseX - viewportBounds.getMinX();
		double mouseYInViewport = mouseY - viewportBounds.getMinY();

		if (contentWidth > viewportWidth) {
			double newHValue = (targetXInStack - mouseXInViewport) / (contentWidth - viewportWidth);
			trafficMapWrapper.setHvalue(clamp(newHValue));
		}

		if (contentHeight > viewportHeight) {
			double newVValue = (targetYInStack - mouseYInViewport) / (contentHeight - viewportHeight);
			trafficMapWrapper.setVvalue(clamp(newVValue));
		}
	}

	// Method to display instructions to the user in the instructions label
	private void DisplayInstruction(String instruction) {
		instructionsLabel.setText(instruction);
	}

	// createDefaultMap
	private void createDefaultMap() {
		TrafficNode node1 = new Junction(new TrafficPoint(25000, 24700));
		TrafficNode node2 = new Junction(new TrafficPoint(25000, 25300));
		trafficMap.addNode(node1);
		trafficMap.addNode(node2);
		trafficMap.addConnection(node1, node2, 3);
	}

	// Render mapLayer
	private void renderTrafficMap() {
		mapLayer.getChildren().clear();
		mapLayer.getChildren().add(trafficMapRenderer.render(trafficMap));
		trafficMapWrapper.setContent(trafficMapContainer);
	}

	// Render vehicleLayer
	private void renderDefaultVehicles() {
		vehicleLayer.getChildren().clear();
		for (Vehicle veh : trafficMap.getVehicleList()) {
			vehicleLayer.getChildren().add(vehicleRenderer.render(veh));
		}
	}

	// Clamp function for auto scrolling when dragging near the edge of the viewport
	private double clamp(double value) {
		if (value < 0.0)
			return 0.0;
		if (value > 1.0)
			return 1.0;
		return value;
	}

	// Deactivates the current mode and clears all event handlers.
	private void deactivateCurrentMode() {
		mapLayer.setOnMouseClicked(null);
		mapLayer.setOnMousePressed(null);
		mapLayer.setOnMouseDragged(null);
		mapLayer.setOnMouseReleased(null);
		if (previewLine != null) {
			mapLayer.getChildren().remove(previewLine);
			previewLine = null;
		}
		startNode = null;
		endNode = null;
		trafficMapWrapper.setPannable(true);
		DisplayInstruction("Click the buttons above to add or remove traffic nodes and roads.");

		// Remove active style from all buttons
		addTrafficNodeButton.getStyleClass().remove("active-button");
		addRoadButton.getStyleClass().remove("active-button");
		removeTrafficNodeButton.getStyleClass().remove("active-button");

		currentMode = EditMode.NONE;
	}

	/**
	 * Sets the editing mode. If the requested mode is already active, it
	 * deactivates it (toggle off).
	 * Otherwise, it deactivates the current mode and activates the new one.
	 */
	private void setMode(EditMode mode, Button sourceButton) {
		if (currentMode == mode) {
			// Toggle off
			deactivateCurrentMode();
			return;
		}
		// Deactivate previous mode first
		deactivateCurrentMode();
		currentMode = mode;
		sourceButton.getStyleClass().add("active-button");
	}

	public void addNewNode(ActionEvent event) {
		setMode(EditMode.ADD_NODE, addTrafficNodeButton);
		if (currentMode != EditMode.ADD_NODE)
			return;

		DisplayInstruction("Click on the map to place nodes. Click 'Add Node' again to stop.");

		mapLayer.setOnMouseClicked((e) -> {
			Point2D localPoint = trafficMapWrapper.getContent().sceneToLocal(e.getSceneX(), e.getSceneY());
			TrafficPoint clickedPoint = new TrafficPoint(localPoint.getX(), localPoint.getY());
			trafficMap.addNode(new Junction(clickedPoint));
			renderTrafficMap();
			// Re-attach handler since renderTrafficMap clears children
			// (handler stays on mapLayer, not its children, so it persists)
		});
	}

	public void addNewRoad(ActionEvent event) {
		setMode(EditMode.ADD_ROAD, addRoadButton);
		if (currentMode != EditMode.ADD_ROAD)
			return;

		trafficMapWrapper.setPannable(false);
		DisplayInstruction("Drag from start node to end node. Click 'Add Road' again to stop.");

		mapLayer.setOnMousePressed((e) -> {
			if (currentMode != EditMode.ADD_ROAD)
				return;

			Point2D clickedPoint = trafficMapWrapper.getContent().sceneToLocal(e.getSceneX(), e.getSceneY());
			TrafficPoint localPoint = new TrafficPoint(clickedPoint.getX(), clickedPoint.getY());
			startNode = trafficMap.getNodeByPoint(localPoint);

			if (startNode != null) {
				previewLine = new Line(localPoint.getX(), localPoint.getY(), localPoint.getX(), localPoint.getY());
				mapLayer.getChildren().add(previewLine);
			}
		});

		mapLayer.setOnMouseDragged((e) -> {
			if (currentMode != EditMode.ADD_ROAD)
				return;
			if (previewLine == null)
				return;

			Point2D draggedPoint = trafficMapWrapper.getContent().sceneToLocal(e.getSceneX(), e.getSceneY());
			TrafficPoint localPoint = new TrafficPoint(draggedPoint.getX(), draggedPoint.getY());

			// Auto scroll when dragging near the edge of the viewport
			double edgeMargin = 20;
			double scrollStep = 0.002;
			Point2D viewportPoint = trafficMapWrapper.sceneToLocal(e.getSceneX(), e.getSceneY());
			double vx = viewportPoint.getX();
			double vy = viewportPoint.getY();
			double vw = trafficMapWrapper.getViewportBounds().getWidth();
			double vh = trafficMapWrapper.getViewportBounds().getHeight();

			if (vx < edgeMargin) {
				trafficMapWrapper.setHvalue(clamp(trafficMapWrapper.getHvalue() - scrollStep));
			} else if (vx > vw - edgeMargin) {
				trafficMapWrapper.setHvalue(clamp(trafficMapWrapper.getHvalue() + scrollStep));
			}
			if (vy < edgeMargin) {
				trafficMapWrapper.setVvalue(clamp(trafficMapWrapper.getVvalue() - scrollStep));
			} else if (vy > vh - edgeMargin) {
				trafficMapWrapper.setVvalue(clamp(trafficMapWrapper.getVvalue() + scrollStep));
			}

			previewLine.setEndX(localPoint.getX());
			previewLine.setEndY(localPoint.getY());
		});

		mapLayer.setOnMouseReleased((e) -> {
			if (currentMode != EditMode.ADD_ROAD)
				return;

			Point2D releasedPoint = trafficMapWrapper.getContent().sceneToLocal(e.getSceneX(), e.getSceneY());
			TrafficPoint localPoint = new TrafficPoint(releasedPoint.getX(), releasedPoint.getY());

			endNode = trafficMap.getNodeByPoint(localPoint);
			if (endNode != null && startNode != null && endNode != startNode) {
				int laneCount = laneCountSpinner.getValue();
				trafficMap.addConnection(startNode, endNode, laneCount);
				renderTrafficMap();
			}
			// Clean up for next drag, but keep mode active
			if (previewLine != null) {
				mapLayer.getChildren().remove(previewLine);
			}
			startNode = null;
			endNode = null;
			previewLine = null;
		});
	}

	public void removeNode(ActionEvent event) {
		setMode(EditMode.REMOVE_NODE, removeTrafficNodeButton);
		if (currentMode != EditMode.REMOVE_NODE)
			return;

		DisplayInstruction("Click on nodes to remove them. Click 'Remove Node' again to stop.");

		mapLayer.setOnMouseClicked((e) -> {
			Point2D clickedPoint = trafficMapWrapper.getContent().sceneToLocal(e.getSceneX(), e.getSceneY());
			TrafficPoint localPoint = new TrafficPoint(clickedPoint.getX(), clickedPoint.getY());
			TrafficNode clickedNode = trafficMap.getNodeByPoint(localPoint);
			if (clickedNode != null) {
				trafficMap.removeNode(clickedNode);
				renderTrafficMap();
			}
		});
	}

	// Timer ticks to update vehicle positions and re-render them at their new
	// positions all the long variable is the current time in nanoseconds
	public void startVehicleAnimation() {
		vehicleTimer = new AnimationTimer() {
			private long lastVehicleAddTimeNano = 0;

			@Override
			public void handle(long now) {
				if (lastFrameTimeNano == 0) {
					lastFrameTimeNano = now;
					return;
				}
				// time elapsed since last frame in seconds
				double deltaTime = (now - lastFrameTimeNano) / Constants.NANOS_PER_SECOND; // convert from nanoseconds
																							// to seconds
				double deltaTimeSinceLastVehicleAdd = (now - lastVehicleAddTimeNano) / Constants.NANOS_PER_SECOND;
				lastFrameTimeNano = now; // update current time for the next frame

				if (deltaTimeSinceLastVehicleAdd >= 3) { // add a new vehicle every 5 seconds
					trafficMap.addDefaultVehicleToRoad(trafficMap.getRoadList().get(0), true);
					lastVehicleAddTimeNano = now; // update last vehicle add time
				}
				// update vehicle positions based on their speed and the elapsed time
				trafficMap.updateVehicles(deltaTime);
				renderDefaultVehicles(); // re-render vehicles at their new positions
			}
		};
		vehicleTimer.start();
	}

	public void pauseSimulation(ActionEvent event) {
		deactivateCurrentMode();
		if (vehicleTimer != null && !simulationPaused) {
			vehicleTimer.stop();
			simulationPaused = true;
			DisplayInstruction("Simulation paused");
		}
	}

	public void resumeSimulation(ActionEvent event) {
		deactivateCurrentMode();
		if (vehicleTimer != null && simulationPaused) {
			simulationPaused = false;
			lastFrameTimeNano = 0;
			vehicleTimer.start();
			DisplayInstruction("");
		}
	}

	private double currentZoom = 1.0;
	private static final double MAX_ZOOM = 5.0;
	private static final double MIN_ZOOM = 0.1;

	@FXML
	public void zoomIn(ActionEvent event) {
		double centerX = trafficMapWrapper.getViewportBounds().getWidth() / 2;
		double centerY = trafficMapWrapper.getViewportBounds().getHeight() / 2;
		zoomToPoint(1.1, centerX, centerY);
	}

	@FXML
	public void zoomOut(ActionEvent event) {
		double centerX = trafficMapWrapper.getViewportBounds().getWidth() / 2;
		double centerY = trafficMapWrapper.getViewportBounds().getHeight() / 2;
		zoomToPoint(1 / 1.1, centerX, centerY);
	}

	@FXML
	public void resetZoom(ActionEvent event) {
		currentZoom = 1.0;
		applyZoom();
	}

	private void applyZoom() {
		trafficMapContainer.setScaleX(currentZoom);
		trafficMapContainer.setScaleY(currentZoom);
	}
}
