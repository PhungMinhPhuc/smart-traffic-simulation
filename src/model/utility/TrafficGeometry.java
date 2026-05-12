package model.utility;

import config.Constants;

public final class TrafficGeometry {
    private TrafficGeometry() {
    }

    public static TrafficPoint intersectsLines(
            TrafficPoint a1, TrafficPoint a2,
            TrafficPoint b1, TrafficPoint b2) {
        double x1 = a1.getX(), y1 = a1.getY();
        double x2 = a2.getX(), y2 = a2.getY();
        double x3 = b1.getX(), y3 = b1.getY();
        double x4 = b2.getX(), y4 = b2.getY();

        double denominator = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);
        if (Math.abs(denominator) < Constants.EPS) {
            return null; // parallel or overlapping
        }

        double det1 = x1 * y2 - y1 * x2;
        double det2 = x3 * y4 - y3 * x4;

        double x = (det1 * (x3 - x4) - (x1 - x2) * det2) / denominator;
        double y = (det1 * (y3 - y4) - (y1 - y2) * det2) / denominator;

        if (!isPointOnLine(x, y, x1, y1, x2, y2))
            return null;
        if (!isPointOnLine(x, y, x3, y3, x4, y4))
            return null;

        return new TrafficPoint(x, y);
    }

    private static boolean isPointOnLine(
            double px, double py,
            double x1, double y1,
            double x2, double y2) {
        double minX = Math.min(x1, x2) - Constants.EPS;
        double maxX = Math.max(x1, x2) + Constants.EPS;
        double minY = Math.min(y1, y2) - Constants.EPS;
        double maxY = Math.max(y1, y2) + Constants.EPS;

        return px >= minX && px <= maxX && py >= minY && py <= maxY;
    }

    public static boolean intersectsRectangles(
            TrafficPoint aStart, TrafficPoint aEnd, double aHalfWidth,
            TrafficPoint bStart, TrafficPoint bEnd, double bHalfWidth) {
        // Build the 4 corners of each rotated rectangle from its center line.
        TrafficPoint[] a = buildCorners(aStart, aEnd, aHalfWidth);
        TrafficPoint[] b = buildCorners(bStart, bEnd, bHalfWidth);

        // SAT axes: the 2 edge directions of each rectangle are enough.
        TrafficVector[] axes = new TrafficVector[] {
                new TrafficVector(a[0], a[1]),
                new TrafficVector(a[1], a[2]),
                new TrafficVector(b[0], b[1]),
                new TrafficVector(b[1], b[2])
        };

        for (TrafficVector axis : axes) {
            double axisLen = axis.length();
            if (axisLen == 0) {
                continue; // Ignore degenerate axes.
            }

            // Normalize the axis so projection values are comparable.
            double ax = axis.getX() / axisLen;
            double ay = axis.getY() / axisLen;

            double[] projA = project(a, ax, ay);
            double[] projB = project(b, ax, ay);

            // If projections do not overlap on any axis, the rectangles do not intersect.
            if (projA[1] < projB[0] || projB[1] < projA[0]) {
                return false;
            }
        }

        // No separating axis found, so the rectangles intersect.
        return true;
    }

    private static TrafficPoint[] buildCorners(TrafficPoint start, TrafficPoint end, double halfWidth) {
        TrafficVector dir = new TrafficVector(start, end);
        if (dir.length() == 0) {
            throw new IllegalArgumentException("start and end cannot be the same point");
        }

        // Unit direction along the center line of the rectangle.
        TrafficVector unit = dir.normalize();

        // Perpendicular vector used to offset the rectangle width.
        TrafficVector normal = new TrafficVector(-unit.getY(), unit.getX()).scale(halfWidth);

        // Return the 4 corners in clockwise order.
        return new TrafficPoint[] {
                start.moveByVector(normal),
                end.moveByVector(normal),
                end.moveByVector(normal.scale(-1)),
                start.moveByVector(normal.scale(-1))
        };
    }

    private static double[] project(TrafficPoint[] rect, double ax, double ay) {
        // Project all rectangle corners onto one axis and return [min, max].
        double min = rect[0].getX() * ax + rect[0].getY() * ay;
        double max = min;

        for (int i = 1; i < rect.length; i++) {
            double p = rect[i].getX() * ax + rect[i].getY() * ay;
            if (p < min)
                min = p;
            if (p > max)
                max = p;
        }

        return new double[] { min, max };
    }

    public static boolean intersectsCircle(TrafficPoint aCenter, double aRadius, TrafficPoint bCenter, double bRadius) {
        return aCenter.distanceTo(bCenter) <= aRadius + bRadius;
    }
}