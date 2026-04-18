package baseFeature.behavior;

import baseFeature.vehicle.Vehicle;

public class EmergencyDriver implements DriverBehavior {

    @Override
    public void changeSpeed(Vehicle vehicle) {
        System.out.println("Emergency driver is changing speed.");
    }

    @Override
    public void changeLane(Vehicle vehicle) {
        System.out.println("Emergency driver is changing lane.");
    }

    @Override
    public void onRedLight(Vehicle vehicle) {
        System.out.println("Emergency driver is stopping at red light.");
    }

    @Override
    public void onPassingRequest(Vehicle vehicle) {
        System.out.println("Emergency driver is yielding to passing request.");
    }

    @Override
    public void onEmergency(Vehicle vehicle) {
        System.out.println("Emergency driver is taking emergency action.");
    }
}