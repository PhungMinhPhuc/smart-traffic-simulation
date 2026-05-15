package render;

import config.Constants;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import model.road.Lane;
import model.road.Way;
import model.traffic.LightState;
import model.utility.*;

public class WayRenderer implements IRender<Way> {
	private static LaneRenderer laneRenderer = new LaneRenderer();

	@Override
	public Parent render(Way way) {
		Group wayGroup = new Group();
		for (Lane lane : way.getLaneList()) {
			wayGroup.getChildren().add(laneRenderer.render(lane));

		}
		// Render the lines between lanes
		for (int i = 0; i < way.getLaneList().size() - 1; i++) {
			Lane currentLane = way.getLaneList().get(i);
			Lane nextLane = way.getLaneList().get(i + 1);

			TrafficPoint dividerStartPoint = new TrafficPoint(
					(currentLane.getStartPoint().getX() + nextLane.getStartPoint().getX()) / 2.0,
					(currentLane.getStartPoint().getY() + nextLane.getStartPoint().getY()) / 2.0);
			TrafficPoint dividerEndPoint = new TrafficPoint(
					(currentLane.getEndPoint().getX() + nextLane.getEndPoint().getX()) / 2.0,
					(currentLane.getEndPoint().getY() + nextLane.getEndPoint().getY()) / 2.0);

			Line laneDividerLine = new Line(dividerStartPoint.getX(), dividerStartPoint.getY(), dividerEndPoint.getX(),
					dividerEndPoint.getY());

			laneDividerLine.setStrokeWidth(1.0);
			laneDividerLine.setStroke(Color.WHITE);
			laneDividerLine.getStrokeDashArray().addAll(10.0, 10.0); // Set dash pattern for dashed line
			wayGroup.getChildren().add(laneDividerLine);
		}

		// Render the traffic light as a perpendicular line centered at the lane end point.
		TrafficPoint firstLaneEndPoint = way.getLaneList().getFirst().getEndPoint();
		TrafficPoint lastLaneEndPoint = way.getLaneList().getLast().getEndPoint();

		TrafficVector lightVector = new TrafficVector(firstLaneEndPoint, lastLaneEndPoint);
		firstLaneEndPoint = lightVector.translatePoint(firstLaneEndPoint, -Constants.LANE_WIDTH / 2.0);
		lastLaneEndPoint = lightVector.translatePoint(lastLaneEndPoint, Constants.LANE_WIDTH / 2.0);

		Line lightLine = new Line(firstLaneEndPoint.getX(), firstLaneEndPoint.getY(), lastLaneEndPoint.getX(),
				lastLaneEndPoint.getY());
		lightLine.setStrokeWidth(2.0);
		
		LightState state = way.getTrafficLight().getCurrentState();
		if (state == LightState.RED) {
			lightLine.setStroke(Color.RED);
		} else if (state == LightState.YELLOW) {
			lightLine.setStroke(Color.YELLOW);
		} else {
			lightLine.setStroke(Color.LIMEGREEN);
		}
		wayGroup.getChildren().add(lightLine);

		return wayGroup;
	}

}