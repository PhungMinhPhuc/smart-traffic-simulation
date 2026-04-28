package node;

import road.Road;

public class Edge {
    private Node startNode;
    private Node endNode;
    private Road road;

    public Edge(Node startNode, Node endNode, Road road) {
        this.startNode = startNode;
        this.endNode = endNode;
        this.road = road;
    }

    public Node getOtherNode(Node node) {
        if (node.equals(startNode)) {
            return endNode;
        }
        if (node.equals(endNode)) {
            return startNode;
        }
        return null;
    }

    public Road getRoad() {
        return road;
    }
}
