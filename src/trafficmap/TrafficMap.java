package trafficmap;

import node.TrafficNode;
import road.Road;

import java.util.ArrayList;
import java.util.List;

public class TrafficMap {
    private List<TrafficNode> nodeList;
    private List<Road> roadList;

    public TrafficMap() {
        this.nodeList = new ArrayList<>();
        this.roadList = new ArrayList<>();
        System.out.println("Map created");
    }

    public void addNode(TrafficNode node) {
        if (!nodeList.contains(node)) {
            nodeList.add(node);
        }
    }

    public void addRoad(TrafficNode startNode, TrafficNode endNode) {
        if (!nodeList.contains(startNode) || !nodeList.contains(endNode)) {
            throw new IllegalArgumentException("Node does not exist");
        }

        Road road = new Road(startNode, endNode);
        roadList.add(road);

        startNode.addRoad(road);
        endNode.addRoad(road);
    }

    public List<TrafficNode> getNodeList() {
        return nodeList;
    }

    public List<Road> getRoadList() {
        return roadList;
    }
}
