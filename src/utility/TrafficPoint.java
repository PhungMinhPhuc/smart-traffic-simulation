package utility;

import config.Constants;

import java.util.Objects;

public class TrafficPoint {
    private double x;
    private double y;

    public TrafficPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public TrafficPoint clone() {
        return new TrafficPoint(this.x, this.y);
    }

    public double distance(TrafficPoint other) {
        return Math.sqrt(Math.pow(this.x - other.x, 2) + Math.pow(this.y - other.y, 2));
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TrafficPoint other = (TrafficPoint) o;
        return Double.compare(x, other.x) == 0 && Double.compare(y, other.y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    public TrafficPoint moveBy(TrafficVector vector) {
        return new TrafficPoint(x + vector.getX(), y + vector.getY());
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

}
