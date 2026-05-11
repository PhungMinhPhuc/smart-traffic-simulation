package main.java;

import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import model.map.TrafficMap;
import model.node.*;
import model.utility.TrafficPoint;
import model.vehicle.Vehicle;
import render.TrafficMapRenderer;
import render.VehicleRenderer;

public class MainSceneController {
	//FXML elements
	@FXML VBox editingSideBar;
	@FXML Button addTrafficNodeButton;
	@FXML Button removeTrafficNodeButton;
	@FXML Button addRoadButton;
	@FXML Button removeRoadButton;
	@FXML Label instructionsLabel; //For displaying instructions to the user when they are adding or removing nodes and roads
	@FXML ScrollPane trafficMapWrapper; //Create view port of the map
	@FXML Pane mapLayer; //Pane to draw the static map on, will be placed inside the 
	@FXML Pane vehicleLayer; //Pane to draw dynamic Vehicle
	@FXML Group trafficMapContainer;
	
	//Map and map renderer
	private static TrafficMapRenderer trafficMapRenderer = new TrafficMapRenderer();
	private static VehicleRenderer vehicleRenderer = new VehicleRenderer();
	private static TrafficMap trafficMap = new TrafficMap();
	
	//Normal fields for functionalities
	Boolean addingRoad = false; //flag to indicate if currently in the process of adding a road
	Line previewLine = null; //line to show the road being dragged out when adding a new road by dragging from start node to end node
	TrafficNode startNode = null; //placeHolder for TrafficNode for event handlers when adding or removing roads and nodes
	TrafficNode endNode = null; 
	
	@FXML
	private void initialize() { //auto run when the scene is loaded
		//Create defaultMap
		createDefaultMap();
		
		//initializes base mapLayer and vehicleLayr
		renderTrafficMap();
		renderDefaultVehicles();
		
		//config scrollPane and Panes
		trafficMapWrapper.setPannable(true); //allow panning by dragging the mouse on the scroll pane
		vehicleLayer.setMouseTransparent(true); //set vehicleLayer to not recognize mouse events

		//set initial instruction
		instructionsLabel.setWrapText(true);
		DisplayInstruction("Click the buttons above to add or remove traffic nodes and roads.");
		
		//start animation to move vehicles
		startVehicleAnimation();
	}
	
/**
 * Helper functions for this controller
 */
	//Method to display instructions to the user in the instructions label
	private void DisplayInstruction(String instruction) {
		instructionsLabel.setText(instruction);
	}
	//createDefaultMap		
	private void createDefaultMap() {
		TrafficNode node1 = new Junction(new TrafficPoint(400, 400));
		TrafficNode node2 = new Junction(new TrafficPoint(400, 1000));
		trafficMap.addNode(node1);
		trafficMap.addNode(node2);
		trafficMap.addConnection(node1, node2);
		// trafficMap.addDefaultVehicleToRoad(trafficMap.getRoadList().get(0), true);
	}
	//render mapLayer
	private void renderTrafficMap() {
		mapLayer.getChildren().clear();
		mapLayer.getChildren().add(trafficMapRenderer.render(trafficMap));
		trafficMapWrapper.setContent(trafficMapContainer);
	}
	//render vehicleLayer
	private void renderDefaultVehicles() {
		vehicleLayer.getChildren().clear();
		for(Vehicle veh : trafficMap.getVehicleList()) {
			vehicleLayer.getChildren().add(vehicleRenderer.render(veh));
		}
	}
	//clamp function for auto scrolling when dragging near the edge of the viewport
	private double clamp(double value) {
	    if (value < 0.0) return 0.0;
	    if (value > 1.0) return 1.0;
	    return value;
	}
	
/**
 * Functions for app's functionalities
 */
	public void addNewNode(ActionEvent event) {
	    DisplayInstruction("Click on the map to set the location of the new traffic node");

	    mapLayer.setOnMouseClicked((e) -> {//one shot event handler to get clicked point

	        //Transform Scene coordinates to Local coordinates of that content
	       Point2D localPoint = trafficMapWrapper.getContent().sceneToLocal(e.getSceneX(), e.getSceneY());

	        //Convert to custom TrafficPoint
	        TrafficPoint lastClickedPoint = new TrafficPoint(localPoint.getX(), localPoint.getY());
	        trafficMap.addNode(new Junction(lastClickedPoint));
	        renderTrafficMap();
	        mapLayer.setOnMouseClicked(null); //remove event handler after one use
	    });
	}
	
