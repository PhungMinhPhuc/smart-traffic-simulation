package render;

import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.shape.Rectangle;
import model.vehicle.Vehicle;

//VehicleRenderer class that renders a vehicle as a rectangle rotated in its direction
public class VehicleRenderer implements IRender<Vehicle> {
	@Override
	public Parent render(Vehicle vehicle) {
		Rectangle rect = new Rectangle(vehicle.getWidth(), vehicle.getLength(), vehicle.getColor());
		rect.setX(-vehicle.getWidth() / 2);
		rect.setY(-vehicle.getLength() / 2);

		// Calculate angle from direction vector
		double angle = Math.toDegrees(vehicle.getDirection().getAngle());

		// Apply rotation and translation
		Group rectGroup = new Group(rect);
		rectGroup.setRotate(angle);
		rectGroup.setTranslateX(vehicle.getPosition().getX());
		rectGroup.setTranslateY(vehicle.getPosition().getY());

		return rectGroup;
	}

}