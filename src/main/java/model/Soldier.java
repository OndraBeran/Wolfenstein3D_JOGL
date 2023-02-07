package model;

public class Soldier {
    private static final double IDLE_SPEED = 0.5;
    private static final int IDLE_ANIMATION = 150;

    private static final double ENGAGED_SPEED = 1;

    private double x;
    private double y;

    private int currentSprite = 0;
    private long lastUpdate = 0;

    private Point[] idleTargets = new Point[2];
    private int targetIndex = 1;

    private boolean idle = true;

    public Soldier(double x, double y, double idleX, double idleY) {
        this.x = x;
        this.y = y;

        idleTargets[0] = new Point(x, y);
        idleTargets[1] = new Point(idleX, idleY);
    }

    public void update(Point playerDirVect){
        updatePos();
        if (System.currentTimeMillis() - lastUpdate > IDLE_ANIMATION){
            lastUpdate = System.currentTimeMillis();
            if (currentSprite == 4){
                currentSprite = 1;
            } else {
                currentSprite++;
            }
        }

        Point normDirVector = Point.normalizeVector(Point.pointsToVector(idleTargets[idleTargets.length - 1 - targetIndex], idleTargets[targetIndex]));
        //System.out.println(Point.angle(normDirVector, playerDirVect));

        System.out.println(normDirVector);
    }

    private void updatePos(){
        if (idle){
            Point normDirVector = Point.normalizeVector(Point.pointsToVector(idleTargets[idleTargets.length - 1 - targetIndex], idleTargets[targetIndex]));
            x += normDirVector.getX() * IDLE_SPEED;
            y += normDirVector.getY() * IDLE_SPEED;

            if (Math.round(x) == Math.round(idleTargets[targetIndex].getX()) && Math.round(y) == Math.round(idleTargets[targetIndex].getY())){
                targetIndex = 1 - targetIndex;
            }
        }
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public int getCurrentSprite() {
        return currentSprite;
    }

    public Point getNormalDirVector(){
        if (idle){
            return Point.normalizeVector(Point.pointsToVector(idleTargets[idleTargets.length - 1 - targetIndex], idleTargets[targetIndex]));
        } else {
            //TODO doplnit
            return null;
        }
    }
}
