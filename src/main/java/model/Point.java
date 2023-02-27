package model;

public record Point(double x, double y) {
    public static final Point outOfBounds = new Point(Double.MAX_VALUE, Double.MAX_VALUE);

    public static double distance(Point a, Point b) {
        double x = a.x() - b.x();
        double y = a.y() - b.y();
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }

    public static double distance(double x1, double y1, double x2, double y2) {
        double x = x1 - x2;
        double y = y1 - y2;
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }

    @Override
    public String toString() {
        return "X: " + x() + ", Y: " + y();
    }
}
