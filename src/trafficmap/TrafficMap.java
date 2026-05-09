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


    public List<TrafficNode> getNodeList() {
        return nodeList;
    }

    public List<Road> getRoadList() {
        return roadList;
    }
}
