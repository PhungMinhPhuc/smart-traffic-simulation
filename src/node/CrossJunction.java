package node;

import point.Point;

public class CrossJunction extends Node {
    public CrossJunction(Point centerPoint) {
        super(centerPoint);
        maxConnections = 4;
    }
}
