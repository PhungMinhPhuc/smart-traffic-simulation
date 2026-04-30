package items.node;
import items.utility.Point2D;

public class CrossJunction extends TrafficNode{
    public static final int ROAD_NUM = 4;
    public double radius;

    public CrossJunction(Point2D point) {
        super(point);
        this.radius = 60.0;
    }
    
    @Override
    public double getRadius() {
		return radius;
	}
}
