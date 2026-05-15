package model.node;

import model.utility.*;
import model.vehicle.Vehicle;
import config.Constants;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Junction extends TrafficNode {

    public Junction(TrafficPoint centerPoint) {
        super(centerPoint);
    }

    public Path getRandomPathFromPoint(TrafficPoint point) {
        ArrayList<Path> connectedPaths = new ArrayList<>();
        for (Path path : this.getPathList()) {
            if (path.getStartPoint().equals(point)) {
                connectedPaths.add(path);
            }
        }

        if (connectedPaths.isEmpty())
            return null;

        int randomIndex = (int) (Math.random() * connectedPaths.size());
        return connectedPaths.get(randomIndex);
    }

    /**
     * Calculates the "effective" distance to a vehicle ahead for a vehicle on a
     * path.
     * This accounts for both vehicles on the same path and vehicles on conflicting
     * paths.
     */
    public double getEffectiveDistanceAhead(Vehicle self, Path selfPath) {
        double minDistance = Double.MAX_VALUE;

        // If we are already close enough to transition, don't stop for others
        if (self.getPosition().distanceTo(selfPath.getEndPoint()) < Constants.MIN_DISTANCE_TO_END_POINT) {
            return -1;
        }

        // 1. Check vehicles on the same path
        List<Vehicle> samePathVehicles = selfPath.getVehicleList();
        int selfIndex = samePathVehicles.indexOf(self);
        if (selfIndex > 0) {
            Vehicle vehicleAhead = samePathVehicles.get(selfIndex - 1);
            minDistance = self.getPosition().distanceTo(vehicleAhead.getPosition());
        }

        // 2. Check vehicles on conflicting paths
        Map<Path, TrafficPoint> conflictPoints = selfPath.getConflictPointList();
        for (Map.Entry<Path, TrafficPoint> entry : conflictPoints.entrySet()) {
            Path otherPath = entry.getKey();
            TrafficPoint conflictPoint = entry.getValue();

            // Ignore conflict points that are at the very end of the path (shared exits)
            if (conflictPoint.distanceTo(selfPath.getEndPoint()) < Constants.MIN_DISTANCE_TO_END_POINT)
                continue;

            double distToConflict = self.getPosition().distanceTo(conflictPoint);

            if (!isConflictAhead(self, selfPath, conflictPoint))
                continue;

            for (Vehicle otherVeh : otherPath.getVehicleList()) {
                double otherDistToConflict = otherVeh.getPosition().distanceTo(conflictPoint);

                if (isConflictAhead(otherVeh, otherPath, conflictPoint)) {
                    // Only yield if the other vehicle is closer and not about to exit
                    if (otherDistToConflict < distToConflict &&
                            otherVeh.getPosition()
                                    .distanceTo(otherPath.getEndPoint()) > Constants.MIN_DISTANCE_TO_END_POINT) {
                        if (distToConflict < minDistance) {
                            minDistance = distToConflict;
                        }
                    }
                }
            }
        }

        return minDistance == Double.MAX_VALUE ? -1 : minDistance;
    }

    private boolean isConflictAhead(Vehicle v, Path path, TrafficPoint conflictPoint) {
        double distToStart = v.getPosition().distanceTo(path.getStartPoint());
        double conflictToStart = conflictPoint.distanceTo(path.getStartPoint());
        double distToEnd = v.getPosition().distanceTo(path.getEndPoint());
        double conflictToEnd = conflictPoint.distanceTo(path.getEndPoint());

        // A conflict is truly "ahead" if it's on our forward path and at a significant
        // distance
        return conflictToStart > distToStart && distToEnd > conflictToEnd + 5.0;
    }
}