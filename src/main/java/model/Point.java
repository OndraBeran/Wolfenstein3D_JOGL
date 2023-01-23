package model;

public class Point {
    private final double x;
    private final double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public static double distance(Point a, Point b){
        double x = a.getX() - b.getX();
        double y = a.getY() - b.getY();
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }

    @Override
    public String toString(){
        return "X: " + getX() + ", Y: " + getY();
    }
}
