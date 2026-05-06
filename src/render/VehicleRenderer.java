package render;

import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import model.vehicle.Vehicle;
import config.Constants;

public class VehicleRenderer implements IRender<Vehicle> {
	@Override
	public Parent render(Vehicle vehicle) {
		Circle circle = new Circle(Constants.LANE_WIDTH/2.0, Color.BLUE);
		circle.setCenterX(vehicle.getPosition().getX());
		circle.setCenterY(vehicle.getPosition().getY());
		Group vehicleGroup = new Group();
		vehicleGroup.getChildren().add(circle);
		return vehicleGroup;
	}
	
}
