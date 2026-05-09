package model.node;

import java.util.ArrayList;

import config.Constants;
import generator.IdGenerator;
import model.road.Lane;
import model.road.Road;
import model.road.Way;
import model.utility.TrafficPoint;
import model.vehicle.Vehicle;
import model.observer.ITrafficObsever;

public abstract class TrafficNode implements ITrafficObsever {
	protected TrafficPoint centerPoint;
    protected ArrayList<Path> pathList = new ArrayList<Path>();
    protected ArrayList<Road> roadList = new ArrayList<Road>();
    protected String id;

    public TrafficNode(TrafficPoint point){
        centerPoint = (TrafficPoint)point.clone();
        id = IdGenerator.nodeId();
    }
    
    @Override
	public void checkVehicleLeaving() {
    	for(Path path : this.getPathList()) {
			for(Vehicle vehicle : path.getVehicleList()) {
				if(vehicle.getPosition().distance(path.getEndPoint()) < Constants.MIN_DISTANCE_TO_END_POINT) { //if vehicle get close enough to path's endPoint
					//store conected lanes for vehicles to enter after leaving the node
			    	ArrayList<Lane> connectedLanes = new ArrayList<Lane>(); 
					//get the lane connectd to this path
					for(Road road : roadList) {
						for(Way way : new Way[] {road.getLeftWay(), road.getRightWay()}) {
							for(Lane lane : way.getLaneList()) {
								if(lane.getStartPoint().equals(path.getEndPoint())) {
									connectedLanes.add(lane);
								}
							}
						}
					}
					//choose randomly one of the connected lanes for the vehicle to enter, if there is no connected lane, then the vehicle will be removed from the path
					if(!connectedLanes.isEmpty()) {
						int randomIndex = (int)(Math.random() * connectedLanes.size());
						Lane chosenLane = connectedLanes.get(randomIndex);
						path.getVehicleList().remove(vehicle); //remove vehicle from the path
						chosenLane.addVehicle(vehicle); //add vehicle to the chosen lane
					} else {
						path.getVehicleList().remove(vehicle); //remove vehicle from the path if there is no connected lane for it to enter
					}
				}
			}
		}
		
	}
    
    /**
     * - When adding a new road to the node, 
     * we need to create new paths between the entry way of the new road and all existing exit ways,
     * and new paths between the exit way of the new road and all existing entry ways.
     * - Then rebuild the conflict points for all paths after adding new paths.
     * @param newRoad
     */
    public void addRoad(Road newRoad){
    	//access the current road list and create current entry/exit ways
        ArrayList<Way> entryWays = new ArrayList<Way>();
        ArrayList<Way> exitWays = new ArrayList<Way>();
        for(Road road : roadList){
            Way rightWay = road.getRightWay();
            Way leftWay = road.getLeftWay();

            //decide which way is entry way and which way is exit way by the distance from center point to start point of each way
            if(centerPoint.distance(rightWay.getLaneList().get(0).getStartPoint()) //Right way is entry way
                > centerPoint.distance(leftWay.getLaneList().get(0).getStartPoint())){
                    entryWays.add(rightWay);
                    exitWays.add(leftWay);
            } else {
                    entryWays.add(leftWay);
                    exitWays.add(rightWay);
            }
        }
        
        //create new entry/exit ways for the new road to be added
        Way newEntryWay;
        Way newExitWay;
        //decide which way is entry way and which way is exit way by the distance from center point to start point of each way
        if(centerPoint.distance(newRoad.getRightWay().getLaneList().get(0).getStartPoint()) //Right way is entry way
				> centerPoint.distance(newRoad.getLeftWay().getLaneList().get(0).getStartPoint())){
					newEntryWay = newRoad.getRightWay();
					newExitWay = newRoad.getLeftWay();
		} else {
					newEntryWay = newRoad.getLeftWay();
					newExitWay = newRoad.getRightWay();
		}
        //create paths between newEntryWay and all existing exit ways
        for(Way exitWay : exitWays){
			for(Lane entryLane : newEntryWay.getLaneList()){
				for(Lane exitLane : exitWay.getLaneList()){
					Path path = new Path(
							IdGenerator.pathId(id,pathList.size()),
							entryLane.getEndPoint(), 
							exitLane.getStartPoint());
					pathList.add(path); //add new path to node's pathList
				}
			}
		}
		//create paths between newExitWay and all existing entry ways
		for(Way entryWay : entryWays){
			for(Lane entryLane : entryWay.getLaneList()){
				for(Lane exitLane : newExitWay.getLaneList()){
					Path path = new Path(
							IdGenerator.pathId(id,pathList.size()),
							entryLane.getEndPoint(), 
							exitLane.getStartPoint());
					pathList.add(path); //add new path to node's pathList
				}
			}
		}
        
        //add road to the road list and then build the conflict points again
        roadList.add(newRoad);
        buildAllConflictPoints(); //rebuild conflict points after adding new paths
    }

