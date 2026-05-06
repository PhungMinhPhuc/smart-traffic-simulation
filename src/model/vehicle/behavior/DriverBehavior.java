package model.vehicle.behavior;

import model.vehicle.Vehicle;

public abstract class DriverBehavior {
	
	protected double sightDistance;
	
	  public void decide(Vehicle self, Vehicle ahead, double dist, boolean isRed) {
		  if (self.isEmergency()) handleEmergency(self);
		  else if (isRed && dist < sightDistance) handleRedLight(self, dist);  
		  else if (ahead != null) handleFollowVehicle(self, ahead);
		  else handleFreeWay(self);
	  }
	  
	  protected abstract void handleFreeWay (Vehicle self);
	  protected abstract void  handleFollowVehicle (Vehicle self, Vehicle ahead);
	  protected abstract void handleRedLight (Vehicle self, double distance);
	  protected abstract void handleEmergency (Vehicle self);
      public abstract String getBehaviorName();
}