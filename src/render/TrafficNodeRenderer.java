package render;

import config.Constants;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import model.node.TrafficNode;

public class TrafficNodeRenderer implements IRender<TrafficNode> {
	public Parent render(TrafficNode node) {
		Group trafficNodeGroup = new Group();
		Circle trafficNodeCircle = new Circle(node.getCenterPoint().getX(), node.getCenterPoint().getY(),
				node.getRadius());
		Circle centerCircle = new Circle(node.getCenterPoint().getX(), node.getCenterPoint().getY(), 2.0);

		trafficNodeCircle.setFill(Constants.JUNCTION_COLOR);
		centerCircle.setFill(Constants.JUNCTION_CENTER_POINT_COLOR);

		trafficNodeGroup.getChildren().add(trafficNodeCircle);
		trafficNodeGroup.getChildren().add(centerCircle);

		return trafficNodeGroup;
	}
}