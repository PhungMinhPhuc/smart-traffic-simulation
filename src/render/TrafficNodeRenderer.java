package render;

import items.node.Path;
import items.node.TrafficNode;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class TrafficNodeRenderer implements IRender<TrafficNode>{
	public Parent render(TrafficNode node) {
		
		PathRenderer pathRenderer = new PathRenderer();
		
		Group trafficNodeGroup = new Group();
		Circle trafficNodeCircle = new Circle(node.getCenterPoint().getX(), node.getCenterPoint().getY(), node.getRadius());
		Circle centerCircle = new Circle(node.getCenterPoint().getX(), node.getCenterPoint().getY(), 2.0);
		
		//coloring
		trafficNodeCircle.setFill(Color.GRAY);
		centerCircle.setFill(Color.WHITE);
		
		trafficNodeGroup.getChildren().add(trafficNodeCircle);
		trafficNodeGroup.getChildren().add(centerCircle);
		
		//Render the paths
		for(Path path : node.getPathList())
			trafficNodeGroup.getChildren().add(pathRenderer.render(path));
		
		return trafficNodeGroup;
	}
}
