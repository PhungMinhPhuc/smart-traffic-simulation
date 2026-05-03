package model.map;
import java.util.HashMap;
import java.util.Map;

import config.Constants;
import model.node.TrafficNode;
import model.road.Road;
import model.utility.TrafficPoint;
import model.utility.TrafficVector;

import java.util.ArrayList;


public class TrafficMap {
    private Map<String,ArrayList<TrafficNode>> adjacentList = new HashMap<>();
    private ArrayList<Road> roadList = new ArrayList<Road>();
    private ArrayList<TrafficNode> trafficNodeList = new ArrayList<TrafficNode>();
    

    public void addNode(TrafficNode trafficNode){
        trafficNodeList.add(trafficNode);
        if(!adjacentList.containsKey(trafficNode.getId())){
            adjacentList.put(trafficNode.getId(), new ArrayList<TrafficNode>());
        }
        else{
            System.out.println("TrafficNode with id " + trafficNode.getId() + " already exists in the map.");
        }
    }

    public void addConnection(TrafficNode startNode, TrafficNode endNode){
        //check if both nodes exist in the map, if they do, add the connection between them, otherwise print an error message
        if(adjacentList.containsKey(startNode.getId()) && adjacentList.containsKey(endNode.getId())){

            //create new road between the two nodes and add it to the road list
            TrafficVector directionVector = new TrafficVector(startNode.getCenterPoint(), endNode.getCenterPoint());
            TrafficPoint roadStartPoint = directionVector.translatePoint(startNode.getCenterPoint(),Constants.JUNCTION_RADIUS);
            TrafficPoint roadEndPoint = directionVector.translatePoint(endNode.getCenterPoint(),-Constants.JUNCTION_RADIUS);
            Road newRoad = new Road(roadStartPoint, roadEndPoint); //Use the constructor with default lane count and traffic light state
            
            //check collision between the new road and existing roads, if there is a collision, print an error message and do not add the connection
            for(Road road : roadList) {
            	if(newRoad.checkConflict(road)){
					System.out.println("The new road between node " + startNode.getId() + " and node " + endNode.getId() + " collides with an existing road. Connection not added.");
					return;
				}
            }

            
            //add the connection between the two nodes, which is to add each node to the adjacent list of the other node
            adjacentList.get(startNode.getId()).add(endNode);
            adjacentList.get(endNode.getId()).add(startNode);
            
            //add road to the map's roadLiss
            roadList.add(newRoad);
            
            //add road to the nodes's road lists
            startNode.addRoad(newRoad);
            endNode.addRoad(newRoad);
        }
        else{
            System.out.println("One or both nodes do not exist in the map.");
        }
    }

    public void removeNode(String nodeId){
        if(adjacentList.containsKey(nodeId)){
            TrafficNode nodeRemove = null;
            for(TrafficNode trafficNode : trafficNodeList){ //find the node with the given id in the node list
                if(trafficNode.getId().equals(nodeId)){
                    nodeRemove = trafficNode;
                    trafficNodeList.remove(trafficNode);
                    break;
                }
            }
            if(nodeRemove != null){
                ArrayList<Road> roadsToRemove = nodeRemove.getRoadList(); //Roads connected to the node that need to be removed from the road list
                for(TrafficNode adjacentNode : adjacentList.get(nodeId)){
                    adjacentList.get(adjacentNode.getId()).remove(nodeRemove); //remove the node from the adjacent list of its adjacent nodes
                    for(Road road : roadsToRemove){
                        adjacentNode.removeRoad(road.getId()); //remove the road from the adjacent nodes
                        roadList.remove(road); //remove the road from the road list
                    }
                }
            }
            //remove the node from the adjacent list
            adjacentList.remove(nodeRemove.getId());
        }
        else{
            System.out.println("TrafficNode with id " + nodeId + " does not exist in the map.");
        }
    }

    public ArrayList<TrafficNode> getTrafficNodeList() {
        return trafficNodeList;
    }

    public ArrayList<Road> getRoadList() {
        return roadList;
    }

}
