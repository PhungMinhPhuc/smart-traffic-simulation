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
import model.node.Edge;
import model.node.Path;

import java.util.ArrayList;

public class TrafficMap {
    private Map<TrafficNode, ArrayList<Edge>> adjacentList;

    public TrafficMap() {
        this.adjacentList = new HashMap<>();
        System.out.println("Map created");
    }

    public void addNode(TrafficNode node) {
        adjacentList.putIfAbsent(node, new ArrayList<>());
    }

    public void addConnection(TrafficNode startNode, TrafficNode endNode) {
        // Check node existence
        if (!adjacentList.containsKey(startNode) || !adjacentList.containsKey(endNode)) {
            System.out.println("One or both nodes do not exist in the map");
            return;
        }
        // Create road and edge
		TrafficVector directionVector = new TrafficVector(startNode.getCenterPoint(), endNode.getCenterPoint());
		TrafficPoint roadStartPoint = directionVector.translatePoint(startNode.getCenterPoint(),Constants.JUNCTION_RADIUS);
		TrafficPoint roadEndPoint = directionVector.translatePoint(endNode.getCenterPoint(),-Constants.JUNCTION_RADIUS);
		Road newRoad = new Road(roadStartPoint, roadEndPoint); //Use the constructor with default lane count and traffic light state
		
		//check colision with existing roads
		for(Road existingRoad : getRoadList()) {
			if(existingRoad.checkConflict(newRoad)) {
				System.out.println("New road conflicts with existing road:" + existingRoad.getId());
				return;
			}
		}
		
        Edge newEdge = new Edge(startNode, endNode, newRoad);

        // Add edge to both nodes's adjacentList
        adjacentList.get(startNode).add(newEdge);
        adjacentList.get(endNode).add(newEdge);
        
        // Add road to both nodes
        startNode.addRoad(newRoad);
        endNode.addRoad(newRoad);
    }
    
    public void removeNode(TrafficNode removeNode) {
        if (!adjacentList.containsKey(removeNode)) {
            throw new IllegalArgumentException("Node does not exist");
        }

        for (ArrayList<Edge> edgeList : adjacentList.values()) {
            for (Iterator<Edge> it = edgeList.iterator(); it.hasNext(); ) { //use iterator to avoid ConcurrentModificationException
                Edge edge = it.next();
                if (edge.getOtherNode(removeNode) != null) {
                    Road road = edge.getRoad();
                    edge.getStartNode().removeRoad(road.getId());
                    edge.getEndNode().removeRoad(road.getId());
                    it.remove(); // safe removal during iteration
                }
            }
        }

        adjacentList.remove(removeNode);
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
		ArrayList<TrafficNode> trafficNodeList = new ArrayList<>();
		for(TrafficNode node : adjacentList.keySet()) {
			trafficNodeList.add(node);
		}
		return trafficNodeList;
	}

	//Get unique set of Road
	public ArrayList<Road> getRoadList() {
		ArrayList<Road> roadList = new ArrayList<>();
		for(ArrayList<Edge> edgeList : adjacentList.values()) {
			for(Edge edge : edgeList) {
				if(!roadList.contains(edge.getRoad())) {
					roadList.add(edge.getRoad());
				}
			}
		}
		return roadList;
	}
	
	public ArrayList<Vehicle> getVehicleList(){
		ArrayList<Vehicle> vehicleList = new ArrayList<>();
		//Get vehicle in Lanes
		for(Road road : getRoadList()) {
			Way rightWay = road.getRightWay();
			Way leftWay = road.getLeftWay();
			for(Lane lane : rightWay.getLaneList()) {
				vehicleList.addAll(lane.getVehicleList());
			}
			for(Lane lane : leftWay.getLaneList()) {
				vehicleList.addAll(lane.getVehicleList());
			}
		}
		//get Vehicle in nodes
		for(TrafficNode node : adjacentList.keySet()) {
			for(Path path : node.getPathList()) {
				vehicleList.addAll(path.getVehicleList());
			}
		}
		return vehicleList;
	}
	
	public TrafficNode getNodeByPoint(TrafficPoint point) {
		for(TrafficNode node : adjacentList.keySet()) {
			if(node.containsPoint(point)) {
				return node;
			}
		}
		return null; //no node found at the point
	}
	

    public Map<TrafficNode, ArrayList<Edge>> getAdjacentList() {
        return adjacentList;
    }
    

}
