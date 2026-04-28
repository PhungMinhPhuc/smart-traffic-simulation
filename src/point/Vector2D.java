package point;

public class Vector2D {
    private  double x;
    private double y;

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    // Create vector from 2 points
    public static Vector2D fromPoints(Point start,  Point end) {
        return new Vector2D(end.getX() - start.getX(), end.getY() - start.getY());
    }

    public double length() {
        return Math.sqrt(x * x + y * y);
    }

    public Vector2D normalize() {
        if (length() == 0) {
            throw new IllegalStateException("Zero length vector");
        }

        return new Vector2D(x / length(), y / length());
    }

    public Vector2D scale(double scale) {
        return new Vector2D(x * scale, y * scale);
    }

    // Rotate 90 degrees to the right
    public Vector2D rotateRight90() {
        return new Vector2D(y, -x);
    }

    // Rotate 180 degrees
    public Vector2D rotate180() {
        return new Vector2D(-x, -y);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
