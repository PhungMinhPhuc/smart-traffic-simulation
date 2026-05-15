package render;

import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.shape.Rectangle;
import model.vehicle.Vehicle;

//VehicleRenderer class that renders a vehicle as a rectangle rotated in its direction
public class VehicleRenderer implements IVehicleRenderer {
	public Group createNode(Vehicle vehicle) {
		Rectangle rect = new Rectangle(vehicle.getLength(), vehicle.getWidth(), vehicle.getColor());
		rect.setX(-vehicle.getLength() / 2);
		rect.setY(-vehicle.getWidth() / 2);

		Group rectGroup = new Group(rect);
		updateNode(rectGroup, vehicle);
		return rectGroup;
	}

	public void updateNode(Group rectGroup, Vehicle vehicle) {
		Rectangle rect = (Rectangle) rectGroup.getChildren().get(0);
		rect.setFill(vehicle.getColor());
		rectGroup.setRotate(Math.toDegrees(vehicle.getDirection().getAngle()));
		rectGroup.setTranslateX(vehicle.getPosition().getX());
		rectGroup.setTranslateY(vehicle.getPosition().getY());
	}

	@Override
	public Parent render(Vehicle vehicle) {
		return createNode(vehicle);
	}
}