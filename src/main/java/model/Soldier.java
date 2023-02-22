package model;

import java.util.Random;

public class Soldier{
    private final double IDLE_SPEED = 12;
    private final int IDLE_ANIMATION = 150;

    private final int DYING_ANIMATION = 100;

    private final double ENGAGED_SPEED = 24;

    private final int WIDTH = 50;

    private double x;
    private double y;

    private int currentSpriteStage = 0;
    private long lastUpdate = 0;
    private int orientatedSpriteIndex = 0;

    private Player player;
    private int damageToBeSubtracted;

    //private Point[] idleTargets = new Point[2];
    private int targetIndex = 1;

    //TODO change to private
    public int[] targetTile;

    private boolean shooting = false;
    private long lastShot = System.currentTimeMillis();
    private int shootingSpriteIndex = 0;
    private final int TIME_BETWEEN_SHOTS = 2000;
    private final double MAX_DIST_TO_SHOOT = 1500;

    private boolean idle = true;
    private long lastSeenPlayer = 0;

    private int hp = 100;
    private boolean dying = false;
    private boolean dead = false;

    public Soldier(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void update(Point playerDirVect, double playerX, double playerY){
        updateState();

        if(targetTile == null) chooseTargetTile();

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

            double midwayX = x + dirVects[i].getX() / 2;
            double midwayY = y + dirVects[i].getY() / 2;

            if (Map.isWalkable(newX, newY) && Map.isWalkable(midwayX, midwayY)){
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

        if (Map.isWall(Map.coordToTile(x) - 1, Map.coordToTile(y)) && Map.coordInTile(x) < WIDTH / 2){
            x = Map.coordToTile(x) * Map.getTILE_SIZE() + WIDTH / 2;
        } else if (Map.isWall(Map.coordToTile(x) + 1, Map.coordToTile(y)) && Map.coordInTile(x) > Map.getTILE_SIZE() - WIDTH / 2){
            x = (Map.coordToTile(x) + 1) * Map.getTILE_SIZE() - WIDTH / 2;
        } else if (Map.isWall(Map.coordToTile(x), Map.coordToTile(y) - 1) && Map.coordInTile(y) < WIDTH / 2){
            y = (Map.coordToTile(y)) * Map.getTILE_SIZE() + WIDTH / 2;
        } else if (Map.isWall(Map.coordToTile(x), Map.coordToTile(y) + 1) && Map.coordInTile(y) > Map.getTILE_SIZE() - WIDTH / 2){
            y = (Map.coordToTile(y) + 1) * Map.getTILE_SIZE() - WIDTH / 2;
        }

        if (targetReached()){
            chooseTargetTile();
        }

        switch (indexOfSmallest){
            case 0:
                orientatedSpriteIndex = 4;
                break;
            case 1:
                orientatedSpriteIndex = 5;
                break;
            case 2:
                orientatedSpriteIndex = 6;
                break;
            case 3:
                orientatedSpriteIndex = 7;
                break;
            case 4:
                orientatedSpriteIndex = 0;
                break;
            case 5:
                orientatedSpriteIndex = 1;
                break;
            case 6:
                orientatedSpriteIndex = 2;
                break;
            case 7:
                orientatedSpriteIndex = 3;
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
            player.subtractHP(damageToBeSubtracted);
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

    private void updateState(){
        if (idle){
            idle = !canSeePlayer();
        } else {
            if (!canSeePlayer() && System.currentTimeMillis() - lastSeenPlayer > 3000){
                idle = true;
            }
        }

        if (canSeePlayer()){
            lastSeenPlayer = System.currentTimeMillis();
        }
    }

    private boolean canSeePlayer(){
        double distToPlayer = Point.distance(getCoordinates(), player.getCoordinates());

        Point enemyPlayerVector = new Point(player.getxCoor() - x, -(player.getyCoor() - y));
        double distToWall = RayCaster.castRay(getCoordinates(), Point.angleToXAxis(enemyPlayerVector));

        return distToWall > distToPlayer;
    }

    private void shoot(){
        shooting = true;
        lastShot = System.currentTimeMillis();
        orientatedSpriteIndex = 8;
        currentSpriteStage = -1;

        double distToPlayer = Point.distance(getCoordinates(), player.getCoordinates());
        int chanceToHit = 7;

        if (distToPlayer > 128 * 3) chanceToHit--;
        if (distToPlayer > 128 * 5) chanceToHit--;
        if (distToPlayer > 128 * 7) chanceToHit--;

        Random r = new Random();
        if (r.nextInt(10) + 1 < chanceToHit){
            //calculate damage
            int baseDamage = 30;

            if (distToPlayer < 128 * 5)baseDamage += 20;
            if (distToPlayer < 128 * 3)baseDamage += 20;

            damageToBeSubtracted = baseDamage;
        }
    }

    private boolean canShoot(){
        double distToPlayer = Point.distance(getCoordinates(), player.getCoordinates());
        long timeSinceLastShot = System.currentTimeMillis() - lastShot;

        if (!canSeePlayer())return false;
        if (distToPlayer > MAX_DIST_TO_SHOOT)return false;
        if (timeSinceLastShot < TIME_BETWEEN_SHOTS) return false;

        return true;
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

    public double getDistToPlayer(){
        return Point.distance(getCoordinates(), player.getCoordinates());
    }

    public void setPlayer(Player player) {
        this.player = player;
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
                if (!(i == currentX && j == currentY) && Map.isWalkable(i, j)){
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

            if (!(x == currentX && y == currentY) && Map.isWalkable(x, y)){
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
