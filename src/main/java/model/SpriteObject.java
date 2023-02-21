package model;

public class SpriteObject {
    private final double x;
    private final double y;
    private final int index;
    private final int stage = 0;

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

    public int getStage() {
        return stage;
    }

    public Point getPosition(){
        return new Point(x, y);
    }
}
