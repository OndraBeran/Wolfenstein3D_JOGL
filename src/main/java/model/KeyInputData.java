package model;

public class KeyInputData {
    private static int movement = 0;
    private static int rotation = 0;
    private static boolean shooting = false;
    private static boolean debugging = false;
    private static boolean drawWalls = true;
    private static boolean restart = false;

    private static long startedTurning = System.currentTimeMillis();

    public static synchronized int getMovement() {
        return movement;
    }

    public static synchronized void setMovement(int movement) {
        KeyInputData.movement = movement;
    }

    public static synchronized int getRotation() {
        return rotation;
    }

    public static synchronized void setRotation(int rotation) {
        if (rotation != KeyInputData.rotation){
            startedTurning = System.currentTimeMillis();
        }
        KeyInputData.rotation = rotation;
    }

    public static synchronized boolean isShooting() {
        return shooting;
    }

    public static synchronized void setShooting(boolean shooting) {
        KeyInputData.shooting = shooting;
    }

    public static synchronized boolean isDebugging() {
        return debugging;
    }

    public static synchronized void setDebugging(boolean debugging) {
        KeyInputData.debugging = debugging;
    }

    public static synchronized long getStartedTurning() {
        return startedTurning;
    }

    public static synchronized boolean isRestart() {
        return restart;
    }

    public static synchronized void setRestart(boolean restart) {
        KeyInputData.restart = restart;
    }

    public static synchronized boolean isDrawWalls() {
        return drawWalls;
    }

    public static synchronized void setDrawWalls(boolean drawWalls) {
        KeyInputData.drawWalls = drawWalls;
    }
}

