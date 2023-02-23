package model;

public class Gun {

    private int currentSprite = 0;
    private long lastUpdate = Long.MIN_VALUE / 2;
    private boolean playSound = false;

    private boolean shooting = false;
    private final int TIME_BETWEEN_SHOTS = 700;
    private long lastShot = 0;

    private int damage = 50;

    protected void update(){
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

    public long getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public boolean isShooting() {
        return shooting;
    }

    public void setShooting(boolean shooting) {
        this.shooting = shooting;

        if (shooting){
            playSound = true;
        }
    }

    public long getLastShot() {
        return lastShot;
    }

    public void setLastShot(long lastShot) {
        this.lastShot = lastShot;
    }

    public int getTIME_BETWEEN_SHOTS() {
        return TIME_BETWEEN_SHOTS;
    }

    public int getDamage() {
        return damage;
    }

    public boolean isPlaySound() {
        return playSound;
    }

    public void setPlaySound(boolean playSound) {
        this.playSound = playSound;
    }
}
