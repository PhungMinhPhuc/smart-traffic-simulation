package baseFeature.test;
import generator.VehicleGenerator;
import model.map.Point;
import model.vehicle.*;
public class VehicleGenTest {
    static void main(String[] args) {
        Vehicle vehicle = VehicleGenerator.getRandomVehicle(new Point(0, 0));
        System.out.println(vehicle.getType());
        System.out.println(vehicle.getBehavior().getBehaviorName());
    }
}
