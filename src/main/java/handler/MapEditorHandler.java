package main.java.handler;

import javafx.event.ActionEvent;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import model.map.TrafficMap;
import model.node.Junction;
import model.node.TrafficNode;
import model.utility.TrafficPoint;
import render.TrafficMapRenderer;

public class MapEditorHandler {

    private enum EditMode {
        NONE, ADD_NODE, ADD_ROAD, REMOVE_NODE
    }

    private EditMode currentMode = EditMode.NONE;

    // References to UI and Data (to be initialized by Controller)
    private Pane mapLayer;
    private ScrollPane trafficMapWrapper;
    private TrafficMap trafficMap;
    private TrafficMapRenderer trafficMapRenderer;
    private Spinner<Integer> laneCountSpinner;
    private Label instructionsLabel;

    // Buttons for styling
    private Button addTrafficNodeButton;
    private Button addRoadButton;
    private Button removeTrafficNodeButton;

    // Internal state for road creation
    private Line previewLine = null;
    private TrafficNode startNode = null;
    private TrafficNode endNode = null;

    public void initialize(Pane mapLayer, ScrollPane trafficMapWrapper, TrafficMap trafficMap,
            TrafficMapRenderer trafficMapRenderer, Spinner<Integer> laneCountSpinner,
            Label instructionsLabel, Button addNodeBtn, Button addRoadBtn, Button removeNodeBtn) {
        this.mapLayer = mapLayer;
        this.trafficMapWrapper = trafficMapWrapper;
        this.trafficMap = trafficMap;
        this.trafficMapRenderer = trafficMapRenderer;
        this.laneCountSpinner = laneCountSpinner;
        this.instructionsLabel = instructionsLabel;
        this.addTrafficNodeButton = addNodeBtn;
        this.addRoadButton = addRoadBtn;
        this.removeTrafficNodeButton = removeNodeBtn;
    }

    private void displayInstruction(String instruction) {
        instructionsLabel.setText(instruction);
    }

    public void deactivateCurrentMode() {
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
        displayInstruction("Click the buttons above to add or remove traffic nodes and roads.");

        // Remove active style from all buttons
        if (addTrafficNodeButton != null)
            addTrafficNodeButton.getStyleClass().remove("active-button");
        if (addRoadButton != null)
            addRoadButton.getStyleClass().remove("active-button");
        if (removeTrafficNodeButton != null)
            removeTrafficNodeButton.getStyleClass().remove("active-button");

        currentMode = EditMode.NONE;
    }

    private void setMode(EditMode mode, Button sourceButton) {
        if (currentMode == mode) {
            deactivateCurrentMode();
            return;
        }
        deactivateCurrentMode();
        currentMode = mode;
        if (sourceButton != null)
            sourceButton.getStyleClass().add("active-button");
    }

    public void addNewNode(ActionEvent event) {
        setMode(EditMode.ADD_NODE, addTrafficNodeButton);
        if (currentMode != EditMode.ADD_NODE)
            return;

        displayInstruction("Click on the map to place nodes. Click 'Add Node' again to stop.");

        mapLayer.setOnMouseClicked((e) -> {
            Point2D localPoint = trafficMapWrapper.getContent().sceneToLocal(e.getSceneX(), e.getSceneY());
            TrafficPoint clickedPoint = new TrafficPoint(localPoint.getX(), localPoint.getY());
            trafficMap.addNode(new Junction(clickedPoint));
            renderTrafficMap();
        });
    }

    public void addNewRoad(ActionEvent event) {
        setMode(EditMode.ADD_ROAD, addRoadButton);
        if (currentMode != EditMode.ADD_ROAD)
            return;

        trafficMapWrapper.setPannable(false);
        displayInstruction("Drag from start node to end node. Click 'Add Road' again to stop.");

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
            if (currentMode != EditMode.ADD_ROAD || previewLine == null)
                return;

            Point2D draggedPoint = trafficMapWrapper.getContent().sceneToLocal(e.getSceneX(), e.getSceneY());
            TrafficPoint localPoint = new TrafficPoint(draggedPoint.getX(), draggedPoint.getY());

            // Auto scroll logic
            handleAutoScroll(e.getSceneX(), e.getSceneY());

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

        displayInstruction("Click on nodes to remove them. Click 'Remove Node' again to stop.");

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

    private void renderTrafficMap() {
        mapLayer.getChildren().clear();
        mapLayer.getChildren().add(trafficMapRenderer.render(trafficMap));
    }

    private void handleAutoScroll(double sceneX, double sceneY) {
        double edgeMargin = 20;
        double scrollStep = 0.002;
        Point2D viewportPoint = trafficMapWrapper.sceneToLocal(sceneX, sceneY);
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
    }

    private double clamp(double value) {
        return Math.max(0.0, Math.min(1.0, value));
    }
}
