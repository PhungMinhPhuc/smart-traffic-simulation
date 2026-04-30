package render;
import items.map.*;
import items.node.*;
import items.road.*;
import javafx.scene.Group;
import javafx.scene.Parent;

public class TrafficMapRenderer implements IRender<TrafficMap> {
	private static RoadRenderer roadRenderer = new RoadRenderer();
	private static TrafficNodeRenderer trafficNodeRenderer = new TrafficNodeRenderer();
	
	@Override
	public Parent render(TrafficMap trafficMap) {
		Group mapGroup = new Group();
		for(Road road : trafficMap.getRoadList()) {
			mapGroup.getChildren().add(roadRenderer.render(road));
		}
		for(TrafficNode node : trafficMap.getTrafficNodeList()) {
			mapGroup.getChildren().add(trafficNodeRenderer.render(node));
		}
		return mapGroup;
	}

}
