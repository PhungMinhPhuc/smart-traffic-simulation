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
            return null; // Parallel or overlapping
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
        TrafficPoint[] a = buildCorners(aStart, aEnd, aHalfWidth);
        TrafficPoint[] b = buildCorners(bStart, bEnd, bHalfWidth);

        int[][] edges = new int[][] {
                { 0, 1 },
                { 1, 2 },
                { 2, 3 },
                { 3, 0 }
        };

        for (int[] edgeA : edges) {
            for (int[] edgeB : edges) {
                if (intersectsLines(
                        a[edgeA[0]], a[edgeA[1]],
                        b[edgeB[0]], b[edgeB[1]]) != null) {
                    return true;
                }
            }
        }

        return isPointInsideRectangle(a[0], b)
                || isPointInsideRectangle(b[0], a);
    }

    private static TrafficPoint[] buildCorners(TrafficPoint start, TrafficPoint end, double halfWidth) {
        TrafficVector dir = new TrafficVector(start, end);
        if (dir.length() == 0) {
            throw new IllegalArgumentException("Start and end cannot be the same point");
        }

        // Unit direction along the center line of the rectangle.
        TrafficVector unit = dir.normalize();

        // Perpendicular vector used to offset the rectangle width.
        TrafficVector normal = new TrafficVector(-unit.getY(), unit.getX()).scale(halfWidth);
        TrafficVector opposite = new TrafficVector(-normal.getX(), -normal.getY());

        // Return the 4 corners in clockwise order.
        return new TrafficPoint[] {
                start.moveByVector(normal),
                end.moveByVector(normal),
                end.moveByVector(opposite),
                start.moveByVector(opposite)
        };
    }

    private static boolean isPointInsideRectangle(TrafficPoint point, TrafficPoint[] rect) {
        double sign = 0.0;

        for (int i = 0; i < rect.length; i++) {
            TrafficPoint a = rect[i];
            TrafficPoint b = rect[(i + 1) % rect.length];
            double cross = cross(a, b, point);
            if (Math.abs(cross) < Constants.EPS) {
                continue;
            }

            if (sign == 0.0) {
                sign = cross;
            } else if (sign * cross < 0.0) {
                return false;
            }
        }

        return true;
    }

    private static double cross(TrafficPoint a, TrafficPoint b, TrafficPoint c) {
        return (b.getX() - a.getX()) * (c.getY() - a.getY())
                - (b.getY() - a.getY()) * (c.getX() - a.getX());
    }

    public static boolean intersectsCircle(TrafficPoint aCenter, double aRadius, TrafficPoint bCenter, double bRadius) {
        return aCenter.distanceTo(bCenter) <= aRadius + bRadius;
    }
}
