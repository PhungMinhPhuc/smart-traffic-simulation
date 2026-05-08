package render;

import config.Constants;
import javafx.scene.Parent;
import javafx.scene.Group;
import javafx.scene.shape.Line;
import model.node.Path;

public class PathRenderer implements IRender<Path> {
    @Override
    public Parent render(Path path) {
        Line pathLine = new Line(
            path.getStartPoint().getX(), path.getStartPoint().getY(),
            path.getEndPoint().getX(), path.getEndPoint().getY()
        );
        pathLine.setStrokeWidth(Constants.PATH_WIDTH);
        pathLine.setStroke(Constants.PATH_COLOR);
        Group pathGroup = new Group();
        pathGroup.getChildren().add(pathLine);
        return pathGroup;
    }
}
