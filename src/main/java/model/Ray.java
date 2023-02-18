package model;

public class Ray {
    private final double xCoor;
    private final double yCoor;
    private final double angle;

    public Ray(Point start, double angle) {
        xCoor = start.getX();
        yCoor = start.getY();
        if (angle < 0){
            this.angle = 360 + angle;
        } else if (angle >= 360){
            this.angle = angle % 360;
        } else {
            this.angle = angle;
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
