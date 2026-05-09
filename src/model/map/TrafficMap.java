package model.map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import config.Constants;
import model.node.TrafficNode;
import model.road.Lane;
import model.road.Road;
import model.road.Way;
import model.utility.TrafficPoint;
import model.utility.TrafficVector;
import model.vehicle.Vehicle;
import model.node.Path;

import java.util.ArrayList;

public class TrafficMap {
    private ArrayList<TrafficNode> nodeList = new ArrayList<>();
    private ArrayList<Road> roadList = new ArrayList<>();

    public void addNode(TrafficNode node) {
        nodeList.add(node);
    }

    public void addConnection(TrafficNode startNode, TrafficNode endNode) {
        // Check node existence
        if (!nodeList.contains(startNode) || !nodeList.contains(endNode)) {
            System.out.println("One or both nodes do not exist in the map");
            return;
        }
      
		Road newRoad = new Road(startNode, endNode); //Use the constructor with default lane count and traffic light state
		
		//check colision with existing roads
		for(Road existingRoad : getRoadList()) {
			if(existingRoad.checkConflict(newRoad)) {
				System.out.println("New road conflicts with existing road:" + existingRoad.getId());
				return;
			}
		}
		
		//add new road to map's roadList
		roadList.add(newRoad);
		
		//add new road to the start and end node's road list
		newRoad.getStartNode().addRoad(newRoad);
		newRoad.getEndNode().addRoad(newRoad);
    }
    
    public void removeNode(TrafficNode removeNode) {
        if (removeNode == null || !nodeList.contains(removeNode)) {
            return;
        }

        //create snapshot(clone) of the connected roads to avoid concurrent modification exception when removing roads from the node's road list
        ArrayList<Road> connectedRoads = new ArrayList<>(removeNode.getRoadList());
        for (Road road : connectedRoads) {
            // remove the roads connected to the node from the map's road list
            roadList.remove(road);

            // remove the roads connected to the removeNode from all connected nodes' road list
            road.getStartNode().removeRoad(road);
            road.getEndNode().removeRoad(road);
        }

        // remove the node from the map's node list
        nodeList.remove(removeNode);
    }
    
    //Testing method 
    public void addDefaultVehicleToRoad(Road road, boolean isRightWay) {
		Way way = isRightWay ? road.getRightWay() : road.getLeftWay();
		if(way.getLaneList().isEmpty()) {
			System.out.println("No lane available on the way to add vehicle");
			return;
		}
		//Add a default vehicle to the first lane of the way
		way.getLaneList().get(0).addVehicle();
	}
    
    
    //update the position of all vehicles in the map, 
    // this method will be called in each time step of the simulation
    public void updateVehicles(double timeInterval) {
		for(Vehicle vehicle : getVehicleList()) {
			vehicle.move(timeInterval);
		}
	}
    
    //Get unique set of TraffiNode 
	public ArrayList<TrafficNode> getTrafficNodeList() {
		return nodeList;
	}

	//Get unique set of Road
	public ArrayList<Road> getRoadList() {
		return roadList;
	}
	
	public ArrayList<Vehicle> getVehicleList(){
		ArrayList<Vehicle> vehicleList = new ArrayList<>();
		//get all vehicles from all lanes of all roads
		for(Road road : roadList) {
			for(Lane lane : road.getRightWay().getLaneList()) {
				vehicleList.addAll(lane.getVehicleList());
			}
			for(Lane lane : road.getLeftWay().getLaneList()) {
				vehicleList.addAll(lane.getVehicleList());
			}
		}
		
		//get vehicle from all paths of all nodes
		for(TrafficNode node : nodeList) {
			for(Path path : node.getPathList()) {
				vehicleList.addAll(path.getVehicleList());
			}
		}
		
		return vehicleList;
	}
	
	public TrafficNode getNodeByPoint(TrafficPoint point) {
		for(TrafficNode node : nodeList) {
			if(node.containsPoint(point)) {
				return node;
			}
		}
		return null; //no node found at the point
	}
}