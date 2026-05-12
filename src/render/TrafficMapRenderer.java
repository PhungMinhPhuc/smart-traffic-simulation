package render;
import javafx.scene.Group;
import javafx.scene.Parent;
import model.map.*;
import model.node.*;
import model.road.*;

public class TrafficMapRenderer implements IRender<TrafficMap> {
	private static RoadRenderer roadRenderer = new RoadRenderer();
	private static TrafficNodeRenderer trafficNodeRenderer = new TrafficNodeRenderer();
	private static PathRenderer pathRenderer = new PathRenderer();
	
	@Override
	public Parent render(TrafficMap trafficMap) {
		Group mapGroup = new Group();
		// Layer 1: Render junction circles (bottom)
		for(TrafficNode node : trafficMap.getTrafficNodeList()) {
			mapGroup.getChildren().add(trafficNodeRenderer.render(node));
		}
		// Layer 2: Render roads on top of junctions
		for(Road road : trafficMap.getRoadList()) {
			mapGroup.getChildren().add(roadRenderer.render(road));
		}
		// Layer 3: Render paths on top of everything
		for(TrafficNode node : trafficMap.getTrafficNodeList()) {
			for(Path path : node.getPathList()) {
				mapGroup.getChildren().add(pathRenderer.render(path));
			}
		}
		return mapGroup;
	}

}