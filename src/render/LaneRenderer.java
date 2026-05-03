package render;

import config.Constants;
import javafx.scene.Parent;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import model.road.Lane;

public class LaneRenderer implements IRender<Lane> {

	@Override
	public Parent render(Lane lane) {
		Line laneLine = new Line(
				lane.getStartPoint().getX(), lane.getStartPoint().getY(),
			lane.getEndPoint().getX(), lane.getEndPoint().getY()
		);
		laneLine.setStrokeWidth(Constants.LANE_WIDTH);
		laneLine.setStroke(Color.GRAY);
		Group laneGroup = new Group();	
		laneGroup.getChildren().add(laneLine);
		return laneGroup;
	}
}
