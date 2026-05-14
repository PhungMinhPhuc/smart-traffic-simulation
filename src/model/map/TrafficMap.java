package model.map;

import config.Constants;
import model.node.*;
import model.road.*;
import model.utility.*;
import model.vehicle.*;
import model.traffic.*;

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
				if (TrafficGeometry.intersectsCircle(newNode.getCenterPoint(), Constants.JUNCTION_MIN_RADIUS,
						node.getCenterPoint(), node.getRadius())) {
					return;
				}
			}
			nodeList.add(newNode);
		}
	}

	public void addConnection(TrafficNode startNode, TrafficNode endNode) {
		addConnection(startNode, endNode, Constants.DEFAULT_LANE_COUNT);
	}

	public void addConnection(TrafficNode startNode, TrafficNode endNode, int laneCountPerWay) {
		// Check node existence
		if (!nodeList.contains(startNode) || !nodeList.contains(endNode)) {
			System.out.println("One or both nodes do not exist in the map");
			return;
		}

		Road newRoad = new Road(startNode, endNode, laneCountPerWay);

		// Check collision with existing roads
		for (Road existingRoad : getRoadList()) {
			if (existingRoad.checkConflict(newRoad)) {
				System.out.println("New road conflicts with existing road:" + existingRoad.getId());
				return;
			}
		}

		// Add new road to map's roadList
		roadList.add(newRoad);

		// Add new road to the start and end node's road list
		newRoad.getStartNode().addRoad(newRoad);
		newRoad.getEndNode().addRoad(newRoad);
	}

	public void removeNode(TrafficNode removeNode) {
		if (removeNode == null || !nodeList.contains(removeNode)) {
			return;
		}

		// Create snapshot (clone) of the connected roads to avoid concurrent
		// modification exception when removing roads from the node's road list
		ArrayList<Road> connectedRoads = new ArrayList<>(removeNode.getRoadList());
		for (Road road : connectedRoads) {
			// Remove the roads connected to the node from the map's road list
			roadList.remove(road);

			// Remove the roads connected to the removeNode from all connected nodes road
			// list
			road.getStartNode().removeRoad(road);
			road.getEndNode().removeRoad(road);
		}

		// Remove the node from the map's node list
		nodeList.remove(removeNode);
	}

	// Testing method
	public void addDefaultVehicleToRoad(Road road, boolean isRightWay) {
		Way way = isRightWay ? road.getRightWay() : road.getLeftWay();
		if (way.getLaneList().isEmpty()) {
			System.out.println("No lane available on the way to add vehicle");
			return;
		}
		// Add a default vehicle to the first lane of the way
		Lane lane = way.getLaneList().get(0);
		Vehicle vehicle = new Car(lane.getStartPoint().clone(),
				new TrafficVector(lane.getStartPoint(), lane.getEndPoint()));
		lane.addVehicle(vehicle);
	}

	// Update the position of all vehicles in the map, this method will be called in
	// each time step of the simulation
	public void updateVehicles(double timeInterval) {
		// Update Junction logic
		for (TrafficNode node : nodeList) {
			node.update(timeInterval);
		}
		// Update Vehicles on Roads
		for (Road road : roadList) {
			updateWay(road.getRightWay(), timeInterval);
			updateWay(road.getLeftWay(), timeInterval);
		}

		// Update Vehicles on Junction Paths
		for (TrafficNode node : nodeList) {
			for (Path path : node.getPathList()) {
				List<Vehicle> pathVehicles = path.getVehicleList();
				for (int i = 0; i < pathVehicles.size(); i++) {
					Vehicle v = pathVehicles.get(i);
					Vehicle ahead = (i == 0) ? null : pathVehicles.get(i - 1);

					double distAhead = (ahead == null) ? -1 : v.getPosition().distanceTo(ahead.getPosition());
					double speedAhead = (ahead == null) ? 0 : ahead.getSpeed();

					// On a path inside a junction, we assume no red lights (already cleared)
					// and no lane changes.
					v.update(distAhead, speedAhead, 1000.0, false, false, false, false, timeInterval);
				}
			}
		}

		// Transition vehicles between roads and paths
		for (Road road : roadList) {
			road.transitionVehicles();
		}

		for (TrafficNode node : nodeList) {
			node.transitionVehicles();
		}
	}

	private void updateWay(Way way, double dt) {
		List<Lane> lanes = way.getLaneList();
		boolean isRed = (way.getTrafficLight() != null && way.getTrafficLight().getCurrentState() == LightState.RED);

		for (int l = 0; l < lanes.size(); l++) {
			Lane lane = lanes.get(l);
			List<Vehicle> vehicles = lane.getVehicleList();

			for (int i = 0; i < vehicles.size(); i++) {
				Vehicle v = vehicles.get(i);
				Vehicle ahead = (i == 0) ? null : vehicles.get(i - 1);

				double distAhead = (ahead == null) ? -1 : v.getPosition().distanceTo(ahead.getPosition());
				double speedAhead = (ahead == null) ? 0 : ahead.getSpeed();
				double distLight = v.getPosition().distanceTo(lane.getEndPoint());

				boolean canLeft = (l > 0);
				boolean canRight = (l < lanes.size() - 1);

				v.update(distAhead, speedAhead, distLight, isRed, canRight, canLeft, false, dt);
			}
		}
	}

	// Get unique set of TraffiNode
	public List<TrafficNode> getTrafficNodeList() {
		return nodeList;
	}

	// Get unique set of Road
	public List<Road> getRoadList() {
		return roadList;
	}

	public List<Vehicle> getVehicleList() {
		ArrayList<Vehicle> vehicleList = new ArrayList<>();
		// Get all vehicles from all lanes of all roads
		for (Road road : roadList) {
			for (Lane lane : road.getRightWay().getLaneList()) {
				vehicleList.addAll(lane.getVehicleList());
			}
			for (Lane lane : road.getLeftWay().getLaneList()) {
				vehicleList.addAll(lane.getVehicleList());
			}
		}

		// Get vehicle from all paths of all nodes
		for (TrafficNode node : nodeList) {
			for (Path path : node.getPathList()) {
				vehicleList.addAll(path.getVehicleList());
			}
		}

		return vehicleList;
	}

	public TrafficNode getNodeByPoint(TrafficPoint point) {
		for (TrafficNode node : nodeList) {
			if (node.containsPoint(point)) {
				return node;
			}
		}
		return null; // no node found at the point
	}

	public ArrayList<TrafficLight> getTrafficLightList() {
		ArrayList<TrafficLight> trafficLightList = new ArrayList<>();
		for (Road road : roadList) {
			if (road.getRightWay().getTrafficLight() != null)
				trafficLightList.add(road.getRightWay().getTrafficLight());
			if (road.getLeftWay().getTrafficLight() != null)
				trafficLightList.add(road.getLeftWay().getTrafficLight());
		}
		return trafficLightList;
	}
}