package baseFeature.vehicle;

import java.awt.Point;
import baseFeature.behavior.DriverBehavior;
import baseFeature.generator.BehaviorFactory;
import baseFeature.supportClass.SoundPlayer;

public class Car extends Vehicle {

    // Constructor đầy đủ (dùng cho các trường hợp đặc biệt)
    public Car(Point coordinate, double width, double length, 
               double maxSpeed, String sound, DriverBehavior driverBehavior) {
        super(coordinate, width, length, maxSpeed, sound, driverBehavior); 
    }

    // Constructor rút gọn (dùng cho AutoGenerator)
    public Car(Point coordinate) {
        super(coordinate, 2.0, 4.0, 120.0, "car_honk.wav", BehaviorFactory.getRandomBehavior());
    }

    @Override
    public void makeSound() {
        SoundPlayer.playSound(this.getSound()); 
    }
}