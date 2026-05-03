package node;

import point.TrafficPoint;

public class FiveWayJunction extends TrafficNode {
    public FiveWayJunction(TrafficPoint centerPoint) {
        super(centerPoint);
        maxConnections = 5;
    }
}
