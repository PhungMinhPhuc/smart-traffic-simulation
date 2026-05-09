package generator;

import java.util.Random;
import model.map.Point;
import model.vehicle.*;
import model.vehicle.behavior.*;

public class VehicleGenerator {
    public enum VehicleType {
        CAR, FIRETRUCK, MOTORCYCLE, AMBULANCE, TRUCK
    }
    //Phải tạo thêm hàm sinh tọa đỗ ngẫu nhiên cho xe nữa

    static final Random Rand = new Random();

     public static Vehicle getVehicle(VehicleType type, Point coordinate, DriverBehavior driverBehavior) {
        switch (type) {
            case FIRETRUCK:
                return new FireTruck(coordinate);
            case MOTORCYCLE:
                return new Motorbike(coordinate);
            case AMBULANCE:
                return new Ambulance(coordinate);
            case TRUCK:
                return new Truck(coordinate);
            default:
                return new Car(coordinate);
        }
    }
    public static Vehicle getRandomVehicle(Point coordinate) {
        VehicleType[] types = VehicleType.values();
        // Chọn ngẫu nhiên index từ 0 đến độ dài của mảng enum
        VehicleType randomType = types[Rand.nextInt(types.length)];
        if (randomType == VehicleType.FIRETRUCK || randomType == VehicleType.AMBULANCE) {
            return getVehicle(randomType, coordinate, BehaviorGenerator.getRandomBehavior_EmergencyIncluded());
        }
        else 
            return getVehicle(randomType, coordinate, BehaviorGenerator.getRandomBehavior());
    }
}
