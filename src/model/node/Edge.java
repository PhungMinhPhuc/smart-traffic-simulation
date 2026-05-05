package model.node;

import model.road.Road;

public class Edge {
    private TrafficNode startNode;
    private TrafficNode endNode;
    private Road road;

    public Edge(TrafficNode startNode, TrafficNode endNode, Road road) {
        this.startNode = startNode;
        this.endNode = endNode;
        this.road = road;
    }

    public TrafficNode getOtherNode(TrafficNode node) {
        if (node.equals(startNode)) {
            return endNode;
        }
        if (node.equals(endNode)) {
            return startNode;
        }
        return null;
    }
    
    public TrafficNode getStartNode() {
    	return startNode;
    }
    
    public TrafficNode getEndNode() {
    	return endNode;
    }

    public Road getRoad() {
        return road;
    }
}
