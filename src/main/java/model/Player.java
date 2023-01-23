package model;

public class Player {
    private final int FOV = 60;
    private double xCoor;
    private double yCoor;
    private double angle;

    public int getFOV() {
        return FOV;
    }

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

    public void setAngle(double angle) {
        if(angle < 360){
            this.angle = angle;
        }
        else {
            this.angle = angle % 360;
        }
    }

    public Player(double xCoor, double yCoor, double angle) {
        this.xCoor = xCoor;
        this.yCoor = yCoor;
        this.angle = angle;
    }
}
