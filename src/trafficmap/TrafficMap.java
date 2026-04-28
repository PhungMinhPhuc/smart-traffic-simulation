package trafficmap;

import node.Edge;
import node.Node;
import road.Road;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrafficMap {
    private Map<Node, List<Edge>> adjacentList;

    public TrafficMap() {
        this.adjacentList = new HashMap<>();
        System.out.println("Map created");
    }

    public void addNode(Node node) {
        adjacentList.putIfAbsent(node, new ArrayList<>());
    }

    public void addEdge(Node startNode, Node endNode) {

        // Check node existence
        if (!adjacentList.containsKey(startNode) || !adjacentList.containsKey(endNode)) {
            throw new IllegalArgumentException("Node does not exist");
        }
        // Create road and edge
        Road road = new Road(startNode.getCenterPoint(), endNode.getCenterPoint());
        Edge edge = new Edge(startNode, endNode, road);

        // Add ways from road to both nodes
        startNode.addWay(road.getReverseWay(), road.getFowardWay());
        endNode.addWay(road.getFowardWay(), road.getReverseWay());

        // Add edge to both nodes
        adjacentList.get(startNode).add(edge);
        adjacentList.get(endNode).add(edge);
    }

    public Map<Node, List<Edge>> getAdjacentList() {
        return adjacentList;
    }
}
