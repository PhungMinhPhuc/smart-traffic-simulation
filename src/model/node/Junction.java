package model.node;
import model.utility.TrafficPoint;

public class Junction extends TrafficNode{
	private final int roadNum;
	
	public Junction(TrafficPoint point, int roadNum) {
		super(point);
		this.roadNum = roadNum;
	}
}