    /**
     * - When removing a road from the node, 
     * we need to remove all paths connected to the entry way and exit way of the road to be removed.
     * - If a path have start point equal to the end point of a lane in the entry way to be removed, 
     * or have end point equal to the start point of a lane in the exit way to be removed, 
     * => then this path should be removed from the path list.
     * - Then rebuild the conflict points for all paths after removing the road.
     * @param roadToRemove
     */
    public void removeRoad(Road roadToRemove){
    	if(!roadList.contains(roadToRemove)) {
			System.out.println("The road to be removed is not connected to this node");
			return;
		}
    	Way entryWayToRemove;
    	Way exitWayToRemove;
    	//decide which way is entry way and which way is exit way by the distance from center point to start point of each way
		if(centerPoint.distance(roadToRemove.getRightWay().getLaneList().get(0).getStartPoint()) //Right way is entry way
				> centerPoint.distance(roadToRemove.getLeftWay().getLaneList().get(0).getStartPoint())){
					entryWayToRemove = roadToRemove.getRightWay();
					exitWayToRemove = roadToRemove.getLeftWay();
		} else {
				entryWayToRemove = roadToRemove.getLeftWay();
				exitWayToRemove = roadToRemove.getRightWay();
		}
		//remove all paths connected to the entry way and exit way of the road to be removed
		ArrayList<Path> existingPaths = new ArrayList<Path>(pathList); //create a copy of the path list to avoid ConcurrentModificationException
		for(Path path : existingPaths) {
			for(Lane entryLane : entryWayToRemove.getLaneList()) {
				if(path.getStartPoint().equals(entryLane.getEndPoint())) { //if a path's start point is the same as the end point of a lane in the entry way to be removed, 
					pathList.remove(path);									//then remove this path from the path list
				}
			}
			for(Lane exitLane : exitWayToRemove.getLaneList()) { 
				if(path.getEndPoint().equals(exitLane.getStartPoint())) { //if a path's end point is the same as the start point of a lane in the exit way to be removed, 
					pathList.remove(path); 									//then remove this path from the path list
				}
			}
		}
		
    	//remove road from the road list and then build the conflict points again
		roadList.remove(roadToRemove);
		buildAllConflictPoints();
    }

    public void buildAllConflictPoints(){
    	for(Path path : pathList) {
    		path.getConflictPointList().clear(); //clear existing conflict points before rebuilding
    	}
        int length = pathList.size();
        for(int i = 0; i < length; i++){
            for(int j = i + 1; j < length; j++){
                Path path1 = pathList.get(i);
                Path path2 = pathList.get(j);
                TrafficPoint conflictPoint = path1.findConflictPoint(path2);
                if(conflictPoint != null){
                    path1.addConflictPoint(path2, conflictPoint);
                    path2.addConflictPoint(path1, conflictPoint);
                }
            }
        }
    }
    
    public boolean containsPoint(TrafficPoint point) {
		//check if the point is within a certain distance from the center point, if it is, then it is considered as containing the point
		double distance = centerPoint.distance(point);
		return distance <= Constants.JUNCTION_RADIUS; 
	}
    
    @Override
    public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		TrafficNode other = (TrafficNode) obj;
		return id.equals(other.id);
	}
    
    public TrafficPoint getCenterPoint() {
        return centerPoint;
    }

    public void setCenterPoint(TrafficPoint centerPoint) {
        this.centerPoint = (TrafficPoint)centerPoint.clone();
    }

    public ArrayList<Path> getPathList() {
        return pathList;
    }

    public ArrayList<Road> getRoadList() {
        return roadList;
    }

    public String getId(){
        return id;
    }
}
