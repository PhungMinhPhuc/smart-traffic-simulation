package baseFeature.test;

import java.awt.Point;
import baseFeature.vehicle.Car;
import baseFeature.behavior.DriverBehavior;
import baseFeature.behavior.NormalDriver;

public class TestCar {

    public static void main(String[] args) {

        //khởi tạo đối tượng DriverBehavior và Car
        DriverBehavior normalDriver = new NormalDriver();
        Point initialLocation = new Point(10, 10);
        
        Car myCar = new Car(
            initialLocation, 
            2.0,            //rộng
            4.8,            //dài
            220.0,          //vmax
            "car_honk.wav", //Tên file âm thanh
            normalDriver
        );

        System.out.println("=== Starting Car Test ===");

        //Hiển thị thông tin khởi tạo
        System.out.println("Initial Position: " + myCar.getCoordinate());
        System.out.println("Car Dimensions: " + myCar.getWidth() + "m x " + myCar.getLength() + "m");
        System.out.println("Max Speed: " + myCar.getMaxSpeed() + " km/h");
        System.out.println("Sound File Assigned: " + myCar.getSound());

        // Cập nhật các thuộc tính
        System.out.println("\n--- Updating Car State ---");
        myCar.setSpeed(80.5);
        myCar.setCoordinate(new Point(50, 150));
        
        System.out.println("New Speed: " + myCar.getSpeed() + " km/h");
        System.out.println("New Position: " + myCar.getCoordinate());

        // Kiểm tra tính năng âm thanh
        System.out.println("\n--- Testing Horn Sound ---");
        myCar.makeSound();

        // Duy trì chương trình để nghe thấy âm thanh
        // Do âm thanh chạy trên thread nền, cần đợi vài giây trước khi đóng
        try {
            System.out.println("Playing audio... please wait.");
            Thread.sleep(4000); 
        } catch (InterruptedException e) {
            System.err.println("Test interrupted.");
            e.printStackTrace();
        }

        System.out.println("\n=== Car Test Finished ===");
    }
}