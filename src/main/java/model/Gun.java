package model;

public class Gun {

    private int currentSprite = 0;
    private long lastUpdate = Long.MIN_VALUE / 2;

    private boolean shooting = false;
    private final int TIME_BETWEEN_SHOTS = 700;
    private long lastShot = 0;

    double playerAngle = 0;

    private Soldier[] enemies;

    public Gun(Soldier[] enemies) {
        this.enemies = enemies;
    }

    protected void update(double angle){
        playerAngle = angle;

        if (KeyInputData.isShooting() && canShoot()){
            shoot();
        }

        if (System.currentTimeMillis() - lastUpdate > 128 && shooting){
            if (currentSprite == 2){
                currentSprite = 0;

                shooting = false;
            } else {
                currentSprite++;
            }

            lastUpdate = System.currentTimeMillis();
        }
    }

    public int getCurrentSprite() {
        return currentSprite;
    }

    private boolean canShoot(){
        long timeSinceLastShot = (System.currentTimeMillis() - lastShot);

        return timeSinceLastShot > TIME_BETWEEN_SHOTS;
    }

    private void shoot(){
        shooting = true;
        lastShot = System.currentTimeMillis();
    }

    private void checkHit(){
        for (Soldier enemy:
             enemies) {

        }
    }
}
