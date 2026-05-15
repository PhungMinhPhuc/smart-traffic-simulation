package render;

import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.shape.Rectangle;
import model.vehicle.Vehicle;

public class VehicleRenderer implements IRender<Vehicle> {
	public Group createNode(Vehicle vehicle) {
		Rectangle rect = new Rectangle(vehicle.getWidth(), vehicle.getLength(), vehicle.getColor());
		rect.setX(-vehicle.getWidth() / 2);
		rect.setY(-vehicle.getLength() / 2);

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
