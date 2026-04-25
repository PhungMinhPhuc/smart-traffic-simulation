package trafficmap;

import node.Node;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrafficMap {
    private Map<Node, List<Node>> adjacentList;
    public TrafficMap() {
        this.adjacentList = new HashMap<>();
        System.out.println("Map created");
    }

    public void addNode(Node node, List<Node> listNode) {
        adjacentList.putIfAbsent(node, listNode);
    }

    public void addEdge(Node startNode, Node endNode) {

        // Check node existence
        if (!adjacentList.containsKey(startNode) || !adjacentList.containsKey(endNode)) {
            throw new IllegalArgumentException("node.Node does not exist");
        }

        // add node to the other's adjacent list
        if (!adjacentList.get(startNode).contains(endNode) && !adjacentList.get(endNode).contains(startNode)) {
            adjacentList.get(startNode).add(endNode);
            adjacentList.get(endNode).add(startNode);
            startNode.addConnection(endNode);
            endNode.addConnection(startNode);
        }
    }

    public Map<Node, List<Node>> getAdjacentList() {
        if (adjacentList.isEmpty()) {
            System.out.println("Map is empty");
        }
        return adjacentList;
    }
}
