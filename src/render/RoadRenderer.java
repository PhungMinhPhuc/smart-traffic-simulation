package render;
import items.road.*;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class RoadRenderer implements IRender<Road> {
	private static WayRenderer wayRenderer = new WayRenderer();
	
	@Override
	public Parent render(Road road) {
		Group roadGroup = new Group();
		roadGroup.getChildren().add(wayRenderer.render(road.getRightWay()));
		roadGroup.getChildren().add(wayRenderer.render(road.getLeftWay()));
		
		//add line between the two ways
		Line dividerLine = new Line(road.getStartPoint().getX(), road.getStartPoint().getY(), road.getEndPoint().getX(), road.getEndPoint().getY());
		dividerLine.setStrokeWidth(1.0);
		dividerLine.setStroke(Color.WHITE);
		roadGroup.getChildren().add(dividerLine);
		return roadGroup;
	}
	
}
