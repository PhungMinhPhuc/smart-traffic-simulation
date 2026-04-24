package map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import node.Node;

public class TrafficMap {
    private Map<Integer,ArrayList<Node>> adjacentList = new HashMap<>();
    

    public void addNode(Node n, ArrayList<Node> adjNodes){
        adjacentList.put(n.getId(),new ArrayList<Node>()); //new Node added to Map

        for(Node node : adjNodes) //Update its adjList
            adjacentList.get(n.getId()).add(node);

        for(Node node : adjNodes){ //Add the new node to it's adjNodes's adjList
            adjacentList.get(node.getId()).add(n);
        }
    }

    public void removeNode(Node n){
        for(Node node : adjacentList.get(n.getId())){ //remove the target node from all node's adjList
            adjacentList.get(node.getId()).remove(n);
        }
        adjacentList.remove(n.getId()); //remove the target node's adjList
    }
}
