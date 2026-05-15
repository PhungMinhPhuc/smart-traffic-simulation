package render;

import config.Constants;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import model.vehicle.*;

import java.util.HashMap;
import java.util.Map;

public class ImageVehicleRenderer implements IVehicleRenderer {
    private final Map<Class<? extends Vehicle>, Image> vehicleImages = new HashMap<>();
    private final Map<Class<? extends Vehicle>, Double> vehicleIndividualScales = new HashMap<>();

    public ImageVehicleRenderer() {
        loadImages();
    }

    private void loadImages() {
        loadImage(Car.class, Constants.CAR_IMAGE, Constants.CAR_IMAGE_SCALE);
        loadImage(Ambulance.class, Constants.AMBULANCE_IMAGE, Constants.AMBULANCE_IMAGE_SCALE);
        loadImage(Bicycle.class, Constants.BICYCLE_IMAGE, Constants.BICYCLE_IMAGE_SCALE);
        loadImage(Bus.class, Constants.BUS_IMAGE, Constants.BUS_IMAGE_SCALE);
        loadImage(FireTruck.class, Constants.FIRE_TRUCK_IMAGE, Constants.FIRE_TRUCK_IMAGE_SCALE);
        loadImage(Motorbike.class, Constants.MOTORBIKE_IMAGE, Constants.MOTORBIKE_IMAGE_SCALE);
        loadImage(Truck.class, Constants.TRUCK_IMAGE, Constants.TRUCK_IMAGE_SCALE);
    }

    private void loadImage(Class<? extends Vehicle> clazz, String path, double individualScale) {
        vehicleIndividualScales.put(clazz, individualScale);
        try {
            var stream = getClass().getResourceAsStream(path);
            if (stream != null) {
                vehicleImages.put(clazz, new Image(stream));
            } else {
                System.err.println("Could not find image: " + path);
            }
        } catch (Exception e) {
            System.err.println("Error loading image " + path + ": " + e.getMessage());
        }
    }

    @Override
    public Group createNode(Vehicle vehicle) {
        Image img = vehicleImages.get(vehicle.getClass());
        if (img != null) {
            ImageView imageView = new ImageView(img);

            // Scale image using vehicle length, individual scale, and global scale
            double individualScale = vehicleIndividualScales.getOrDefault(vehicle.getClass(), 1.0);
            double size = vehicle.getLength() * individualScale * Constants.VEHICLE_IMAGE_SCALE;
            imageView.setFitWidth(size);
            imageView.setFitHeight(size);
            imageView.setPreserveRatio(true);

            // Center the image (rotation point is at the center of the Group)
            imageView.setX(-size / 2);
            imageView.setY(-size / 2);

            Group group = new Group(imageView);
            updateNode(group, vehicle);
            return group;
        } else {
            // Fallback to rectangle if image is missing
            Rectangle rect = new Rectangle(vehicle.getLength(), vehicle.getWidth(), vehicle.getColor());
            rect.setX(-vehicle.getLength() / 2);
            rect.setY(-vehicle.getWidth() / 2);
            Group group = new Group(rect);
            updateNode(group, vehicle);
            return group;
        }
    }

    @Override
    public void updateNode(Group node, Vehicle vehicle) {
        node.setRotate(Math.toDegrees(vehicle.getDirection().getAngle()));
        node.setTranslateX(vehicle.getPosition().getX());
        node.setTranslateY(vehicle.getPosition().getY());
    }

    @Override
    public Parent render(Vehicle vehicle) {
        return createNode(vehicle);
    }
}
