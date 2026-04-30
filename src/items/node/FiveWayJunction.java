package items.node;
import items.utility.Point2D;

public class FiveWayJunction extends TrafficNode{
    public static final int ROAD_NUM = 5;
    public double radius;

    public FiveWayJunction(Point2D point) {
        super(point);
        this.radius = 70.0;
    }
    
    @Override
    public double getRadius() {
    	return radius;
    }
}
