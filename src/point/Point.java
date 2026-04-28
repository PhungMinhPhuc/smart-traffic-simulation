package point;

import java.util.Objects;

public class Point {
    private double x;
    private double y;
    private static final double EPS = 1e-6;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public boolean isClose(Point other) {
        return Math.abs(x - other.x) < EPS && Math.abs(y - other.y) < EPS;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Point other = (Point) o;
        return Double.compare(x, other.x) == 0 && Double.compare(y, other.y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    public Point moveBy(Vector2D vector) {
        return new Point(x + vector.getX(), y + vector.getY());
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
