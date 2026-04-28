package node;

import point.Point;

public class TJunction extends Node {

    public TJunction(Point centerPoint) {
        super(centerPoint);
        maxConnections = 3;
    }
}
