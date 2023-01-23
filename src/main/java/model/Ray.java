package model;

public class Ray {
    private final double xCoor;
    private final double yCoor;
    private final double angle;

    public Ray(double xCoor, double yCoor, double angle) {
        this.xCoor = xCoor;
        this.yCoor = yCoor;
        this.angle = angle;
    }

    public double getxCoor() {
        return xCoor;
    }

    public double getyCoor() {
        return yCoor;
    }

    public double getAngle() {
        return angle;
    }
}
