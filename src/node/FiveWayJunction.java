package node;

import point.Point;

public class FiveWayJunction extends Node {
    public FiveWayJunction(Point centerPoint) {
        super(centerPoint);
        maxConnections = 5;
    }
}
