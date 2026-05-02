package application.scenes;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import items.map.TrafficMap;
import items.node.*;
import items.road.Lane;
import items.road.Road;
import items.utility.Point2D;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import render.TrafficMapRenderer;

public class MainSenceController {
	
	//Map and map renderer
	private static TrafficMapRenderer trafficMapRenderer = new TrafficMapRenderer();
	private static TrafficMap trafficMap = new TrafficMap();

	//FXML elements
	@FXML VBox editingSideBar;
	@FXML Button addTrafficNodeButton;
	@FXML Button removeTrafficNodeButton;
	@FXML Button addRoadButton;
	@FXML Button removeRoadButton;
	@FXML Label instructionsLabel; //For displaying instrictions to the user when they are adding or removing nodes and roads
	@FXML ScrollPane trafficMapContainer;

	
	@FXML
	private void initialize() {
		
		//Create some initial traffic nodes and roads for testing
//		TrafficNode node1 = new TJunction(new Point2D(400, 400));
//		TrafficNode node2 = new CrossJunction(new Point2D(800, 1000));
//		TrafficNode node3 = new FiveWayJunction(new Point2D(2000, 1600));
//		trafficMap.addNode(node1);
//		trafficMap.addNode(node2);
//		trafficMap.addNode(node3);
//		trafficMap.addConnection(node1, node2);
//		trafficMap.addConnection(node2, node3);
//		trafficMap.addConnection(node3, node1);
		trafficMapContainer.setContent(trafficMapRenderer.render(trafficMap));
		
		//set initial instruction
		DisplayInstruction("Click the buttons above to add or remove traffic nodes and roads.");
		
		//print testing info to console
//		for(Road roads : trafficMap.getRoadList()) {
//			System.out.println("Road from (" + roads.getStartPoint().getX() + ", " + roads.getStartPoint().getY() + ") to (" +
//					roads.getEndPoint().getX() + ", " + roads.getEndPoint().getY() + ")");
//			System.out.println("Road id:" + roads.getId());
//			System.out.println("Left lane id:" + roads.getLeftWay().getRoadId());
//			System.out.println("Right lane id:" + roads.getRightWay().getRoadId());
//		}
	}
	
	//Helper method to display instructions to the user in the instructions label
	private void DisplayInstruction(String instruction) {
		instructionsLabel.setText(instruction);
	}
	
	
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
	    	//Get the actual content (the Canvas or Group) inside the ScrollPane
	        javafx.scene.Node content = trafficMapContainer.getContent();

	        //Transform Scene coordinates to Local coordinates of that content
	        javafx.geometry.Point2D localPoint = content.sceneToLocal(e.getSceneX(), e.getSceneY());

	        //Convert to custom Point2D
	        Point2D lastClickedPoint = new Point2D(localPoint.getX(), localPoint.getY());

	        TrafficNode node;
	        switch (result.get()) {
	            case "T Junction":
	                node = new TJunction(lastClickedPoint);
	                System.out.println("T Junction added at: " + lastClickedPoint.getX() + ", " + lastClickedPoint.getY());
	                break;
	            case "Cross Junction":
	                node = new CrossJunction(lastClickedPoint);
	                System.out.println("Cross Junction added at: " + lastClickedPoint.getX() + ", " + lastClickedPoint.getY());
	                break;
	            case "Five-Way Junction":
	                node = new FiveWayJunction(lastClickedPoint);
	                System.out.println("Five-Way Junction added at: " + lastClickedPoint);
	                break;
	            default:
	                return;
	        }

	        trafficMap.addNode(node);
	        trafficMapContainer.setContent(trafficMapRenderer.render(trafficMap));
	        trafficMapContainer.setOnMouseClicked(null); //remove event handler after one use
	    });
	}

	
	public void addNewRoad(ActionEvent event) {
		
		//Dialogs to select start and end nodes for the new road
		int startNodeId;
		int endNodeId;
		ChoiceDialog<Integer> startNodeDialog = new ChoiceDialog<>(-1, trafficMap.getTrafficNodeList().stream().map(TrafficNode::getId).toList());
		ChoiceDialog<Integer> endNodeDialog = new ChoiceDialog<>(-1, trafficMap.getTrafficNodeList().stream().map(TrafficNode::getId).toList());
		startNodeDialog.setTitle("Select Start Node");
		startNodeDialog.setHeaderText("Choose the start node for the new road");
		startNodeId = startNodeDialog.showAndWait().orElse(-1);
		endNodeDialog.setTitle("Select End Node");
		endNodeDialog.setHeaderText("Choose the end node for the new road");
		endNodeId = endNodeDialog.showAndWait().orElse(-1);
		
		if(startNodeId == -1 || endNodeId == -1) {
			return; //user cancelled
		}
		
		TrafficNode startNode = trafficMap.getTrafficNodeList().stream().filter(node -> node.getId() == startNodeId).findFirst().orElse(null);
		TrafficNode endNode = trafficMap.getTrafficNodeList().stream().filter(node -> node.getId() == endNodeId).findFirst().orElse(null);
		if(startNode == null || endNode == null) {
			DisplayInstruction("Invalid node selection. Please try again.");
			return;
		}
		else {
			trafficMap.addConnection(startNode, endNode);
			trafficMapContainer.setContent(trafficMapRenderer.render(trafficMap));
//			System.out.println("Road added between node " + startNodeId + " and node " + endNodeId);
		}
	}
	
	public void removeNode(ActionEvent event) {
		int removeNodeId;
		ChoiceDialog<Integer> removeNodeDialog = new ChoiceDialog<>(-1, trafficMap.getTrafficNodeList().stream().map(TrafficNode::getId).toList());
		removeNodeDialog.setTitle("Select Node to Remove");
		removeNodeDialog.setHeaderText("Choose the traffic node to remove");
		removeNodeId = removeNodeDialog.showAndWait().orElse(-1);
		if(removeNodeId == -1) {
			return; //user cancelled
		}
		trafficMap.removeNode(removeNodeId);
		trafficMapContainer.setContent(trafficMapRenderer.render(trafficMap));
	}
}
