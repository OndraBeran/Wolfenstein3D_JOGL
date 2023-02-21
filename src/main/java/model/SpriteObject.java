package model;

public class SpriteObject {
    protected double x;
    protected double y;
    protected int index;

    public SpriteObject(double x, double y, int index) {
        this.x = x;
        this.y = y;
        this.index = index;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getIndex() {
        return index;
    }
}
