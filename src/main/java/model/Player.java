package model;

public class Player {

    private double xCoor;
    private double yCoor;
    private double angle;
    private double speedX;
    private double speedY;

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

    public double getSpeedX() {
        return speedX;
    }

    public void setSpeedX(double speedX) {
        this.speedX = speedX;
    }

    public double getSpeedY() {
        return speedY;
    }

    public void setSpeedY(double speedY) {
        this.speedY = speedY;
    }

    public Player(double xCoor, double yCoor, double angle) {
        this.xCoor = xCoor;
        this.yCoor = yCoor;
        this.angle = angle;
    }
}
