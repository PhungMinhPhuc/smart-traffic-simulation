package model.map;

import config.Constants;

public class Vector2D {
    public double dx, dy;

    public Vector2D(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public double length() {
        return Math.sqrt(dx * dx + dy * dy);
    }

    public Vector2D normalize() {
        double len = length();
        if (len < Constants.EPS) return new Vector2D(0, 0);
        return new Vector2D(dx / len, dy / len);
    }

    public Vector2D multiply(double scalar) {
        return new Vector2D(dx * scalar, dy * scalar);
    }

    public Vector2D getPerpendicular() {
        return new Vector2D(-dy, dx);
    }
}
