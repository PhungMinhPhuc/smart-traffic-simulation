package model.vehicle.behavior;

import model.vehicle.Vehicle;

public abstract class DriverBehavior {
	protected double speedRatio;       // Tỉ lệ tốc độ mong muốn so với maxSpeed
    protected double safeTimeGap;      // Khoảng cách an toàn tính bằng giây
    protected double accStrong;		   // Gia tốc tăng tốc gấp
    protected double accNormal;        // Gia tốc tăng tốc bình thường
    protected double brakeNormal;      // Gia tốc phanh bình thường
    protected double brakeStrong;      // Gia tốc phanh gấp
    protected double sightDistance;    // Tầm nhìn xa để phản ứng
    protected double overtakeThreshold; // Ngưỡng tốc độ xe trước để quyết định vượt

    public void decide(Vehicle self, double distAhead, double speedAhead, double distLight, 
    		boolean isRed, boolean canRight, boolean canLeft) {
        double freeWayAcc = handleFreeWay(self, distAhead);
        double followAcc = handleFollowVehicle(self, distAhead, speedAhead);
        double lightAcc = handleRedLight(self,distAhead, distLight, isRed);
        double finalAcc = Math.min(freeWayAcc, Math.min(followAcc, lightAcc));
        self.applyAcceleration(finalAcc);
        
        int offset = 0;
        if (handleLaneChange(self, distAhead, speedAhead)) {
            if (canLeft) offset = -1;
            else if (canRight) offset = 1;
        }
        
        self.setPendingLaneChange(offset); 
    }
   
    protected double calculateBrakeToStop(double currentSpeed, double distance) {
        if (distance <= 0.5) return 0;
        double targetAcc = -(currentSpeed * currentSpeed) / (2 * distance);
        return Math.max(brakeStrong, targetAcc);
    }

    protected double handleFreeWay(Vehicle self, double distAhead) {
    	if (distAhead >= 0) return Double.MAX_VALUE;
    	else if (distAhead <= 3) self.setSpeed(0.0);
        double targetSpeed = self.getMaxSpeed() * this.speedRatio;
        if (self.getSpeed() < targetSpeed) return this.accNormal;
        else if (self.getSpeed() > targetSpeed) return this.brakeNormal;
        return 0.0;
    }
    
    protected double handleFollowVehicle(Vehicle self, double distAhead, double speedAhead) {
    	if (distAhead < 0) return Double.MAX_VALUE;
    	
        double safetyGap = self.getSpeed() * this.safeTimeGap;

        if (distAhead < safetyGap) return self.getSpeed() > speedAhead ? brakeNormal : brakeStrong;
        else return this.accNormal;
        
    }
    
    protected double handleRedLight(Vehicle self, double distAhead, double distLight, boolean isRed) {
    	double distance = distAhead > distLight ? distAhead : distLight;
        if (!isRed || distance > sightDistance) return Double.MAX_VALUE;
        return this.calculateBrakeToStop(self.getSpeed(), distance);
    }
    
    protected boolean handleLaneChange(Vehicle self, double distAhead, double speedAhead) {
    	if (distAhead > 0) {
            double targetSpeed = self.getMaxSpeed() * this.speedRatio;
            
            if (distAhead < targetSpeed * this.safeTimeGap 
            		&& speedAhead < targetSpeed * this.overtakeThreshold)
            	return true;
            return false;
        }
    	return false;
    }
    
    protected void handleEmergency(Vehicle self) {
    }
    
    public abstract String getBehaviorName();

	public double getSpeedRatio() {
		return speedRatio;
	}
    
    
}