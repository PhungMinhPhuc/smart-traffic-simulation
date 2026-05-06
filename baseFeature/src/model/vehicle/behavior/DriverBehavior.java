package model.vehicle.behavior;

import model.vehicle.Vehicle;
import model.map.Lane;

public abstract class DriverBehavior {

    protected double safeTimeGap;
    protected double minDistance;
    protected double accelerationValue;
    protected double brakingValue;
    protected double stopLineDistance;
    
    protected abstract void handleFreeWay (Vehicle self);
    protected abstract void handleFollowVehicle (Vehicle self, Vehicle ahead);
    protected abstract void handleRedLight (Vehicle self, double distance);
    protected abstract void handleEmergency (Vehicle self);
    public abstract String getBehaviorName();

    public void decide(Vehicle self, Vehicle ahead, double dist, boolean isRed) {
          if (self.isEmergency()) handleEmergency(self);
          else if (isRed && dist < stopLineDistance) handleRedLight(self, dist);  
          else if (ahead != null) handleFollowVehicle(self, ahead);
          else handleFreeWay(self);
    }

    protected double safeDistance(Vehicle self) {
        return self.getSpeed() * safeTimeGap + minDistance;
    }

    protected double distance(Vehicle a, Vehicle b) {
        return a.getPosition().distanceTo(b.getPosition());
    }

    protected double brakingToStop(Vehicle self, double distance) {
        if (distance <= 1) return 0;

        double v = self.getSpeed();
        double a = -(v * v) / (2 * distance);

        return Math.max(a, brakingValue);
    }

    protected boolean shouldChangeLane(Vehicle self, int offset) {
        if (self.getCurrentLane() == null) return false;

        Lane neighbor = self.getCurrentLane().getNeighborLane(offset);
        if (neighbor == null) return false; // Không có làn bên cạnh thì không rẽ

        // Kiểm tra xem trên làn bên cạnh có xe phía trước không
        Vehicle aheadInNeighbor = neighbor.getVehicleAhead(self);
        if (aheadInNeighbor != null) {
            // Nếu khoảng cách đến xe đó nhỏ hơn khoảng cách an toàn -> không rẽ
            if (distance(self, aheadInNeighbor) < safeDistance(self)) {
                return false;
            }
        }

        // Không vướng xe nào quá gần thì rẽ được
        return true;
    }
}