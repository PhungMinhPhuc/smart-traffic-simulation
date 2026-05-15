package model.transition;

import java.util.*;
import config.Constants;
import model.road.Lane;
import model.road.Way;
import model.utility.TrafficVector;
import model.utility.TrafficPoint;
import model.vehicle.Vehicle;
import main.java.handler.SoundManager;

public class LaneChangeTransition {
    private final Map<Vehicle, SmoothMoveState> activeChanges = new HashMap<>();
    private final Map<Vehicle, Double> cooldowns = new HashMap<>();
    private static final double COOLDOWN_DURATION = 2.0; // Slightly longer cooldown to prevent jittery lane changes

    public void processLaneChangeRequests(Way way) {
        List<Lane> lanes = way.getLaneList();
        List<LaneChangeRequest> requests = new ArrayList<>();

        for (Lane lane : lanes) {
            for (Vehicle vehicle : lane.getVehicleList()) {
                if (activeChanges.containsKey(vehicle)) {
                    vehicle.resetLaneChangeDirection();
                    continue;
                }
                
                if (cooldowns.containsKey(vehicle)) {
                    vehicle.resetLaneChangeDirection();
                    continue;
                }

                int offset = vehicle.getLaneChangeDirection();
                if (offset != 0) {
                    requests.add(new LaneChangeRequest(way, lane, vehicle, offset));
                }
                vehicle.resetLaneChangeDirection();
            }
        }

        Set<Vehicle> moved = new HashSet<>();
        for (LaneChangeRequest req : requests) {
            if (moved.contains(req.vehicle)) {
                continue;
            }
            if (isSafe(req.way, req.fromLane, req.vehicle, req.offset)) {
                startSmoothMove(req.way, req.fromLane, req.vehicle, req.offset);
                moved.add(req.vehicle);
            }
        }
    }

    private boolean isSafe(Way currentWay, Lane currentLane, Vehicle vehicle, int laneIndexOffset) {
        TrafficVector laneDirection = new TrafficVector(currentLane.getStartPoint(), currentLane.getEndPoint()).normalize();
        TrafficVector perpendicularVector = laneDirection.rotateVector(Math.toRadians(90));
        TrafficPoint targetPosition = perpendicularVector.translatePoint(vehicle.getPosition(), Constants.LANE_WIDTH * laneIndexOffset);
        
        int targetLaneIndex = currentLane.getIndex() + laneIndexOffset;
        if (targetLaneIndex < 0 || targetLaneIndex >= currentWay.getLaneList().size()) {
            return false;
        }
        
        for (Vehicle other : currentWay.getLaneList().get(targetLaneIndex).getVehicleList()) {
            if (other == vehicle) continue;
            double dist = targetPosition.distanceTo(other.getPosition());
            // Check for collision with safe margin
            double minSafeDist = (vehicle.getLength() + other.getLength()) / 2.0 + Constants.SAFE_DISTANCE * 0.2;
            if (dist < minSafeDist) {
                return false;
            }
        }

        // Check for conflicts with other vehicles currently changing lanes
        for (Map.Entry<Vehicle, SmoothMoveState> entry : activeChanges.entrySet()) {
            Vehicle other = entry.getKey();
            if (other == vehicle) continue;
            
            // Final position of other vehicle
            TrafficPoint otherTarget = entry.getValue().targetPoint;
            
            if (targetPosition.distanceTo(otherTarget) < (vehicle.getLength() + other.getLength()) / 2.0 + 10.0) {
                return false;
            }
        }
        return true;
    }

    public void updateTransitions(double deltaTime) {
        Iterator<Map.Entry<Vehicle, SmoothMoveState>> it = activeChanges.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Vehicle, SmoothMoveState> entry = it.next();
            Vehicle vehicle = entry.getKey();
            SmoothMoveState state = entry.getValue();

            // Progress is based on distance traveled.
            // We use a small minimum speed (5.0) so vehicles don't get stuck diagonally if they stop.
            double effectiveSpeed = Math.max(vehicle.getSpeed(), 5.0); 
            double distanceStep = deltaTime * effectiveSpeed;
            double progressStep = distanceStep / Constants.LANE_CHANGE_DISTANCE;
            double actualProgressStep = Math.min(progressStep, 1.0 - state.progress);
            
            state.progress += actualProgressStep;

            if (state.progress >= 1.0) {
                // Restore straight direction when finished
                vehicle.setDirection(state.targetDirection);
                it.remove();
                cooldowns.put(vehicle, COOLDOWN_DURATION);
            } else {
                // Keep diagonal direction during move
                vehicle.setDirection(state.diagonalDirection);
            }
        }
        
        Iterator<Map.Entry<Vehicle, Double>> cooldownIt = cooldowns.entrySet().iterator();
        while (cooldownIt.hasNext()) {
            Map.Entry<Vehicle, Double> entry = cooldownIt.next();
            double newTime = entry.getValue() - deltaTime;
            if (newTime <= 0) {
                cooldownIt.remove();
            } else {
                entry.setValue(newTime);
            }
        }
    }

    public boolean isVehicleChangingLane(Vehicle v) {
        return activeChanges.containsKey(v);
    }

    private void startSmoothMove(Way currentWay, Lane currentLane, Vehicle vehicle, int laneIndexOffset) {
        TrafficVector laneDir = new TrafficVector(currentLane.getStartPoint(), currentLane.getEndPoint()).normalize();
        TrafficVector lateralDir = laneDir.rotateVector(Math.toRadians(90));
        
        int newLaneIndex = currentLane.getIndex() + laneIndexOffset;
        Lane targetLane = currentWay.getLaneList().get(newLaneIndex);
        
        currentLane.removeVehicle(vehicle);
        targetLane.addVehicle(vehicle);

        SoundManager.play(vehicle.getSound());

        double lateralDistance = Constants.LANE_WIDTH * laneIndexOffset;
        TrafficPoint targetPosition = lateralDir.translatePoint(vehicle.getPosition(), lateralDistance);
        
        // Calculate the diagonal vector for the physics/renderer
        TrafficVector diagonal = new TrafficVector(
            laneDir.getX() * Constants.LANE_CHANGE_DISTANCE + lateralDir.getX() * lateralDistance,
            laneDir.getY() * Constants.LANE_CHANGE_DISTANCE + lateralDir.getY() * lateralDistance
        ).normalize();

        vehicle.setDirection(diagonal);
        activeChanges.put(vehicle, new SmoothMoveState(diagonal, laneDir, targetPosition));
    }

    private static class SmoothMoveState {
        TrafficVector diagonalDirection;
        TrafficVector targetDirection;
        TrafficPoint targetPoint;
        double progress = 0.0;

        SmoothMoveState(TrafficVector diagonal, TrafficVector target, TrafficPoint targetPt) {
            this.diagonalDirection = diagonal;
            this.targetDirection = target;
            this.targetPoint = targetPt;
        }
    }

    private static class LaneChangeRequest {
        Way way;
        Lane fromLane;
        Vehicle vehicle;
        int offset;

        LaneChangeRequest(Way w, Lane f, Vehicle v, int o) {
            this.way = w;
            this.fromLane = f;
            this.vehicle = v;
            this.offset = o;
        }
    }
}
