package items.utility;

public class Vector2D {
    private double x;
    private double y;

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }
    public Vector2D(Point2D start, Point2D end) {
        this.x = end.getX() - start.getX();
        this.y = end.getY() - start.getY();
    }

    public Vector2D rotateVector(double radianAngle) {
        double cosAngle = Math.cos(radianAngle);
        double sinAngle = Math.sin(radianAngle);
        double newX = this.x * cosAngle - this.y * sinAngle;
        double newY = this.x * sinAngle + this.y * cosAngle;
        return new Vector2D(newX, newY);
    }

    public Vector2D resizeVector(double newLength) {
        double currentLength = Math.sqrt(this.x * this.x + this.y * this.y);
        if (currentLength == 0) {
            return new Vector2D(0, 0);
        }
        double scale = newLength / currentLength;
        return new Vector2D(this.x * scale, this.y * scale);
    }

    public Point2D translatePoint(Point2D point, double distance) {
        Vector2D direction = new Vector2D(this.getX(), this.getY());
        Vector2D resizedDirection = direction.resizeVector(distance);
        return new Point2D(point.getX() + resizedDirection.x, point.getY() + resizedDirection.y);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
