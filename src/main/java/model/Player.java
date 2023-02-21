package model;

import java.util.ArrayList;

public class Player {

    private final double FOV;
    private final int MIN_DIST_TO_WALL = 30;

    private double xCoor;
    private double yCoor;
    private double angle;

    private int HP = 100;
    private boolean dead = false;
    private boolean invincible = true;

    private final double velocity = 16;
    private final double angleVelocity = 2;

    private long lastUpdate;

    private Gun gun;

    private ArrayList<Soldier> enemies;

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

    public void update(){
        if (dead) return;

        long timeSinceUpdate = System.currentTimeMillis() - lastUpdate;

        double increment = timeSinceUpdate / (1000 / 60.0);

        double rotationSpeed = adjustedAngleVelocity(System.currentTimeMillis() - KeyInputData.getStartedTurning());

        setAngle(angle + (KeyInputData.getRotation() * rotationSpeed * increment));

        double newY = yCoor - KeyInputData.getMovement() * (Math.sin(Math.toRadians(angle)) * velocity * increment);
        double newX = xCoor + KeyInputData.getMovement() * (Math.cos(Math.toRadians(angle)) * velocity * increment);

        if (!Map.isWall(newX, newY)){
            if (Map.isWall(Map.coordToTile(newX) - 1, Map.coordToTile(newY)) && Map.coordInTile(newX) < MIN_DIST_TO_WALL){
                newX = Map.coordToTile(newX) * Map.getTILE_SIZE() + MIN_DIST_TO_WALL;
            } else if (Map.isWall(Map.coordToTile(newX) + 1, Map.coordToTile(newY)) && Map.coordInTile(newX) > Map.getTILE_SIZE() - MIN_DIST_TO_WALL){
                newX = (Map.coordToTile(newX) + 1) * Map.getTILE_SIZE() - MIN_DIST_TO_WALL;
            } else if (Map.isWall(Map.coordToTile(newX), Map.coordToTile(newY) - 1) && Map.coordInTile(newY) < MIN_DIST_TO_WALL){
                newY = (Map.coordToTile(newY)) * Map.getTILE_SIZE() + MIN_DIST_TO_WALL;
            } else if (Map.isWall(Map.coordToTile(newX), Map.coordToTile(newY) + 1) && Map.coordInTile(newY) > Map.getTILE_SIZE() - MIN_DIST_TO_WALL){
                newY = (Map.coordToTile(newY) + 1) * Map.getTILE_SIZE() - MIN_DIST_TO_WALL;
            }
            xCoor = newX;
            yCoor = newY;
        }

        updateGun();

        lastUpdate = System.currentTimeMillis();
    }

    private void updateGun(){
        if (KeyInputData.isShooting() && canShoot()){
            shoot();
        }
        gun.update();
    }

    public void setAngle(double angle) {
        if (angle < 0){
            this.angle = 360 + angle;
        } else if (angle >= 360){
            this.angle = angle % 360;
        } else {
            this.angle = angle;
        }
    }

    public Point getCoordinates(){
        return new Point(xCoor, yCoor);
    }

    public Gun getGun() {
        return gun;
    }

    public Point getDirVector(){
        return new Point(Math.cos(Math.toRadians(angle)), Math.sin(Math.toRadians(angle)));
    }

    public Point getStandardDirVector(){
        return new Point(Math.cos(Math.toRadians(360 - angle)), Math.sin(Math.toRadians(360 - angle)));
    }

    public Point[] getFOVVectors(){
        Point[] vectors = new Point[2];

        vectors[0] = new Point(Math.cos(Math.toRadians(angle - (FOV / 2))), Math.sin(Math.toRadians(angle - (FOV / 2))));
        vectors[1] = new Point(Math.cos(Math.toRadians(angle + (FOV / 2))), Math.sin(Math.toRadians(angle + (FOV / 2))));

        return vectors;
    }

    public int getHP() {
        return HP;
    }

    public boolean isDead() {
        return dead;
    }



    public Player(double xCoor, double yCoor, double angle, double fov) {
        this.xCoor = xCoor;
        this.yCoor = yCoor;
        this.angle = angle;
        FOV = fov;

        lastUpdate = System.currentTimeMillis();

        gun = new Gun();

        RayCaster.setPlayer(this);
    }

    public double distToEnemy(Soldier soldier){
        double directDist = Point.distance(xCoor, yCoor, soldier.getX(), soldier.getY());

        return directDist;
    }

    public double angleToEnemy(Soldier soldier){
        Point playerVector = getDirVector();
        Point enemyVector = new Point(soldier.getX() - xCoor, -(soldier.getY() - yCoor));

        double dotProduct = playerVector.getX() * enemyVector.getX() + playerVector.getY() * enemyVector.getY();
        double angleCos = dotProduct / (playerVector.vectorMagnitude() * enemyVector.vectorMagnitude());

        return Math.toDegrees(Math.acos(angleCos));
    }

    private double adjustedAngleVelocity(long time){
        if (time < 100){
            return time / 100.0 * angleVelocity;
        } else {
            return angleVelocity;
        }
    }


    private boolean canShoot(){
        long timeSinceLastShot = (System.currentTimeMillis() - gun.getLastShot());

        return timeSinceLastShot > gun.getTIME_BETWEEN_SHOTS();
    }

    private void shoot(){
        gun.setShooting(true);
        gun.setLastShot(System.currentTimeMillis());
        checkHit();
    }

    private void checkHit(){
        for (Soldier enemy:
                enemies) {
            if (enemy.isDead()) continue;
        //check if enemy is in field of fire
            Point playerEnemyVector = new Point(enemy.getX() - xCoor, enemy.getY() - yCoor);


            //tan angle = deltaY/deltaX
            double angle = Point.angleToXAxis(playerEnemyVector) - (360 - this.angle);

            double angleDiff = Math.abs(angle);

            if (angleDiff > 5){
                continue;
            }

        //check if there is a wall between player and enemy
            double distToEnemy = Point.distance(getCoordinates(), enemy.getCoordinates());

            double angleOfRay = this.angle + angle;

            double distToWall = RayCaster.castRay(getCoordinates(), angleOfRay);

            if (distToWall > distToEnemy){
                processHit(enemy);
                break;
            }
        }
    }

    private void processHit(Soldier enemy){
        enemy.subtractHP(gun.getDamage());
    }

    public void subtractHP(int amount){
        if (dead || invincible) return;

        HP -= amount;
        if (HP <= 0) {
            HP = 0;
            dead = true;
        }
    }

    public void setEnemies(ArrayList<Soldier> enemies) {
        this.enemies = enemies;
    }
}
