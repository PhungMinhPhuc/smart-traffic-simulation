package model.road;

import java.util.ArrayList;
import config.Constants;
import model.traffic.LightState;
import model.traffic.TrafficLight;
import model.utility.TrafficPoint;
import model.utility.TrafficVector;

public class Way {
    private ArrayList<Lane> laneList = new ArrayList<Lane>();
    private TrafficLight trafficLight;
    private String roadId;
    private int laneCount;
    private boolean isRightWay;

    public Way(LightState lightState, int laneCount, boolean isRightWay, TrafficPoint roadStartPoint,
            TrafficPoint roadEndPoint, String roadId) {
        this.roadId = roadId;
        this.laneCount = laneCount;
        this.isRightWay = isRightWay;
        // Create lanes base on the lane count and the position of the road
        TrafficVector vectorRoad = new TrafficVector(roadStartPoint, roadEndPoint);
        if (isRightWay) {
            TrafficVector translateVector = vectorRoad.rotateVector(Math.toRadians(-90));
            translateVector = translateVector.scale(Constants.LANE_WIDTH / 2.0);
            for (int i = 0; i < laneCount; i++) {
                int j = 2 * i + 1;
                // Vectors of the right way's lanes have the opposite direction compared to the
                // road
                TrafficPoint laneStartPoint = new TrafficPoint(roadEndPoint.getX() + translateVector.getX() * j,
                        roadEndPoint.getY() + translateVector.getY() * j);
                TrafficPoint laneEndPoint = new TrafficPoint(roadStartPoint.getX() + translateVector.getX() * j,
                        roadStartPoint.getY() + translateVector.getY() * j);
                laneList.add(new Lane(i, laneStartPoint, laneEndPoint));
            }
        } else {
            TrafficVector translateVector = vectorRoad.rotateVector(Math.toRadians(90));
            translateVector = translateVector.scale(Constants.LANE_WIDTH / 2.0);

            for (int i = 0; i < laneCount; i++) {
                int j = 2 * i + 1;
                // Vectors of the left way's lanes have the same direction to the road
                TrafficPoint laneStartPoint = new TrafficPoint(roadStartPoint.getX() + translateVector.getX() * j,
                        roadStartPoint.getY() + translateVector.getY() * j);
                TrafficPoint laneEndPoint = new TrafficPoint(roadEndPoint.getX() + translateVector.getX() * j,
                        roadEndPoint.getY() + translateVector.getY() * j);
                laneList.add(new Lane(i, laneStartPoint, laneEndPoint));
            }
        }

        // Initialize TrafficLight at the end of the first lane (the stop line)
        if (!laneList.isEmpty()) {
            Lane firstLane = laneList.get(0);
            TrafficVector direction = new TrafficVector(firstLane.getStartPoint(), firstLane.getEndPoint());
            double angleDegrees = Math.toDegrees(direction.getAngle());
            this.trafficLight = new TrafficLight(firstLane.getEndPoint(), 1, angleDegrees);
            this.trafficLight.setState(lightState);
        }
    }

    public void updateLanes(TrafficPoint roadStartPoint, TrafficPoint roadEndPoint) {
        TrafficVector vectorRoad = new TrafficVector(roadStartPoint, roadEndPoint);
        TrafficVector translateVector = vectorRoad.rotateVector(Math.toRadians(isRightWay ? -90 : 90));
        translateVector = translateVector.scale(Constants.LANE_WIDTH / 2.0);

        for (int i = 0; i < laneCount; i++) {
            int j = 2 * i + 1;
            TrafficPoint start, end;
            if (isRightWay) {
                start = new TrafficPoint(roadEndPoint.getX() + translateVector.getX() * j,
                        roadEndPoint.getY() + translateVector.getY() * j);
                end = new TrafficPoint(roadStartPoint.getX() + translateVector.getX() * j,
                        roadStartPoint.getY() + translateVector.getY() * j);
            } else {
                start = new TrafficPoint(roadStartPoint.getX() + translateVector.getX() * j,
                        roadStartPoint.getY() + translateVector.getY() * j);
                end = new TrafficPoint(roadEndPoint.getX() + translateVector.getX() * j,
                        roadEndPoint.getY() + translateVector.getY() * j);
            }
            laneList.get(i).setPoints(start, end);
        }

        if (trafficLight != null && !laneList.isEmpty()) {
            Lane firstLane = laneList.get(0);
            trafficLight.setPosition(firstLane.getEndPoint());
            TrafficVector direction = new TrafficVector(firstLane.getStartPoint(), firstLane.getEndPoint());
            trafficLight.setRotation(Math.toDegrees(direction.getAngle()));
        }
    }

    public void update(double deltaTime) {
        if (trafficLight != null) {
            trafficLight.update(deltaTime, laneList);
        }
    }

    public TrafficLight getTrafficLight() {
        return trafficLight;
    }

    public void setTrafficLight(TrafficLight trafficLight) {
        this.trafficLight = trafficLight;
    }

    public ArrayList<Lane> getLaneList() {
        return laneList;
    }

    public String getRoadId() {
        return roadId;
    }
}