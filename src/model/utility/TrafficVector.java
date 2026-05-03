package model.utility;

import config.Constants;

public class TrafficVector {
    public double dx, dy;

    public TrafficVector(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public double length() {
        return Math.sqrt(dx * dx + dy * dy);
    }

    public TrafficVector normalize() {
        double len = length();
        if (len < Constants.EPS) return new TrafficVector(0, 0);
        return new TrafficVector(dx / len, dy / len);
    }

    public TrafficVector multiply(double scalar) {
        return new TrafficVector(dx * scalar, dy * scalar);
    }

    public TrafficVector getPerpendicular() {
        return new TrafficVector(-dy, dx);
    }
}
