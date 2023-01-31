package model;

public class Ray {
    private final double xCoor;
    private final double yCoor;
    private final double angle;

    public Ray(double xCoor, double yCoor, double angle) {
        this.xCoor = xCoor;
        this.yCoor = yCoor;
        if (angle < 360) {
            this.angle = angle;
        }
        else{
            this.angle = angle % 360;
        }
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
