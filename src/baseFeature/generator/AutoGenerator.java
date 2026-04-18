package baseFeature.generator;

import java.util.Random;
import java.awt.Point;
import baseFeature.vehicle.*;

public class AutoGenerator {
    
    public enum VehicleType {
        CAR, FIRE_TRUCK, MOTORCYCLE
    }

    private static final Random RANDOM = new Random();

    public static Vehicle getVehicle(VehicleType type, Point coordinate) {
        if (coordinate == null) {
            coordinate = new Point(0, 0);
        }

        switch (type) {
            case FIRE_TRUCK:
                return new Firetruck(coordinate); 
            case MOTORCYCLE:
                return new Motorcycle(coordinate); 
            case CAR:
            default:
                return new Car(coordinate); 
        }
    }

    public static Vehicle getRandomVehicle(Point coordinate) {
        VehicleType[] types = VehicleType.values();
        VehicleType randomType = types[RANDOM.nextInt(types.length)];
        return getVehicle(randomType, coordinate);
    }

    public static Point generateRandomPoint(int maxX, int maxY) {
        return new Point(RANDOM.nextInt(maxX), RANDOM.nextInt(maxY));
    }
}