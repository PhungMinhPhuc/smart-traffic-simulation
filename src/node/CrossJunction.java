package node;

import point.TrafficPoint;

public class CrossJunction extends TrafficNode {
    public CrossJunction(TrafficPoint centerPoint) {
        super(centerPoint);
        maxConnections = 4;
    }
}
