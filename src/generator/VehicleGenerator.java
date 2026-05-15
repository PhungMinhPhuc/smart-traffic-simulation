package generator;

import java.util.Random;
import model.utility.*;
import model.vehicle.*;
import model.vehicle.behavior.*;

public class VehicleGenerator {
    public enum VehicleType {
        CAR, FIRETRUCK, MOTORCYCLE, AMBULANCE, TRUCK
    }
    //Phải tạo thêm hàm sinh tọa đỗ ngẫu nhiên cho xe nữa

    static final Random Rand = new Random();
     public static Vehicle getVehicle(VehicleType type, TrafficPoint coordinate, TrafficVector direction,  DriverBehavior driverBehavior) {
        switch (type) {
            case FIRETRUCK:
                return new FireTruck(coordinate, direction, driverBehavior );
            case MOTORCYCLE:
                return new Motorbike(coordinate, direction, driverBehavior );
            case AMBULANCE:
                return new Ambulance(coordinate, direction, driverBehavior  );
            case TRUCK:
                return new Truck(coordinate, direction, driverBehavior );
            default:
                return new Car(coordinate, direction, driverBehavior);
        }
    }

    public static Vehicle getRandomVehicle(TrafficPoint coordinate, TrafficVector direction) {
        VehicleType[] types = VehicleType.values();
        // Chọn ngẫu nhiên index từ 0 đến độ dài của mảng enum
        VehicleType randomType = types[Rand.nextInt(types.length)];
        if (randomType == VehicleType.FIRETRUCK || randomType == VehicleType.AMBULANCE) {
            return getVehicle(randomType, coordinate, direction, BehaviorGenerator.getRandomBehavior_EmergencyIncluded());
        }
        else 
            return getVehicle(randomType, coordinate, direction, BehaviorGenerator.getRandomBehavior());
    }
}