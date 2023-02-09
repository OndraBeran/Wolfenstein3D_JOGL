package model;

public class Gun {

    private int currentSprite = 0;
    private long lastUpdate = Long.MIN_VALUE / 2;

    protected void update(){
        if (System.currentTimeMillis() - lastUpdate > 128){
            if (currentSprite == 2){
                currentSprite = 0;
            } else {
                currentSprite++;
            }

            lastUpdate = System.currentTimeMillis();
        }
    }

    public int getCurrentSprite() {
        return currentSprite;
    }
}
