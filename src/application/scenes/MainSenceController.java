package application.scenes;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import model.map.TrafficMap;
import model.node.*;
import model.utility.TrafficPoint;
import render.TrafficMapRenderer;

public class MainSenceController {
	//FXML elements
	@FXML VBox editingSideBar;
	@FXML Button addTrafficNodeButton;
	@FXML Button removeTrafficNodeButton;
	@FXML Button addRoadButton;
	@FXML Button removeRoadButton;
	@FXML Label instructionsLabel; //For displaying instrictions to the user when they are adding or removing nodes and roads
	@FXML ScrollPane trafficMapWrapper; //Create view port of the map
	@FXML Pane trafficMapContainer; //Pane to draw the actual map on, will be placed inside the ScrollPane
	
	//Map and map renderer
	private static TrafficMapRenderer trafficMapRenderer = new TrafficMapRenderer();
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
		
		//initializes base map
		renderTrafficMap();
		
		//set initial instruction
		instructionsLabel.setWrapText(true);
		DisplayInstruction("Click the buttons above to add or remove traffic nodes and roads.");
		
		//
		trafficMapWrapper.setPannable(true);
		
		//print testing info to console
//		for(Road roads : trafficMap.getRoadList()) {
//			System.out.println("Road from (" + roads.getStartPoint().getX() + ", " + roads.getStartPoint().getY() + ") to (" +
//					roads.getEndPoint().getX() + ", " + roads.getEndPoint().getY() + ")");
//			System.out.println("Road id:" + roads.getId());
//			System.out.println("Left lane id:" + roads.getLeftWay().getRoadId());
//			System.out.println("Right lane id:" + roads.getRightWay().getRoadId());
//		}
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
		TrafficNode node1 = new Junction(new TrafficPoint(400, 400),5);
		TrafficNode node2 = new Junction(new TrafficPoint(800, 1000),3);
		TrafficNode node3 = new Junction(new TrafficPoint(2000, 1600),4);
		trafficMap.addNode(node1);
		trafficMap.addNode(node2);
		trafficMap.addNode(node3);
		trafficMap.addConnection(node1, node2);
		trafficMap.addConnection(node2, node3);
		trafficMap.addConnection(node3, node1);
	}
	//render map
	private void renderTrafficMap() {
		trafficMapContainer.getChildren().clear();
		trafficMapContainer.getChildren().add(trafficMapRenderer.render(trafficMap));
		trafficMapWrapper.setContent(trafficMapContainer);
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
		//Display choose dialog to select the type of traffic node to add
	    List<String> choices = Arrays.asList("T Junction", "Cross Junction", "Five-Way Junction");
	    ChoiceDialog<String> dialog = new ChoiceDialog<>(choices.get(0), choices);
	    dialog.setTitle("Select Node Type");
	    dialog.setHeaderText("Choose the type of traffic node to add");

	    Optional<String> result = dialog.showAndWait();
	    if (result.isEmpty()) {
	        return; // user cancelled
	    }

	    DisplayInstruction("Click on the map to set the location of the new traffic node");

	    trafficMapContainer.setOnMouseClicked((e) -> {//one shot event handler to get clicked point

	        //Transform Scene coordinates to Local coordinates of that content
	       Point2D localPoint = trafficMapWrapper.getContent().sceneToLocal(e.getSceneX(), e.getSceneY());

	        //Convert to custom TrafficPoint
	        TrafficPoint lastClickedPoint = new TrafficPoint(localPoint.getX(), localPoint.getY());

	        TrafficNode node;
	        switch (result.get()) {
	            case "T Junction":
	                node = new Junction(lastClickedPoint,3);
//	                System.out.println("T Junction added at: " + lastClickedPoint.getX() + ", " + lastClickedPoint.getY());
	                break;
	            case "Cross Junction":
	                node = new Junction(lastClickedPoint,4);
//	                System.out.println("Cross Junction added at: " + lastClickedPoint.getX() + ", " + lastClickedPoint.getY());
	                break;
	            case "Five-Way Junction":
	                node = new Junction(lastClickedPoint,5);
//	                System.out.println("Five-Way Junction added at: " + lastClickedPoint);
	                break;
	            default:
	                return;
	        }
	        trafficMap.addNode(node);
	        renderTrafficMap();
	        trafficMapContainer.setOnMouseClicked(null); //remove event handler after one use
	    });
	}
	
	public void addNewRoad(ActionEvent event) {
		addingRoad = true;
		trafficMapWrapper.setPannable(false); //disable panning while adding road to avoid conflicts with dragging to add road
		DisplayInstruction("Click on the start node, then drag to the end node to create a new road");
		
		//One shot event handlers to add road by dragging from start node to end node
		trafficMapContainer.setOnMousePressed((e)->{
			if(!addingRoad) return; //not in adding road mode
			
			Point2D clickedPoint = trafficMapWrapper.getContent().sceneToLocal(e.getSceneX(), e.getSceneY());
			TrafficPoint localPoint = new TrafficPoint(clickedPoint.getX(), clickedPoint.getY()); //convert to custom TrafficPoint
			startNode = trafficMap.getNodeByPoint(localPoint);
		
			if(startNode != null) {
				previewLine = new Line(localPoint.getX(), localPoint.getY(), localPoint.getX(), localPoint.getY());
				trafficMapContainer.getChildren().add(previewLine);
			}
		});
		
		trafficMapContainer.setOnMouseDragged((e)->{
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
		
		trafficMapContainer.setOnMouseReleased((e)->{
			Point2D releasedPoint = trafficMapWrapper.getContent().sceneToLocal(e.getSceneX(), e.getSceneY());
			TrafficPoint localPoint = new TrafficPoint(releasedPoint.getX(), releasedPoint.getY()); //convert to custom TrafficPoint
			
			endNode = trafficMap.getNodeByPoint(localPoint);
			if(endNode != null && startNode != null && endNode != startNode) {
				trafficMap.addConnection(startNode, endNode);
				renderTrafficMap();
			}
			//clean up 
	       if (previewLine != null) {
	            trafficMapContainer.getChildren().remove(previewLine);
	        }
			trafficMapContainer.setOnMousePressed(null);
			trafficMapContainer.setOnMouseDragged(null);
			trafficMapContainer.setOnMouseReleased(null);
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
		trafficMapContainer.setOnMouseClicked((e)->{
			Point2D clickedPoint = trafficMapWrapper.getContent().sceneToLocal(e.getSceneX(), e.getSceneY());
			TrafficPoint localPoint = new TrafficPoint(clickedPoint.getX(), clickedPoint.getY()); //convert to custom TrafficPoint
			startNode = trafficMap.getNodeByPoint(localPoint);
			if(startNode != null) {
				trafficMap.removeNode(startNode);
				renderTrafficMap();
			}
			trafficMapContainer.setOnMouseClicked(null); //remove event handler after one use
			DisplayInstruction("");
		});
		
		if(startNode != null) {
			trafficMap.removeNode(startNode);
			renderTrafficMap();
		}
	}
	
}
