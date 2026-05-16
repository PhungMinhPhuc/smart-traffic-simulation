package model.map;

import config.Constants;
import generator.*;
import model.node.*;
import model.road.*;
import model.utility.*;
import model.vehicle.*;
import model.traffic.*;
import model.transition.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TrafficMap {
	private List<TrafficNode> nodeList;
	private List<Road> roadList;

	public TrafficMap() {
		this.nodeList = new ArrayList<>();
		this.roadList = new ArrayList<>();
		System.out.println("Map created");
	}

	public void clear() {
		nodeList.clear();
		roadList.clear();
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
		if (!nodeList.contains(startNode) || !nodeList.contains(endNode)) {
			System.out.println("One or both nodes do not exist in the map");
			return;
		}

		Road newRoad = new Road(startNode, endNode, laneCountPerWay);

		for (Road existingRoad : getRoadList()) {
			if (existingRoad.checkConflict(newRoad)) {
				System.out.println("New road conflicts with existing road:" + existingRoad.getId());
				return;
			}
		}

		roadList.add(newRoad);
		newRoad.getStartNode().addRoad(newRoad);
		newRoad.getEndNode().addRoad(newRoad);
	}

	public void removeNode(TrafficNode removeNode) {
		if (removeNode == null || !nodeList.contains(removeNode)) {
			return;
		}

		ArrayList<Road> connectedRoads = new ArrayList<>(removeNode.getRoadList());
		for (Road road : connectedRoads) {
			roadList.remove(road);
			road.getStartNode().removeRoad(road);
			road.getEndNode().removeRoad(road);
		}

		nodeList.remove(removeNode);
	}

	// Test
	public void addDefaultVehicleToRoad(Road road, boolean isRightWay) {
		Way way = isRightWay ? road.getRightWay() : road.getLeftWay();
		if (way.getLaneList().isEmpty()) {
			System.out.println("No lane available on the way to add vehicle");
			return;
		}

		Lane lane = way.getLaneList().get(0);
		// Vehicle vehicle = new Car(lane.getStartPoint().clone(), new
		// TrafficVector(lane.getStartPoint(), lane.getEndPoint()));
		Vehicle vehicle = VehicleGenerator.getRandomVehicle(lane.getStartPoint().clone(),
				new TrafficVector(lane.getStartPoint(), lane.getEndPoint()));
		lane.addVehicle(vehicle);
	}

	public void updateVehicles(double timeInterval) {
		for (TrafficNode node : nodeList) {
			node.update(timeInterval);
		}

		for (Road road : roadList) {
			updateWay(road.getRightWay(), road.getLaneChangeHandler(), timeInterval);
			updateWay(road.getLeftWay(), road.getLaneChangeHandler(), timeInterval);
			road.updateLaneChanges(timeInterval);
		}

		for (TrafficNode node : nodeList) {
			for (Path path : node.getPathList()) {
				List<Vehicle> pathVehicles = path.getVehicleList();
				// Ensure order: closest to end is first
				pathVehicles.sort(Comparator.comparingDouble(v -> v.getPosition().distanceTo(path.getEndPoint())));

				for (int i = 0; i < pathVehicles.size(); i++) {
					Vehicle currentVehicle = pathVehicles.get(i);

					double distanceToAhead = -1;
					if (node instanceof Junction) {
						distanceToAhead = ((Junction) node).getEffectiveDistanceAhead(currentVehicle, path);
					}

					double speedOfAhead = 0;

					// If there's a vehicle ahead on the SAME path, use its speed
					if (i > 0) {
						speedOfAhead = pathVehicles.get(i - 1).getSpeed();
					}

					currentVehicle.update(distanceToAhead, speedOfAhead, 1000.0, false, false, false, -1, -1, false,
							false, timeInterval);
				}
			}
		}

		for (Road road : roadList) {
			road.transitionVehicles();
		}

		for (TrafficNode node : nodeList) {
			node.transitionVehicles();
		}

		pruneInvalidVehicles();
	}

	private void pruneInvalidVehicles() {
		for (Road road : roadList) {
			pruneWay(road.getRightWay());
			pruneWay(road.getLeftWay());
		}
		for (TrafficNode node : nodeList) {
			for (Path path : node.getPathList()) {
				path.getVehicleList().removeIf(v -> isVehicleInvalid(v, path.getStartPoint(), path.getEndPoint()));
			}
		}
	}

	private void pruneWay(Way way) {
		for (Lane lane : way.getLaneList()) {
			lane.getVehicleList().removeIf(v -> isVehicleInvalid(v, lane.getStartPoint(), lane.getEndPoint()));
		}
	}

	private boolean isVehicleInvalid(Vehicle v, TrafficPoint start, TrafficPoint end) {
		// 1. Check if vehicle has drifted too far from its assigned segment.
		// We allow a buffer larger than MIN_DISTANCE_TO_END_POINT to accommodate
		// "pre-transitioned" vehicles positioned slightly before the start of the
		// segment.
		double distToSegment = TrafficGeometry.getDistanceToSegment(v.getPosition(), start, end);
		double buffer = Constants.MIN_DISTANCE_TO_END_POINT + 15.0; // Total buffer for adherence

		if (distToSegment > buffer) {
			return true;
		}

		// 2. Check if vehicle is significantly past the end point of its segment
		// (missed transition)
		double segmentLength = start.distanceTo(end);
		double distFromStart = v.getPosition().distanceTo(start);
		if (distFromStart > segmentLength + 40.0) {
			return true;
		}

		return false;
	}

	private void updateWay(Way way, LaneChangeTransition handler, double deltaTime) {
		List<Lane> lanes = way.getLaneList();
		boolean isRed = (way.getTrafficLight() != null && way.getTrafficLight().getCurrentState() == LightState.RED);

		for (int l = 0; l < lanes.size(); l++) {
			Lane lane = lanes.get(l);
			List<Vehicle> vehicles = lane.getVehicleList();

			for (int i = 0; i < vehicles.size(); i++) {
				Vehicle currentVehicle = vehicles.get(i);
				Vehicle vehicleAhead = (i == 0) ? null : vehicles.get(i - 1);

				double distanceToVehicleAhead = (vehicleAhead == null) ? -1
						: currentVehicle.getPosition().distanceTo(vehicleAhead.getPosition());
				double speedOfVehicleAhead = (vehicleAhead == null) ? 0 : vehicleAhead.getSpeed();
				double distanceToLight = currentVehicle.getPosition().distanceTo(lane.getEndPoint());

				boolean canLeft = (l > 0);
				boolean canRight = (l < lanes.size() - 1);

				double distLeft = canLeft ? getDistanceToVehicleAheadInLane(currentVehicle, lanes.get(l - 1)) : -1;
				double distRight = canRight ? getDistanceToVehicleAheadInLane(currentVehicle, lanes.get(l + 1)) : -1;

				boolean isChangingLane = (handler != null && handler.isVehicleChangingLane(currentVehicle));

				currentVehicle.update(distanceToVehicleAhead, speedOfVehicleAhead, distanceToLight, isRed, canRight,
						canLeft, distLeft, distRight, false, isChangingLane, deltaTime);
			}
		}
	}

	private double getDistanceToVehicleAheadInLane(Vehicle currentVehicle, Lane lane) {
		double minDistance = Double.MAX_VALUE;
		boolean found = false;

		for (Vehicle v : lane.getVehicleList()) {
			// Check if v is ahead of currentVehicle by comparing distances to the end of
			// the road
			if (v.getPosition().distanceTo(lane.getEndPoint()) < currentVehicle.getPosition()
					.distanceTo(lane.getEndPoint())) {
				double dist = currentVehicle.getPosition().distanceTo(v.getPosition());
				if (dist < minDistance) {
					minDistance = dist;
					found = true;
				}
			}
		}
		return found ? minDistance : -1;
	}

	public List<TrafficNode> getTrafficNodeList() {
		return nodeList;
	}

	public List<Road> getRoadList() {
		return roadList;
	}

	public List<Vehicle> getVehicleList() {
		ArrayList<Vehicle> vehicleList = new ArrayList<>();
		for (Road road : roadList) {
			for (Lane lane : road.getRightWay().getLaneList()) {
				vehicleList.addAll(lane.getVehicleList());
			}
			for (Lane lane : road.getLeftWay().getLaneList()) {
				vehicleList.addAll(lane.getVehicleList());
			}
		}

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
		return null;
	}

	public Road getRoadByPoint(TrafficPoint point) {
		for (Road road : roadList) {
			if (TrafficGeometry.getDistanceToSegment(point, road.getStartPoint(),
					road.getEndPoint()) < Constants.LANE_WIDTH * road.getRightWay().getLaneList().size()) {
				return road;
			}
		}
		return null;
	}

	public void removeRoad(Road road) {
		if (road == null || !roadList.contains(road)) {
			return;
		}
		roadList.remove(road);
		road.getStartNode().removeRoad(road);
		road.getEndNode().removeRoad(road);
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

	public void setTrafficLightDisplayMode(int mode) {
		for (TrafficLight light : getTrafficLightList()) {
			light.setDisplayMode(mode);
		}
	}
}