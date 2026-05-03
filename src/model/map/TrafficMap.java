package model.map;

import model.node.TrafficNode;
import model.road.Road;
import model.node.Junction;
import model.vehicle.Vehicle;
import java.util.*; 

public class TrafficMap {
    private Map<TrafficNode, List<Road>> adjacencyList;
    private List<Vehicle> vehicles;
    private List<Road> allRoads; 

    public TrafficMap() {
        this.adjacencyList = new HashMap<>();
        this.vehicles = new ArrayList<>();
        this.allRoads = new ArrayList<>();
    }

    // If the node isn't in the list, add it with an empty list of roads
    public void addNode(TrafficNode node) {
        adjacencyList.putIfAbsent(node, new ArrayList<>());
    }

    public void addRoad(Road road) {
        TrafficNode start = road.getStartNode();
        TrafficNode end = road.getEndNode();
        addNode(start);
        addNode(end);
        adjacencyList.get(start).add(road);
        adjacencyList.get(end).add(road);
        
        // Register the road inside the nodes
        start.addRoad(road);
        end.addRoad(road);
        
        // Initialize the junctions so traffic lights are created and cycles begin
        if (start instanceof Junction) {
            ((Junction) start).initializeJunction();
        }
        if (end instanceof Junction) {
            ((Junction) end).initializeJunction();
        }
        
        if (!allRoads.contains(road)) {
            allRoads.add(road);
        }
    }

    // Find all roads connected to a specific node
    public List<Road> getConnectedRoads(TrafficNode node) {
        return adjacencyList.getOrDefault(node, new ArrayList<>());
    }

    public void addVehicle(Vehicle vehicle) {
        vehicles.add(vehicle);
    }

    public void update(double deltaTime) {
        // Update Junctions (Traffic Lights)
        for (TrafficNode node : adjacencyList.keySet()) {
            if (node instanceof Junction) {
                ((Junction) node).update(deltaTime);
            }
        }

        // Update Road segments
        for (Road road : allRoads) {
            road.update();
        }
        
        // Update Vehicles
        List<Vehicle> vehicleCopy = new ArrayList<>(vehicles);
        for (Vehicle v : vehicleCopy) {
            v.update(deltaTime);
        }
    }

    public TrafficNode findNodeAt(model.utility.TrafficPoint p, double threshold) {
    // Iterate through all nodes (keys in the adjacency list)
        for (TrafficNode node : adjacencyList.keySet()) {
            if (node.getCenterPoint().distanceTo(p) < threshold) {
                return node;
            }
        }
        return null;
    }
    
    // Getters and Setters
    public Map<TrafficNode, List<Road>> getAdjacencyList() {
        return adjacencyList;
    }

    public void setAdjacencyList(Map<TrafficNode, List<Road>> adjacencyList) {
        this.adjacencyList = adjacencyList;
    }

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    public List<Road> getAllRoads() {
        return allRoads;
    }

    public void setAllRoads(List<Road> allRoads) {
        this.allRoads = allRoads;
    }
}