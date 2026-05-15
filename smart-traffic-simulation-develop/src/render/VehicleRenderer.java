package render;

import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.shape.Rectangle;
// import javafx.scene.text.Font;
// import javafx.scene.text.Text;
// import javafx.scene.paint.Color;
import model.vehicle.Vehicle;

//VehicleRenderer class that renders a vehicle as a rectangle rotated in its direction
public class VehicleRenderer implements IVehicleRenderer {
	public Group createNode(Vehicle vehicle) {
		Rectangle rect = new Rectangle(vehicle.getLength(), vehicle.getWidth(), vehicle.getColor());
		rect.setX(-vehicle.getLength() / 2);
		rect.setY(-vehicle.getWidth() / 2);

		// Text label = new Text(vehicle.getType());
		// label.setFont(new Font(7));
		// label.setFill(Color.WHITE);
		// label.setX(-vehicle.getLength() / 3); // Rough centering
		// label.setY(vehicle.getWidth() / 4);

		// Group rectGroup = new Group(rect, label);
		Group rectGroup = new Group(rect);
		updateNode(rectGroup, vehicle);
		return rectGroup;
	}

	public void updateNode(Group rectGroup, Vehicle vehicle) {
		Rectangle rect = (Rectangle) rectGroup.getChildren().get(0);
		// Text label = (Text) rectGroup.getChildren().get(1);

		rect.setFill(vehicle.getColor());
		// label.setText(vehicle.getType());

		rectGroup.setRotate(Math.toDegrees(vehicle.getDirection().getAngle()));
		rectGroup.setTranslateX(vehicle.getPosition().getX());
		rectGroup.setTranslateY(vehicle.getPosition().getY());
	}

	@Override
	public Parent render(Vehicle vehicle) {
		return createNode(vehicle);
	}
}