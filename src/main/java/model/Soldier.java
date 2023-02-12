package model;

public class Soldier {
    private final double IDLE_SPEED = 0.5;
    private final int IDLE_ANIMATION = 150;

    private final double ENGAGED_SPEED = 24;

    private double x;
    private double y;

    private int currentSpriteStage = 0;
    private long lastUpdate = 0;
    private int orientatedSpriteIndex = 0;

    private Point[] idleTargets = new Point[2];
    private int targetIndex = 1;

    private int[] targetTile;
    private Point dirVector;

    private boolean idle = false;

    public Soldier(double x, double y, double idleX, double idleY) {
        this.x = x;
        this.y = y;

        idleTargets[0] = new Point(x, y);
        idleTargets[1] = new Point(idleX, idleY);

        targetTile[0] = Map.coordToTile(x) + 1;
        targetTile[1] = Map.coordToTile(y);
        dirVector = new Point(1, 0);
    }

    public void update(Point playerDirVect, double playerX, double playerY){
        if (System.currentTimeMillis() - lastUpdate > IDLE_ANIMATION){
            updatePos(playerDirVect, playerX, playerY);
            lastUpdate = System.currentTimeMillis();
            if (currentSpriteStage == 4){
                currentSpriteStage = 1;
            } else {
                currentSpriteStage++;
            }
        }
    }

    private void updatePos(Point player, double playerX, double playerY){
        if (idle){
            Point normDirVector = Point.normalizeVector(Point.pointsToVector(idleTargets[idleTargets.length - 1 - targetIndex], idleTargets[targetIndex]));
            x += normDirVector.getX() * IDLE_SPEED;
            y += normDirVector.getY() * IDLE_SPEED;

            if (Math.round(x) == Math.round(idleTargets[targetIndex].getX()) && Math.round(y) == Math.round(idleTargets[targetIndex].getY())){
                targetIndex = 1 - targetIndex;
            }
        } else {
            // eight possible directions
            Point[] dirVects = new Point[8];

            double angle = Point.angleToXAxis(player);

            double minDist = Double.MAX_VALUE;
            int indexOfSmallest = 0;

            for (int i = 0; i < 8; i++) {
                dirVects[i] = Point.normalVectFromAngle(angle);

                double newX = x + dirVects[i].getX();
                double newY = y + dirVects[i].getY();

                if (!Map.isWall(newX, newY)){
                    double dist = Point.distance(x + dirVects[i].getX(), y + dirVects[i].getY(), playerX, playerY);

                    if (dist < minDist) {
                        indexOfSmallest = i;
                        minDist = dist;
                    }
                }

                angle += 45;
            }

            x += dirVects[indexOfSmallest].getX() * ENGAGED_SPEED;
            y += dirVects[indexOfSmallest].getY() * ENGAGED_SPEED;

            switch (indexOfSmallest){
                case 0:
                    orientatedSpriteIndex = 4;
                    break;
                case 1:
                    orientatedSpriteIndex = 3;
                    break;
                case 2:
                    orientatedSpriteIndex = 2;
                    break;
                case 3:
                    orientatedSpriteIndex = 1;
                    break;
                case 4:
                    orientatedSpriteIndex = 0;
                    break;
                case 5:
                    orientatedSpriteIndex = 7;
                    break;
                case 6:
                    orientatedSpriteIndex = 6;
                    break;
                case 7:
                    orientatedSpriteIndex = 5;
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

    public int getCurrentSpriteStage() {
        return currentSpriteStage;
    }

    public Point getNormalDirVector(){
        if (idle){
            return Point.normalizeVector(Point.pointsToVector(idleTargets[idleTargets.length - 1 - targetIndex], idleTargets[targetIndex]));
        } else {
            //TODO doplnit
            return new Point(1, 0);
        }
    }

    public int getOrientatedSpriteIndex() {
        return orientatedSpriteIndex;
    }
}
