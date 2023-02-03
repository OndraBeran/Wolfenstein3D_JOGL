package model;

public class Player {

    private double xCoor;
    private double yCoor;
    private double angle;
    private double velocity = 7;
    private double angleVelocity = 3;

    public double getxCoor() {
        return xCoor;
    }

    public void setxCoor(double xCoor) {
        this.xCoor = xCoor;
    }

    public double getyCoor() {
        return yCoor;
    }

    public void setyCoor(double yCoor) {
        this.yCoor = yCoor;
    }

    public double getAngle() {
        return angle;
    }

    public double getVelocity() {
        return velocity;
    }

    public double getAngleVelocity() {
        return angleVelocity;
    }

    public void setAngle(double angle) {
        if (angle < 0){
            this.angle = 360 + angle;
        } else if (angle >= 360){
            this.angle = angle % 360;
        } else {
            this.angle = angle;
        }
        System.out.println(this.angle);
    }

    public Player(double xCoor, double yCoor, double angle) {
        this.xCoor = xCoor;
        this.yCoor = yCoor;
        this.angle = angle;
    }
}
