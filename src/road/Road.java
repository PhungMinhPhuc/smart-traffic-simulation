package road;

import node.Node;

public class Road {
    private String id;
    private Way rightWay;
    private Way leftWay;
    private Node startNode;
    private Node endNode;

    public Road(Node startNode, Node endNode) {
        this.rightWay = new Way();
        this.leftWay = new Way();
        this.startNode = startNode;
        this.endNode = endNode;
    }

    public Way getRightWay() {
        return rightWay;
    }

    public Way getLeftWay() {
        return leftWay;
    }

    public Node getStartNode() {
        return startNode;
    }

    public Node getEndNode() {
        return endNode;
    }

    public void setStartNode(Node startNode) {
        this.startNode = startNode;
    }

    public void setEndNode(Node endNode) {
        this.endNode = endNode;
    }
}
