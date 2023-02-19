package model;

import java.util.Random;

public class Soldier {
    private final double IDLE_SPEED = 12;
    private final int IDLE_ANIMATION = 150;

    private final int DYING_ANIMATION = 100;

    private final double ENGAGED_SPEED = 0;

    private double x;
    private double y;

    private int currentSpriteStage = 0;
    private long lastUpdate = 0;
    private int orientatedSpriteIndex = 0;

    private Player player;

    private Point[] idleTargets = new Point[2];
    private int targetIndex = 1;

    //TODO change to private
    public int[] targetTile;

    private boolean shooting = false;
    private long lastShot = System.currentTimeMillis();
    private int shootingSpriteIndex = 0;
    private final int TIME_BETWEEN_SHOTS = 2000;
    private final double MAX_DIST_TO_SHOOT = 1000;

    private boolean idle = false;

    private int hp = 100;
    private boolean dying = false;
    private boolean dead = false;

    public Soldier(double x, double y, double idleX, double idleY, Player player) {
        this.x = x;
        this.y = y;
        this.player = player;

        idleTargets[0] = new Point(x, y);
        idleTargets[1] = new Point(idleX, idleY);

        chooseTargetTile();
    }

    public void update(Point playerDirVect, double playerX, double playerY){
        if (dead) return;

        int animationSpeed = dying ? DYING_ANIMATION : IDLE_ANIMATION;
        if (System.currentTimeMillis() - lastUpdate > animationSpeed){
            if(dying){
                updateSpriteDying();
                lastUpdate = System.currentTimeMillis();
                return;
            }

            if (canShoot()){
                shoot();
            }

            if (shooting){
                updateSpriteShooting();
            } else {
                updatePos(playerDirVect, playerX, playerY);
                updateSpriteMovement();
            }

            lastUpdate = System.currentTimeMillis();

        }
    }

    private void updatePos(Point player, double playerX, double playerY){
        // eight possible directions
        Point[] dirVects = new Point[8];

        double angle = Point.angleToXAxis(player);
        Point target = Map.centerOfTile(targetTile[0], targetTile[1]);

        double minDist = Double.MAX_VALUE;
        int indexOfSmallest = 0;

        for (int i = 0; i < 8; i++) {
            dirVects[i] = Point.normalVectFromAngle(angle);

            double newX = x + dirVects[i].getX();
            double newY = y + dirVects[i].getY();

            if (!Map.isWall(newX, newY)){
                double dist = Point.distance(x + dirVects[i].getX(), y + dirVects[i].getY(), target.getX(), target.getY());

                if (dist < minDist) {
                    indexOfSmallest = i;
                    minDist = dist;
                }
            }

            angle += 45;
        }

        double speed = idle ? IDLE_SPEED : ENGAGED_SPEED;

        x += dirVects[indexOfSmallest].getX() * speed;
        y += dirVects[indexOfSmallest].getY() * speed;

        if (targetReached()){
            chooseTargetTile();
        }

        switch (indexOfSmallest){
            case 0:
                orientatedSpriteIndex = 4;
                break;
            case 1:
                orientatedSpriteIndex = 7;
                break;
            case 2:
                orientatedSpriteIndex = 6;
                break;
            case 3:
                orientatedSpriteIndex = 5;
                break;
            case 4:
                orientatedSpriteIndex = 0;
                break;
            case 5:
                orientatedSpriteIndex = 3;
                break;
            case 6:
                orientatedSpriteIndex = 2;
                break;
            case 7:
                orientatedSpriteIndex = 1;
        }

    }

    private void updateSpriteMovement(){
        if (currentSpriteStage == 4){
            currentSpriteStage = 1;
        } else {
            currentSpriteStage++;
        }
    }

    private void updateSpriteShooting(){
        if (currentSpriteStage == 2){
            currentSpriteStage = 0;
            shooting = false;
        } else {
            currentSpriteStage++;
        }
    }

    private void updateSpriteDying(){
        if (currentSpriteStage == 4){
            dead = true;
        } else {
            currentSpriteStage++;
        }
    }

    private void shoot(){
        shooting = true;
        lastShot = System.currentTimeMillis();
        orientatedSpriteIndex = 8;
        currentSpriteStage = -1;
    }

    private boolean canShoot(){
        double dist = Point.distance(x, y, player.getxCoor(), player.getyCoor());
        long timeSinceLastShot = System.currentTimeMillis() - lastShot;

        return dist < MAX_DIST_TO_SHOOT && timeSinceLastShot > TIME_BETWEEN_SHOTS;
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

    private void chooseTargetTile(){
        //select random tile if enemy is idle
        if (idle){
            targetTile = randomTile();
            return;
        }

        int currentX = Map.coordToTile(x);
        int currentY = Map.coordToTile(y);

        int[][] possibleTiles = new int[8][2];
        int counter = 0;

        double minDist = Double.MAX_VALUE;
        int indexOfSmallest = 0;

        for (int i = currentX - 1; i <= currentX + 1; i++) {
            for (int j = currentY - 1; j <= currentY + 1; j++){
                if (!(i == currentX && j == currentY) && !Map.isWall(i, j)){
                    possibleTiles[counter] = new int[]{i, j};

                    double distToPlayer2 = Math.pow(i - Map.coordToTile(player.getxCoor()), 2) + Math.pow(j - Map.coordToTile(player.getyCoor()), 2);

                    if (distToPlayer2 < minDist){
                        minDist = distToPlayer2;
                        indexOfSmallest = counter;
                    }

                    counter++;
                }
            }
        }

        targetTile = new int[]{possibleTiles[indexOfSmallest][0], possibleTiles[indexOfSmallest][1]};
    }

    private int[] randomTile(){
        int[] tile = null;
        Random r = new Random();

        int currentX = Map.coordToTile(this.x);
        int currentY = Map.coordToTile(this.y);

        while (tile == null){
            int x = currentX + r.nextInt(3) - 1;
            int y = currentY + r.nextInt(3) - 1;

            if (!(x == currentX && y == currentY) && !Map.isWall(x, y)){
                tile = new int[]{x, y};
            }

        }

        return tile;
    }

    private boolean targetReached(){
        return Point.distance(Map.centerOfTile(targetTile[0], targetTile[1]), new Point(x, y)) <= ENGAGED_SPEED;
    }

    public int getOrientatedSpriteIndex() {
        return orientatedSpriteIndex;
    }

    public Point getCoordinates(){
        return new Point(x, y);
    }

    public void subtractHP(int amount){
        hp -= amount;
        if (hp <= 0){
            dying = true;
            orientatedSpriteIndex = 9;
            currentSpriteStage = 0;
        }
    }

    public boolean isDead() {
        return dead;
    }
}
