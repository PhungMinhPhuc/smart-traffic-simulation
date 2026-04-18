package baseFeature.behavior;

import baseFeature.vehicle.Vehicle;

public class AggressiveDriver implements DriverBehavior {

    @Override
    public void changeSpeed(Vehicle vehicle) {
        System.out.println("Aggressive driver is changing speed.");
    }

    @Override
    public void changeLane(Vehicle vehicle) {
        System.out.println("Aggressive driver is changing lane.");
    }

    @Override
    public void onRedLight(Vehicle vehicle) {
        System.out.println("Aggressive driver is stopping at red light.");
    }

    @Override
    public void onPassingRequest(Vehicle vehicle) {
        System.out.println("Aggressive driver is yielding to passing request.");
    }

    @Override
    public void onEmergency(Vehicle vehicle) {
        System.out.println("Aggressive driver is taking emergency action.");
    }
}