package node;

import point.TrafficPoint;

public class TJunction extends TrafficNode {

    public TJunction(TrafficPoint centerPoint) {
        super(centerPoint);
        maxConnections = 3;
    }
}
