package items.node;
import items.utility.Point2D;

public class TJunction extends TrafficNode{
    public static final int ROAD_NUM = 3;
    public double radius;

    public TJunction(Point2D point) {
        super(point);
        this.radius = 50.0;
    }
    
    @Override
    public double getRadius() {
		return radius;
	}
}