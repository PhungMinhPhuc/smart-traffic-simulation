package render;

import items.road.Lane;
import javafx.scene.Parent;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class LaneRenderer implements IRender<Lane> {

	@Override
	public Parent render(Lane lane) {
		Line laneLine = new Line(
				lane.getStartPoint().getX(), lane.getStartPoint().getY(),
			lane.getEndPoint().getX(), lane.getEndPoint().getY()
		);
		laneLine.setStrokeWidth(Lane.LANEWIDTH);
		laneLine.setStroke(Color.GRAY);
		Group laneGroup = new Group();	
		laneGroup.getChildren().add(laneLine);
		return laneGroup;
	}
}
