package model.map;

import config.Constants;
import model.node.Path;
import model.node.TrafficNode;
import model.road.Lane;
import model.road.Road;
import model.road.Way;
import model.utility.TrafficGeometry;
import model.utility.TrafficPoint;
import model.vehicle.Vehicle;

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

    public void addNode(TrafficNode newNode) {
        if (!nodeList.contains(newNode)) {
            for (TrafficNode node : nodeList) {
                if (TrafficGeometry.intersectsCircle(newNode.getCenterPoint(), Constants.JUNCTION_RADIUS, node.getCenterPoint(), Constants.JUNCTION_RADIUS)) {
                    return;
                }
            }
            nodeList.add(newNode);
        }
    }

    public void addConnection(TrafficNode startNode, TrafficNode endNode) {
        if (!nodeList.contains(startNode) || !nodeList.contains(endNode)) {
            throw new IllegalArgumentException("Node does not exist");
        }

        Road newRoad = new Road(startNode, endNode);

        for (Road road : roadList) {
            if (newRoad.checkConflict(road)) return;
        }

        roadList.add(newRoad);

        startNode.addRoad(newRoad);
        endNode.addRoad(newRoad);
    }

    public void removeNode(TrafficNode removeNode) {
        if (removeNode == null || !nodeList.contains(removeNode)) {
            return;
        }

        //create snapshot(clone) of the connected roads to avoid concurrent modification exception when removing roads from the model.node's model.road list
        ArrayList<Road> connectedRoads = new ArrayList<>(removeNode.getRoadList());
        for (Road road : connectedRoads) {
            // remove the roads connected to the model.node from the model.map's model.road list
            roadList.remove(road);

            // remove the roads connected to the removeNode from all connected nodes' model.road list
            road.getStartNode().removeRoad(road);
            road.getEndNode().removeRoad(road);
        }

        // remove the model.node from the model.map's model.node list
        nodeList.remove(removeNode);
    }

    //Testing method
    public void addDefaultVehicleToRoad(Road road, boolean isRightWay) {
        Way way = isRightWay ? road.getRightWay() : road.getLeftWay();
        if(way.getLaneList().isEmpty()) {
            System.out.println("No lane available on the way to add vehicle");
            return;
        }
        //Add a default vehicle to the first lane of the way
        way.getLaneList().get(0).addVehicle();
    }


    //update the position of all vehicles in the map,
    // this method will be called in each time step of the simulation
    public void updateVehicles(double timeInterval) {
        for(Vehicle vehicle : getVehicleList()) {
            vehicle.move(timeInterval);
        }
    }

    public void updateNodes() {
        for(TrafficNode node : nodeList) {
            node.checkVehicleLeaving();;
        }
    }

    public void updateRoads() {
        for(Road road : roadList) {
            road.checkVehicleLeaving();
        }
    }

    public List<Vehicle> getVehicleList() {
        ArrayList<Vehicle> vehicleList = new ArrayList<>();
        //get all vehicles from all lanes of all roads
        for(Road road : roadList) {
            for(Lane lane : road.getRightWay().getLaneList()) {
                vehicleList.addAll(lane.getVehicleList());
            }
            for(Lane lane : road.getLeftWay().getLaneList()) {
                vehicleList.addAll(lane.getVehicleList());
            }
        }

        //get vehicle from all paths of all nodes
        for(TrafficNode node : nodeList) {
            for(Path path : node.getPathList()) {
                vehicleList.addAll(path.getVehicleList());
            }
        }

        return vehicleList;
    }

    public TrafficNode getNodeByPoint(TrafficPoint point) {
        for(TrafficNode node : nodeList) {
            if(node.containsPoint(point)) {
                return node;
            }
        }
        return null; //no node found at the point
    }

    public List<TrafficNode> getTrafficNodeList() {
        return nodeList;
    }

    public List<Road> getRoadList() {
        return roadList;
    }

}