	public void addNewRoad(ActionEvent event) {
		addingRoad = true;
		trafficMapWrapper.setPannable(false); //disable panning while adding road to avoid conflicts with dragging to add road
		DisplayInstruction("Click on the start node, then drag to the end node to create a new road");
		
		//One shot event handlers to add road by dragging from start node to end node
		mapLayer.setOnMousePressed((e)->{
			if(!addingRoad) return; //not in adding road mode
			
			Point2D clickedPoint = trafficMapWrapper.getContent().sceneToLocal(e.getSceneX(), e.getSceneY());
			TrafficPoint localPoint = new TrafficPoint(clickedPoint.getX(), clickedPoint.getY()); //convert to custom TrafficPoint
			startNode = trafficMap.getNodeByPoint(localPoint);
		
			if(startNode != null) {
				previewLine = new Line(localPoint.getX(), localPoint.getY(), localPoint.getX(), localPoint.getY());
				mapLayer.getChildren().add(previewLine);
			}
		});
		
		mapLayer.setOnMouseDragged((e)->{
			if(!addingRoad) return; //not in adding road mode
			if(previewLine == null) return; //not started dragging from a node
			
			//update preview line end point as mouse drags
			Point2D draggedPoint = trafficMapWrapper.getContent().sceneToLocal(e.getSceneX(), e.getSceneY());
			TrafficPoint localPoint = new TrafficPoint(draggedPoint.getX(), draggedPoint.getY()); //convert to custom TrafficPoint
			
			//Auto scroll when dragging near the edge of the viewport
		    double edgeMargin = 20;      // px from viewport edge
		    double scrollStep = 0.002;    // per drag event, tune this
		    // mouse position in viewport coordinates
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
			
			//update preview line end point
			previewLine.setEndX(localPoint.getX());
			previewLine.setEndY(localPoint.getY());
		});
		
		mapLayer.setOnMouseReleased((e)->{
			Point2D releasedPoint = trafficMapWrapper.getContent().sceneToLocal(e.getSceneX(), e.getSceneY());
			TrafficPoint localPoint = new TrafficPoint(releasedPoint.getX(), releasedPoint.getY()); //convert to custom TrafficPoint
			
			endNode = trafficMap.getNodeByPoint(localPoint);
			if(endNode != null && startNode != null && endNode != startNode) {
				trafficMap.addConnection(startNode, endNode);
				renderTrafficMap();
			}
			//clean up 
	       if (previewLine != null) {
	            mapLayer.getChildren().remove(previewLine);
	        }
			mapLayer.setOnMousePressed(null);
			mapLayer.setOnMouseDragged(null);
			mapLayer.setOnMouseReleased(null);
			startNode = null;
			endNode = null;
			previewLine = null;
			addingRoad = false;
			DisplayInstruction("");
			trafficMapWrapper.setPannable(true); //re-enable panning after adding road
		});
	}
	
	public void removeNode(ActionEvent event) {
		DisplayInstruction("Click on the node you want to remove");
		//Event handler like addNode
		mapLayer.setOnMouseClicked((e)->{
			Point2D clickedPoint = trafficMapWrapper.getContent().sceneToLocal(e.getSceneX(), e.getSceneY());
			TrafficPoint localPoint = new TrafficPoint(clickedPoint.getX(), clickedPoint.getY()); //convert to custom TrafficPoint
			startNode = trafficMap.getNodeByPoint(localPoint);
			if(startNode != null) {
				trafficMap.removeNode(startNode);
				renderTrafficMap();
			}
			mapLayer.setOnMouseClicked(null); //remove event handler after one use
			DisplayInstruction("");
		});
		
		if(startNode != null) {
			trafficMap.removeNode(startNode);
			renderTrafficMap();
		}
	}

/**
 * Functions for app's animation
 */
	//Timer ticks to update vehicle positions and re-render them at their new positions
	// all the long variable is the current time in nanoseconds
	public void startVehicleAnimation() {
		AnimationTimer vehicleTimer = new AnimationTimer() {
			private long lastFrameTimeNano = 0;
			private long lastVehicleAddTimeNano = 0;
			
			@Override
			public void handle(long now) {
				if (lastFrameTimeNano == 0) {
					lastFrameTimeNano = now;
					return;
				}
				//time elapsed since last frame in seconds
				double deltaTime = (now - lastFrameTimeNano) / 1e9; //convert from nanoseconds to seconds
				double deltaTimeSinceLastVehicleAdd = (now - lastVehicleAddTimeNano) / 1e9;
				lastFrameTimeNano = now; //update current time for the next frame

				if(deltaTimeSinceLastVehicleAdd >= 5) { //add a new vehicle every 5 seconds
					trafficMap.addDefaultVehicleToRoad(trafficMap.getRoadList().get(0), true);
					lastVehicleAddTimeNano = now; //update last vehicle add time
				}
				//update vehicle positions based on their speed and the elapsed time
				trafficMap.updateVehicles(deltaTime);
				renderDefaultVehicles(); //re-render vehicles at their new positions
			}
		};
		vehicleTimer.start();
	}
}
