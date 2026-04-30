package render;

import items.node.Path;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class PathRenderer implements IRender<Path>{
	
	@Override
	public Parent render(Path path) {
		Line pathLine = new Line(path.getStartPoint().getX(), path.getStartPoint().getY(),
				path.getEndPoint().getX(), path.getEndPoint().getY());
		pathLine.setStrokeWidth(2.0);
		pathLine.setStroke(Color.RED);
		Group pathGroup = new Group();
		pathGroup.getChildren().add(pathLine);
		return pathGroup;
	}
}
