package render;

import config.Constants;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.shape.Line;
import model.road.Lane;
import model.road.Way;
import model.utility.*;

public class WayRenderer implements IRender<Way> {
    private static LaneRenderer laneRenderer = new LaneRenderer();

    @Override
    public Parent render(Way way) {
        Group wayGroup = new Group();
        for(Lane lane : way.getLaneList()) {
            wayGroup.getChildren().add(laneRenderer.render(lane));
        }

        // Render the lane markings (the lines between lanes)
        for(int i = 0; i < way.getLaneList().size() - 1; i++) {
            Lane currentLane = way.getLaneList().get(i);
			Lane nextLane = way.getLaneList().get(i + 1);
			 
			TrafficPoint dividerStartPoint = new TrafficPoint(
				(currentLane.getStartPoint().getX() + nextLane.getStartPoint().getX()) / 2.0,
				(currentLane.getStartPoint().getY() + nextLane.getStartPoint().getY()) / 2.0);
			TrafficPoint dividerEndPoint = new TrafficPoint(
                (currentLane.getEndPoint().getX() + nextLane.getEndPoint().getX()) / 2.0,
                (currentLane.getEndPoint().getY() + nextLane.getEndPoint().getY()) / 2.0
                );
            
            Line laneDividerLine = new Line(dividerStartPoint.getX(), dividerStartPoint.getY(), dividerEndPoint.getX(), dividerEndPoint.getY());
            laneDividerLine.setStrokeWidth(Constants.LANE_MARKING_WIDTH);
            laneDividerLine.setStroke(Constants.LANE_MARKING_COLOR);
            laneDividerLine.getStrokeDashArray().addAll(10.0, 10.0); // Dashed line
            wayGroup.getChildren().add(laneDividerLine);
        }

        // Render stop line at intersection
        TrafficPoint firstLaneEndPoint = way.getLaneList().getFirst().getEndPoint();
		TrafficPoint lastLaneEndPoint = way.getLaneList().getLast().getEndPoint();

        TrafficVector stopLineVector = new TrafficVector(firstLaneEndPoint, lastLaneEndPoint);
		firstLaneEndPoint = stopLineVector.translatePoint(firstLaneEndPoint, -Constants.LANE_WIDTH / 2.0);
		lastLaneEndPoint = stopLineVector.translatePoint(lastLaneEndPoint, Constants.LANE_WIDTH / 2.0);

        Line stopLine = new Line(firstLaneEndPoint.getX(), firstLaneEndPoint.getY(), lastLaneEndPoint.getX(), lastLaneEndPoint.getY());
        stopLine.setStrokeWidth(Constants.STOP_LINE_WIDTH);
        stopLine.setStroke(Constants.STOP_LINE_MARKING_COLOR);
        wayGroup.getChildren().add(stopLine);

        return wayGroup;
    }
}
